-- Удаление таблиц с учетом зависимостей
DROP TABLE IF EXISTS Storage_Influence CASCADE;
DROP TABLE IF EXISTS Cultural_Affiliation CASCADE;
DROP TABLE IF EXISTS Communication CASCADE;
DROP TABLE IF EXISTS Shadow_Observation CASCADE;
DROP TABLE IF EXISTS Dinner CASCADE;
DROP TABLE IF EXISTS Cultural_Differences CASCADE;
DROP TABLE IF EXISTS Odvin_Satisfaction CASCADE;
DROP TABLE IF EXISTS Storage_Magic CASCADE;
DROP TABLE IF EXISTS Mystery CASCADE;
DROP TABLE IF EXISTS Diaspar_Person CASCADE;
DROP TABLE IF EXISTS Forest_Inhabitants CASCADE;
DROP TABLE IF EXISTS Night CASCADE;
DROP TABLE IF EXISTS Memory_Storage CASCADE;
DROP TABLE IF EXISTS Diaspar CASCADE;
DROP TABLE IF EXISTS Person CASCADE;

-- Создание основных таблиц
CREATE TABLE Person (
    Person_ID SERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Gender VARCHAR(10),
    Current_Location VARCHAR(100)
);

CREATE TABLE Diaspar (
    Diaspar_ID SERIAL PRIMARY KEY,
    Culture_City_Name VARCHAR(50) NOT NULL,
    Location VARCHAR(100),
    Parent_Diaspar_ID INT REFERENCES Diaspar(Diaspar_ID)
);

CREATE TABLE Forest_Inhabitants (
    Inhabitant_ID SERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Location VARCHAR(100) DEFAULT 'Граница света и тьмы'
);

CREATE TABLE Night (
    Night_ID SERIAL PRIMARY KEY,
    Time_of_Day TIME NOT NULL,
    Stars_Present BOOLEAN DEFAULT FALSE,
    Total_Darkness BOOLEAN DEFAULT TRUE
);

CREATE TABLE Mystery (
    Mystery_ID SERIAL PRIMARY KEY,
    Description TEXT NOT NULL,
    Related_Events TEXT,
    Unsolved BOOLEAN DEFAULT TRUE,
    Person_ID INT REFERENCES Person(Person_ID),
    Diaspar_ID INT REFERENCES Diaspar(Diaspar_ID)
);

-- Создание связующих таблиц
CREATE TABLE Diaspar_Person (
    ID SERIAL PRIMARY KEY,
    Diaspar_ID INT NOT NULL REFERENCES Diaspar(Diaspar_ID),
    Person_ID INT NOT NULL REFERENCES Person(Person_ID)
);

-- Триггер 1: Установка Location по умолчанию для Forest_Inhabitants
CREATE OR REPLACE FUNCTION set_default_forest_location()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Location IS NULL THEN
        NEW.Location := 'Граница света и тьмы';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_forest_inhabitants_insert
BEFORE INSERT ON Forest_Inhabitants
FOR EACH ROW
EXECUTE FUNCTION set_default_forest_location();

-- Триггер 2: Проверка логики Stars_Present и Total_Darkness для Night
CREATE OR REPLACE FUNCTION validate_night_darkness()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Stars_Present = TRUE AND NEW.Total_Darkness = TRUE THEN
        RAISE EXCEPTION 'Невозможно одновременное наличие звезд и полной темноты';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_night_insert_update
BEFORE INSERT OR UPDATE ON Night
FOR EACH ROW
EXECUTE FUNCTION validate_night_darkness();

-- Триггер 3: Обновление статуса тайны (Mystery.Unsolved)
CREATE OR REPLACE FUNCTION update_mystery_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Related_Events ILIKE '%решено%' THEN
        NEW.Unsolved := FALSE;
    ELSIF NEW.Related_Events ILIKE '%неразгаданно%' THEN
        NEW.Unsolved := TRUE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_mystery_update
BEFORE UPDATE ON Mystery
FOR EACH ROW
EXECUTE FUNCTION update_mystery_status();

-- Триггер 4: Запрет удаления Diaspar с жителями
CREATE OR REPLACE FUNCTION prevent_diaspar_deletion()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Diaspar_Person WHERE Diaspar_ID = OLD.Diaspar_ID) THEN
        RAISE EXCEPTION 'Нельзя удалить Diaspar с привязанными жителями!';
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_diaspar_delete
BEFORE DELETE ON Diaspar
FOR EACH ROW
EXECUTE FUNCTION prevent_diaspar_deletion();

-- Пример заполнения данными
INSERT INTO Person (Name, Gender) VALUES 
('Одвин', 'M'),
('Хидвар', 'M'),
('Элис', 'F');

INSERT INTO Diaspar (Culture_City_Name, Location) VALUES
('Город Памяти', 'Координаты X:125 Y:340'),
('Забытый Анклав', 'Неизвестно');

INSERT INTO Forest_Inhabitants (Name) VALUES ('Древний Дух');

INSERT INTO Night (Time_of_Day, Stars_Present) VALUES ('23:00:00', TRUE);

INSERT INTO Mystery (Description, Related_Events, Person_ID) VALUES 
('Тайна исчезновения', 'Решено вчера', 1);

-- Проверка работы триггеров
-- 1. Вставка в Forest_Inhabitants без Location (автоматическое назначение)
-- 2. Попытка вставить Night с Stars_Present=TRUE и Total_Darkness=TRUE (ошибка)
-- 3. Обновление Mystery с ключевым словом "решено" (Unsolved станет FALSE)
-- 4. Попытка удалить Diaspar с жителями (ошибка)