-- Улучшенная версия с унифицированным стилем и форматом
CREATE TABLE person (
    person_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
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