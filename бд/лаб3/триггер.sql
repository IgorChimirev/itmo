-- Активация PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Домены
CREATE DOMAIN name_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z- ]{2,49}$');

CREATE DOMAIN title_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z0-9- ]{4,49}$');

CREATE DOMAIN location_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z0-9- ]{4,99}$');

-- Перечисления (ENUM)
CREATE TYPE gender_type AS ENUM ('Male', 'Female', 'Other');
CREATE TYPE transcript_category AS ENUM (
    'Greeting', 'Question', 'Answer', 'Statement', 
    'Request', 'Clarification', 'Farewell'
);
CREATE TYPE communication_status AS ENUM (
    'Pending', 'Active', 'Completed', 'Interrupted'
);
CREATE TYPE magic_property AS ENUM (
    'Time Protection', 'Memory Preservation', 'Cultural Integrity'
);
CREATE TYPE habitat_category AS ENUM (
    'Light Border', 'Twilight Zone', 'Dark Realm'
);
CREATE TYPE interaction_method AS ENUM (
    'Telepathy', 'Verbal', 'Symbolic'
);
CREATE TYPE interaction_category AS ENUM (
    'Dinner', 'Conference', 'Joint Research'
);
CREATE TYPE mystery_location_type AS ENUM (
    'Main Archive', 'Central Square', 'Memory Vault', 'Forest Perimeter'
);
CREATE TYPE behavior_action_type AS ENUM (
    'Observing', 'Mimicking', 'Hiding', 'Communicating'
);

-- Таблицы
CREATE TABLE city (
    city_name location_domain PRIMARY KEY,
    country location_domain NOT NULL,
    quarantine_start_date DATE,
    abandoned BOOLEAN DEFAULT FALSE
);

CREATE TABLE person (
    person_id INT PRIMARY KEY,
    first_name name_domain NOT NULL,
    last_name name_domain NOT NULL,
    gender gender_type NOT NULL,
    birth_date DATE CHECK (
        birth_date > '1900-01-01' 
        AND birth_date < CURRENT_DATE
    ),
    city_name location_domain REFERENCES city(city_name),
    infection_date DATE
);

CREATE TABLE memory_storage (
    storage_id INT PRIMARY KEY,
    title title_domain NOT NULL UNIQUE,
    coordinates geography(Point) NOT NULL,
    construction_year INT NOT NULL 
        CHECK (construction_year BETWEEN 1000 AND EXTRACT(YEAR FROM CURRENT_DATE))
);

CREATE TABLE diaspar (
    diaspar_id INT PRIMARY KEY,
    culture_name title_domain NOT NULL UNIQUE,
    founding_year INT NOT NULL CHECK (founding_year > 0),
    parent_diaspar_id INT REFERENCES diaspar(diaspar_id),
    CONSTRAINT hierarchy_integrity CHECK (diaspar_id != parent_diaspar_id)
);

CREATE TABLE forest_inhabitants (
    inhabitant_id INT PRIMARY KEY,
    species_name title_domain NOT NULL UNIQUE,
    habitat habitat_category NOT NULL,
    first_contact_date DATE NOT NULL 
        CHECK (first_contact_date > '1900-01-01' AND first_contact_date <= CURRENT_DATE),
    aggression_level INT DEFAULT 1
);

CREATE TABLE mystery (
    mystery_id INT PRIMARY KEY,
    mystery_subject title_domain NOT NULL,
    mystery_action title_domain NOT NULL,
    mystery_object title_domain,
    mystery_location mystery_location_type NOT NULL,
    discovery_date DATE NOT NULL 
        CHECK (discovery_date > '2000-01-01' AND discovery_date <= CURRENT_DATE),
    is_unsolved BOOLEAN NOT NULL DEFAULT TRUE,
    reporter_id INT NOT NULL REFERENCES person(person_id),
    diaspar_id INT NOT NULL REFERENCES diaspar(diaspar_id)
);

CREATE TABLE shadow_observation (
    observation_id INT PRIMARY KEY,
    observer_id INT NOT NULL REFERENCES person(person_id),
    inhabitant_id INT NOT NULL REFERENCES forest_inhabitants(inhabitant_id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP CHECK (end_time > start_time AND end_time <= CURRENT_TIMESTAMP),
    behavior_subject title_domain NOT NULL,
    behavior_action behavior_action_type NOT NULL,
    behavior_location location_domain NOT NULL
);

CREATE TABLE cultural_interaction (
    interaction_id INT PRIMARY KEY,
    interaction_date DATE NOT NULL 
        CHECK (interaction_date > '1900-01-01' AND interaction_date <= CURRENT_DATE),
    odvin_culture_id INT NOT NULL REFERENCES diaspar(diaspar_id),
    hidvar_culture_id INT NOT NULL REFERENCES diaspar(diaspar_id),
    interaction_type interaction_category NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    CONSTRAINT culture_check CHECK (odvin_culture_id != hidvar_culture_id)
);

CREATE TABLE diaspar_membership (
    diaspar_id INT NOT NULL REFERENCES diaspar(diaspar_id),
    person_id INT NOT NULL REFERENCES person(person_id),
    membership_start DATE NOT NULL 
        CHECK (membership_start > '1900-01-01' AND membership_start <= CURRENT_DATE),
    membership_end DATE 
        CHECK (membership_end > membership_start AND membership_end <= CURRENT_DATE),
    PRIMARY KEY (diaspar_id, person_id)
);

CREATE TABLE storage_magic (
    storage_id INT PRIMARY KEY REFERENCES memory_storage(storage_id),
    magic_type magic_property NOT NULL DEFAULT 'Time Protection',
    activation_date DATE NOT NULL 
        CHECK (activation_date > '1900-01-01' AND activation_date <= CURRENT_DATE),
    deactivation_date DATE 
        CHECK (deactivation_date > activation_date AND deactivation_date <= CURRENT_DATE)
);

CREATE TABLE intercultural_communication (
    communication_id INT PRIMARY KEY,
    communication_date DATE NOT NULL 
        CHECK (communication_date > '1900-01-01' AND communication_date <= CURRENT_DATE),
    initiator_id INT NOT NULL REFERENCES person(person_id),
    receiver_id INT NOT NULL REFERENCES person(person_id),
    method interaction_method NOT NULL,
    transcript transcript_category NOT NULL,
    community communication_status NOT NULL,
    CONSTRAINT participant_check CHECK (initiator_id != receiver_id)
);

-- Дополнительные таблицы для эпидемии
CREATE TABLE epidemic_mutation (
    mutation_id SERIAL PRIMARY KEY,
    person_id INT REFERENCES person(person_id),
    city_name location_domain REFERENCES city(city_name),
    mutation_date DATE DEFAULT CURRENT_DATE,
    description TEXT
);

-- Функции и триггеры
CREATE OR REPLACE FUNCTION handle_epidemic()
RETURNS TRIGGER AS $$
DECLARE
    current_city location_domain;
    infected_count INT;
    total_population INT;
BEGIN
    IF NEW.infection_date IS NOT NULL AND OLD.infection_date IS NULL THEN
        -- Мутация лесных обитателей
        UPDATE forest_inhabitants 
        SET species_name = species_name || ' (Mutated)',
            aggression_level = aggression_level * 2
        WHERE inhabitant_id IN (
            SELECT inhabitant_id 
            FROM shadow_observation 
            WHERE observer_id = NEW.person_id
        );

        -- Проверка условий для города
        current_city := NEW.city_name;
        SELECT COUNT(*) INTO infected_count 
        FROM person 
        WHERE city_name = current_city 
        AND infection_date IS NOT NULL;

        SELECT COUNT(*) INTO total_population 
        FROM person 
        WHERE city_name = current_city;

        INSERT INTO epidemic_mutation (person_id, city_name, description)
        VALUES (NEW.person_id, current_city, 'Initial infection detected');

        -- Активация карантина
        IF infected_count >= 5 THEN
            UPDATE city 
            SET quarantine_start_date = CURRENT_DATE 
            WHERE city_name = current_city;
        END IF;

        -- Заброшенность города
        IF (infected_count::FLOAT / total_population) > 0.5 THEN
            UPDATE city 
            SET abandoned = TRUE 
            WHERE city_name = current_city;

            UPDATE cultural_interaction 
            SET status = 'Cancelled'
            WHERE odvin_culture_id IN (
                SELECT diaspar_id 
                FROM diaspar 
                WHERE culture_name = current_city
            ) OR hidvar_culture_id IN (
                SELECT diaspar_id 
                FROM diaspar 
                WHERE culture_name = current_city
            );
        END IF;
    END IF;

    -- Снятие карантина
    IF TG_TABLE_NAME = 'city' AND NEW.quarantine_start_date IS NOT NULL THEN
        IF (CURRENT_DATE - NEW.quarantine_start_date) >= 30 THEN
            UPDATE person 
            SET infection_date = NULL 
            WHERE city_name = NEW.city_name;

            UPDATE city 
            SET quarantine_start_date = NULL 
            WHERE city_name = NEW.city_name;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггеры
CREATE TRIGGER person_infection_trigger
AFTER UPDATE OF infection_date ON person
FOR EACH ROW
EXECUTE FUNCTION handle_epidemic();

CREATE TRIGGER city_quarantine_check
AFTER UPDATE ON city
FOR EACH ROW
WHEN (NEW.quarantine_start_date IS NOT NULL)
EXECUTE FUNCTION handle_epidemic();