
CREATE EXTENSION IF NOT EXISTS postgis;


CREATE DOMAIN name_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z- ]{2,49}$');

CREATE DOMAIN title_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z0-9- ]{4,49}$');

CREATE DOMAIN location_domain AS TEXT 
CHECK (VALUE ~ '^[A-Z][a-zA-Z0-9- ]{4,99}$');


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


CREATE TABLE city (
    city_name location_domain PRIMARY KEY,
    country location_domain NOT NULL
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
    city_name location_domain REFERENCES city(city_name)
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
        CHECK (first_contact_date > '1900-01-01' AND first_contact_date <= CURRENT_DATE)
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
