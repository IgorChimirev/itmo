package Network;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private static final long serialVersionUID = 1L;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
