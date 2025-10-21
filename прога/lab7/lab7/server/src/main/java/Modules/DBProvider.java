package Modules;

import CollectionObject.*;
import Network.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.sql.*;
import java.util.Date;

public class DBProvider {
    private static Connection connection;

    public static void establishConnection(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка подключения к БД", e);
        }
    }

    public static boolean checkUserExistence(String username) {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, username);
            ResultSet res = p.executeQuery();
            return res.next() && res.getBoolean(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки пользователя", e);
        }
    }

    public static boolean checkUserPassword(User user) {
        String query = "SELECT hashedpassword FROM users WHERE username = ?";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getUsername());
            ResultSet res = p.executeQuery();
            return res.next() && res.getString(1).equals(hashPasswordMD2(user.getPassword()));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки пароля", e);
        }
    }

    public static void addUser(User user) {
        String query = "INSERT INTO users (username, hashedpassword) VALUES (?, ?)";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getUsername());
            p.setString(2, hashPasswordMD2(user.getPassword()));
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка регистрации: " + e.getMessage());
        }
    }
    public static boolean clearAllRoutes() {
        String query = "TRUNCATE TABLE routes CASCADE";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.executeUpdate();

            // Сбрасываем последовательность ID
            String resetSequence = "ALTER SEQUENCE routes_id_seq RESTART WITH 1";
            try (PreparedStatement ps = connection.prepareStatement(resetSequence)) {
                ps.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка полной очистки коллекции", e);
        }
    }
    // Добавлен метод хэширования MD2
    private static String hashPasswordMD2(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм MD2 недоступен", e);
        }
    }

    public static void load() {
        CollectionService.elementsCount = loadElCount();
        String query = "SELECT r.id, r.name, r.x, r.y, r.creationdate, "
                + "f.x as from_x, f.y as from_y, f.z as from_z, f.name as from_name, "
                + "t.x as to_x, t.y as to_y, t.z as to_z, t.name as to_name, "
                + "r.distance, u.username FROM routes r "
                + "JOIN users u ON u.id = r.creatorid "
                + "JOIN locations f ON r.from_id = f.id "  // Исправлено: from_id вместо from_location
                + "JOIN locations t ON r.to_id = t.id";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            ResultSet res = p.executeQuery();
            while (res.next()) {
                // Создание объектов локаций
                LocationFrom from = new LocationFrom(
                        res.getDouble("from_x"),
                        res.getDouble("from_y"),
                        res.getInt("from_z"),
                        res.getString("from_name")
                );

                LocationTo to = new LocationTo(
                        res.getDouble("to_x"),
                        res.getDouble("to_y"),
                        res.getInt("to_z"),
                        res.getString("to_name")
                );

                // Создание объекта Route
                Route route = new Route(
                        res.getLong("id"),
                        res.getString("name"),
                        new Coordinates(res.getFloat("x"), res.getDouble("y")),
                        res.getDate("creationdate"),
                        from,
                        to,
                        res.getDouble("distance"),
                        res.getString("username")
                );

                // Добавление в коллекцию
                CollectionService.collection.put(route.getId(), route);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки данных", e);
        }
    }

    public static boolean addRoute(RouteModel routeModel, User user) {
        String query = "INSERT INTO routes (name, x, y, creationdate, from_id, to_id, distance, creatorid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?))";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            long fromId = saveLocationFrom(routeModel.getFrom());
            long toId = saveLocationTo(routeModel.getTo());

            p.setString(1, routeModel.getName());
            p.setDouble(2, routeModel.getCoordinates().getX());
            p.setDouble(3, routeModel.getCoordinates().getY());
            p.setTimestamp(4, new Timestamp(new Date().getTime()));
            p.setLong(5, fromId);
            p.setLong(6, toId);
            p.setDouble(7, routeModel.getDistance());
            p.setString(8, user.getUsername());

            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления маршрута", e);
        }
    }
    public static boolean updateRouteDistance(long id, double newDistance, User user) {
        String query = "UPDATE routes SET distance = ? WHERE id = ? AND creatorid = (SELECT id FROM users WHERE username = ?)";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setDouble(1, newDistance);
            p.setLong(2, id);
            p.setString(3, user.getUsername());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления расстояния", e);
        }
    }
    public static boolean insertRoute(long key, RouteModel routeModel, User user) {
        String query = "INSERT INTO routes (id, name, x, y, creationdate, from_id, to_id, distance, creatorid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?))";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            long fromId = saveLocationFrom(routeModel.getFrom());
            long toId = saveLocationTo(routeModel.getTo());

            p.setLong(1, key);
            p.setString(2, routeModel.getName());
            p.setDouble(3, routeModel.getCoordinates().getX());
            p.setDouble(4, routeModel.getCoordinates().getY());
            p.setTimestamp(5, new Timestamp(new Date().getTime()));
            p.setLong(6, fromId);
            p.setLong(7, toId);
            p.setDouble(8, routeModel.getDistance());
            p.setString(9, user.getUsername());

            p.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки маршрута", e);
        }
    }

    public static boolean updateRoute(long id, RouteModel routeModel, User user) {
        String query = "UPDATE routes SET name = ?, x = ?, y = ?, from_id = ?, to_id = ?, distance = ? WHERE id = ? AND creatorid = (SELECT id FROM users WHERE username = ?)";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            long fromId = saveLocationFrom(routeModel.getFrom());
            long toId = saveLocationTo(routeModel.getTo());

            p.setString(1, routeModel.getName());
            p.setDouble(2, routeModel.getCoordinates().getX());
            p.setDouble(3, routeModel.getCoordinates().getY());
            p.setLong(4, fromId);
            p.setLong(5, toId);
            p.setDouble(6, routeModel.getDistance());
            p.setLong(7, id);
            p.setString(8, user.getUsername());

            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления", e);
        }
    }

    public static boolean removeRoute(long id, User user) {
        String query = "DELETE FROM routes WHERE id = ? "
                + "AND creatorid = (SELECT id FROM users WHERE username = ?)";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, id);
            p.setString(2, user.getUsername());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления", e);
        }
    }
    private static long saveLocationTo(LocationTo location) throws SQLException {
        String query = "INSERT INTO locations (x, y, z, name) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setDouble(1, location.getX());
            p.setDouble(2, location.getY());
            p.setInt(3, location.getZ());
            p.setString(4, location.getName());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Не удалось сохранить локацию");
        }
    }
    private static long saveLocationFrom(LocationFrom location) throws SQLException {
        String query = "INSERT INTO locations (x, y, z, name) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setDouble(1, location.getX());
            p.setDouble(2, location.getY());
            p.setInt(3, location.getZ());
            p.setString(4, location.getName());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Не удалось сохранить локацию");
        }
    }

    public static boolean removeRoutesLowerThanId(long startId, User user) {
        String query = "DELETE FROM routes WHERE id < ? "
                + "AND creatorid = (SELECT id FROM users WHERE username = ?)";

        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, startId);
            p.setString(2, user.getUsername());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления", e);
        }
    }

    public static boolean clearRoutes(User user) {
        String query = "DELETE FROM routes WHERE creatorid = (SELECT id FROM users WHERE username = ?)";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getUsername());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки", e);
        }
    }

    public static long loadElCount() {
        String query = "SELECT last_value FROM routes_id_seq";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            ResultSet res = p.executeQuery();
            return res.next() ? res.getLong(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки счётчика", e);
        }
    }
}