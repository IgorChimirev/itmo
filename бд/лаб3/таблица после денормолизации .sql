
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS 
    storage_influence,
    cultural_affiliation,
    communication,
    shadow_observation,
    dinner,
    cultural_differences,
    odvin_satisfaction,
    storage_magic,
    mystery,
    night,
    forest_inhabitants,
    diaspar_person,
    diaspar,
    memory_storage,
    person;
SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE person (
    person_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('Мужской', 'Женский')),
    current_location VARCHAR(100),
    current_culture VARCHAR(50)
);

CREATE TABLE shadow_observation (
    observation_id INT PRIMARY KEY,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    night_time VARCHAR(50),
    inhabitant_location VARCHAR(100),
    CONSTRAINT chk_time CHECK (end_time IS NULL OR end_time > start_time)
);

CREATE TABLE dinner (
    dinner_id INT PRIMARY KEY,
    time DATETIME,
    odvin_id INT,
    hidvar_id INT,
    odvin_name VARCHAR(50),
    hidvar_name VARCHAR(50),
    FOREIGN KEY (odvin_id) REFERENCES person(person_id),
    FOREIGN KEY (hidvar_id) REFERENCES person(person_id)
);

CREATE TABLE diaspar (
    diaspar_id INT PRIMARY KEY,
    culture_city_name VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    parent_diaspar_id INT,
    FOREIGN KEY (parent_diaspar_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE odvin_satisfaction (
    satisfaction_id INT PRIMARY KEY,
    emotional_state VARCHAR(50) NOT NULL CHECK (emotional_state IN 
        ('Спокойствие', 'Радость', 'Грусть', 'Удовлетворение')),
    reason VARCHAR(100),
    odvin_id INT NOT NULL,
    FOREIGN KEY (odvin_id) REFERENCES person(person_id)
);

-- Триггеры
DELIMITER //
CREATE TRIGGER fill_dinner_names 
BEFORE INSERT ON dinner
FOR EACH ROW
BEGIN
    -- Проверка существования ID
    IF NOT EXISTS (SELECT 1 FROM person WHERE person_id = NEW.odvin_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Odvin_id не существует';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM person WHERE person_id = NEW.hidvar_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hidvar_id не существует';
    END IF;
    -- Заполнение имен
    SET NEW.odvin_name = (SELECT name FROM person WHERE person_id = NEW.odvin_id);
    SET NEW.hidvar_name = (SELECT name FROM person WHERE person_id = NEW.hidvar_id);
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER check_observation_time 
BEFORE INSERT ON shadow_observation
FOR EACH ROW
BEGIN
    IF NEW.end_time IS NOT NULL AND NEW.end_time <= NEW.start_time THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Время окончания должно быть позже начала';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER validate_person_culture 
BEFORE INSERT OR UPDATE ON person  
FOR EACH ROW
BEGIN
    IF NEW.current_culture NOT IN (SELECT culture_city_name FROM diaspar) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Культура не существует в таблице Diaspar';
    END IF;
END//
DELIMITER ;
1. Триггер fill_dinner_names
При попытке добавить новую запись в таблицу ужинов (dinner)

Проверяет существование персонажей с odvin_id и hidvar_id в таблице person

Если ID не существуют → вызывает ошибку "Odvin_id/Hidvar_id не существует"

Если ID валидны → автоматически подставляет имена участников из таблицы person в поля odvin_name и hidvar_name

2. Триггер check_observation_time
При создании записи о наблюдении в shadow_observation

Проверяет, что время окончания наблюдения (end_time) не раньше времени начала (start_time)

При нарушении временной логики → вызывает ошибку "Время окончания должно быть позже начала"
3 Триггер validate_person_culture
При создании или изменении персонажа в person

Проверяет, что указанная культура (current_culture) существует в таблице городов-культур diaspar

При попытке указать несуществующую культуру → вызывает ошибку "Культура не существует в таблице Diaspar"
