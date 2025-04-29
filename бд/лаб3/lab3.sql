
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
    gender ENUM('Мужской', 'Женский') NOT NULL,
    current_location VARCHAR(100)
);

CREATE TABLE memory_storage (
    storage_id INT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE diaspar (
    diaspar_id INT PRIMARY KEY,
    culture_city_name VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    parent_diaspar_id INT,
    FOREIGN KEY (parent_diaspar_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE diaspar_person (
    id INT PRIMARY KEY,
    diaspar_id INT NOT NULL,
    person_id INT NOT NULL,
    FOREIGN KEY (diaspar_id) REFERENCES diaspar(diaspar_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id)
);

CREATE TABLE forest_inhabitants (
    inhabitant_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location VARCHAR(100) DEFAULT 'Граница света и тьмы'
);

CREATE TABLE night (
    night_id INT PRIMARY KEY,
    time_of_day TIME NOT NULL,
    stars_present BOOLEAN DEFAULT FALSE,
    total_darkness BOOLEAN DEFAULT TRUE
);

CREATE TABLE mystery (
    mystery_id INT PRIMARY KEY,
    description TEXT NOT NULL,
    related_events TEXT,
    unsolved BOOLEAN DEFAULT TRUE,
    person_id INT,
    diaspar_id INT,
    FOREIGN KEY (person_id) REFERENCES person(person_id),
    FOREIGN KEY (diaspar_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE storage_magic (
    magic_id INT PRIMARY KEY,
    property VARCHAR(100) DEFAULT 'Защита от Времени',
    diaspar_id INT NOT NULL,
    FOREIGN KEY (diaspar_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE odvin_satisfaction (
    satisfaction_id INT PRIMARY KEY,
    emotional_state VARCHAR(50) NOT NULL,
    reason VARCHAR(100),
    odvin_id INT NOT NULL,
    FOREIGN KEY (odvin_id) REFERENCES person(person_id)
);

CREATE TABLE cultural_differences (
    difference_id INT PRIMARY KEY,
    characteristic TEXT NOT NULL,
    odvin_culture_id INT NOT NULL,
    hidvar_culture_id INT NOT NULL,
    FOREIGN KEY (odvin_culture_id) REFERENCES diaspar(diaspar_id),
    FOREIGN KEY (hidvar_culture_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE dinner (
    dinner_id INT PRIMARY KEY,
    time TIMESTAMP NOT NULL,
    odvin_id INT NOT NULL,
    hidvar_id INT NOT NULL,
    FOREIGN KEY (odvin_id) REFERENCES person(person_id),
    FOREIGN KEY (hidvar_id) REFERENCES person(person_id)
);

CREATE TABLE shadow_observation (
    observation_id INT PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    inhabitant_id INT NOT NULL,
    odvin_id INT NOT NULL,
    night_id INT,
    FOREIGN KEY (inhabitant_id) REFERENCES forest_inhabitants(inhabitant_id),
    FOREIGN KEY (odvin_id) REFERENCES person(person_id),
    FOREIGN KEY (night_id) REFERENCES night(night_id)
);

CREATE TABLE communication (
    communication_id INT PRIMARY KEY,
    interpretation TEXT NOT NULL,
    odvin_id INT NOT NULL,
    hidvar_id INT NOT NULL,
    FOREIGN KEY (odvin_id) REFERENCES person(person_id),
    FOREIGN KEY (hidvar_id) REFERENCES person(person_id)
);

CREATE TABLE cultural_affiliation (
    affiliation_id INT PRIMARY KEY,
    odvin_id INT NOT NULL,
    hidvar_id INT NOT NULL,
    culture_id INT NOT NULL,
    FOREIGN KEY (odvin_id) REFERENCES person(person_id),
    FOREIGN KEY (hidvar_id) REFERENCES person(person_id),
    FOREIGN KEY (culture_id) REFERENCES diaspar(diaspar_id)
);

CREATE TABLE storage_influence (
    influence_id INT PRIMARY KEY,
    storage_id INT NOT NULL,
    diaspar_id INT NOT NULL,
    FOREIGN KEY (storage_id) REFERENCES memory_storage(storage_id),
    FOREIGN KEY (diaspar_id) REFERENCES diaspar(diaspar_id)
);


DELIMITER //

-- Проверка иерархии городов
CREATE TRIGGER trg_diaspar_hierarchy 
BEFORE INSERT ON diaspar
FOR EACH ROW
BEGIN
    IF NEW.parent_diaspar_id IS NOT NULL THEN
        IF NOT EXISTS (
            SELECT 1 FROM diaspar 
            WHERE diaspar_id = NEW.parent_diaspar_id
        ) THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Родительский город не существует';
        END IF;
        
        IF NEW.parent_diaspar_id = NEW.diaspar_id THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Город не может быть родителем сам себе';
        END IF;
    END IF;
END//

-- Автоматическое обновление локации персонажа 
CREATE TRIGGER trg_person_location 
AFTER INSERT ON shadow_observation
FOR EACH ROW
BEGIN
    UPDATE person
    SET current_location = 'Зона наблюдения'
    WHERE person_id = NEW.odvin_id; 
END//

-- Валидация эмоционального состояния
CREATE TRIGGER trg_emotion_validation 
BEFORE UPDATE ON odvin_satisfaction
FOR EACH ROW
BEGIN
    IF NEW.emotional_state = OLD.emotional_state THEN
        SIGNAL SQLSTATE '01000'
        SET MESSAGE_TEXT = 'Состояние не изменилось';
    END IF;
    
    IF NEW.emotional_state = 'Радость' AND NEW.reason IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Требуется указать причину радости';
    END IF;
END//

-- Проверка времени наблюдения
CREATE TRIGGER trg_shadow_time_check 
BEFORE INSERT ON shadow_observation
FOR EACH ROW
BEGIN
    IF NEW.end_time IS NOT NULL AND NEW.end_time <= NEW.start_time THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'End time must be after start time';
    END IF;
END//

DELIMITER ;

-- Вставка тестовых данных
INSERT INTO person (person_id, name, gender) VALUES
(1, 'Одвин', 'Мужской'),
(2, 'Хидвар', 'Мужской');

INSERT INTO diaspar (diaspar_id, culture_city_name) VALUES
(1, 'Диаспар'),
(2, 'Лис');

-- Тестовые сценарии

--вызовет ошибку из-за ENUM
INSERT INTO person (person_id, name, gender) VALUES
(3, 'Тест', 'Неизвестно'); 

-- ошибка
UPDATE diaspar SET parent_diaspar_id = 1 WHERE diaspar_id = 1; 

-- ошибка
INSERT INTO shadow_observation (observation_id, start_time, end_time, inhabitant_id, odvin_id) VALUES
(1, '2024-01-01 20:00:00', '2024-01-01 19:00:00', 1, 1); 

--шибка
INSERT INTO odvin_satisfaction (satisfaction_id, emotional_state, odvin_id) VALUES
(1, 'Радость', 1); 
