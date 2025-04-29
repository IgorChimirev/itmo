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
    current_location VARCHAR(100)
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

CREATE TABLE shadow_observation (
    observation_id INT PRIMARY KEY,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    CONSTRAINT chk_time CHECK (end_time IS NULL OR end_time > start_time)
);

-- Триггеры
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
    WHERE person_id IN (
        SELECT odvin_id 
        FROM shadow_observation 
        WHERE observation_id = NEW.observation_id
    );
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

DELIMITER ;

-- Вставка тестовых данных
INSERT INTO person (person_id, name, gender) VALUES
(1, 'Одвин', 'Мужской'),
(2, 'Хидвар', 'Мужской');

INSERT INTO diaspar (diaspar_id, culture_city_name) VALUES
(1, 'Диаспар'),
(2, 'Лис');

-- Тестовые сценарии

-- должно вызвать ошибку
INSERT INTO person (person_id, name, gender) VALUES
(3, 'Тест', 'Неизвестно');

-- должно вызвать ошибку
UPDATE diaspar SET parent_diaspar_id = 1 WHERE diaspar_id = 1;

-- должно вызвать ошибку
INSERT INTO shadow_observation (observation_id, start_time, end_time) VALUES
(1, '2024-01-01 20:00:00', '2024-01-01 19:00:00');

-- должно вызвать ошибку
INSERT INTO odvin_satisfaction (satisfaction_id, emotional_state, odvin_id) VALUES
(1, 'Радость', 1);

Описание триггеров:

trg_diaspar_hierarchy
Контролирует целостность иерархии городов:

Запрещает ссылаться на несуществующий родительский город

Блокирует создание циклических связей (город → сам себя)

trg_person_location
Автоматически обновляет локацию персонажа при начале наблюдения:

При создании записи наблюдения перемещает персонажа в зону наблюдения

trg_emotion_validation
Обеспечивает корректность данных о эмоциях:

Требует изменения состояния при обновлении

Обязательное указание причины для состояния "Радость"
