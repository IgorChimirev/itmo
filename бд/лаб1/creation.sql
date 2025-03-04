
CREATE TABLE Person (
    Person_ID INT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Gender VARCHAR(10),
    Current_Location VARCHAR(100)
);

CREATE TABLE Memory_Storage (
    Storage_ID INT PRIMARY KEY,
    Title VARCHAR(50) NOT NULL,
    Location VARCHAR(100)
);

CREATE TABLE Diaspar (
    Diaspar_ID INT PRIMARY KEY,
    Culture_City_Name VARCHAR(50) NOT NULL,
    Location VARCHAR(100)
);

CREATE TABLE Forest_Inhabitants (
    Inhabitant_ID INT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Location VARCHAR(100) DEFAULT 'Граница света и тьмы' 
);

CREATE TABLE Night (
    Night_ID INT PRIMARY KEY,
    Time_of_Day TIME NOT NULL,
    Stars_Present BOOLEAN DEFAULT FALSE,
    Total_Darkness BOOLEAN DEFAULT TRUE
);

CREATE TABLE Mystery (
    Mystery_ID INT PRIMARY KEY,
    Description TEXT NOT NULL,
    Related_Events TEXT,
    Unsolved BOOLEAN DEFAULT TRUE,
    Person_ID INT,
    Diaspar_ID INT,
    FOREIGN KEY (Person_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Diaspar_ID) REFERENCES Diaspar(Diaspar_ID)
);

CREATE TABLE Storage_Magic (
    Magic_ID INT PRIMARY KEY,
    Property VARCHAR(100) DEFAULT 'Защита от Времени', 
    Diaspar_ID INT NOT NULL,
    FOREIGN KEY (Diaspar_ID) REFERENCES Diaspar(Diaspar_ID)
);

CREATE TABLE Odvin_Satisfaction (
    Satisfaction_ID INT PRIMARY KEY,
    Emotional_State VARCHAR(50) NOT NULL,
    Reason VARCHAR(100),
    Odvin_ID INT NOT NULL,
    FOREIGN KEY (Odvin_ID) REFERENCES Person(Person_ID)
);

CREATE TABLE Cultural_Differences (
    Difference_ID INT PRIMARY KEY,
    Characteristic TEXT NOT NULL,
    Odvin_Culture_ID INT NOT NULL,
    Hidvar_Culture_ID INT NOT NULL,
    FOREIGN KEY (Odvin_Culture_ID) REFERENCES Diaspar(Diaspar_ID),
    FOREIGN KEY (Hidvar_Culture_ID) REFERENCES Diaspar(Diaspar_ID)
);



CREATE TABLE Dinner (
    Dinner_ID INT PRIMARY KEY,
    Time TIMESTAMP NOT NULL, 
    Odvin_ID INT NOT NULL,
    Hidvar_ID INT NOT NULL,
    FOREIGN KEY (Odvin_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Hidvar_ID) REFERENCES Person(Person_ID)
);

CREATE TABLE Shadow_Observation (
    Observation_ID INT PRIMARY KEY,
    Start_Time TIMESTAMP NOT NULL, 
    End_Time TIMESTAMP,            
    Inhabitant_ID INT NOT NULL,
    Odvin_ID INT NOT NULL,
    Night_ID INT, 
    FOREIGN KEY (Inhabitant_ID) REFERENCES Forest_Inhabitants(Inhabitant_ID),
    FOREIGN KEY (Odvin_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Night_ID) REFERENCES Night(Night_ID)
);

CREATE TABLE Communication (
    Communication_ID INT PRIMARY KEY,
    Interpretation TEXT NOT NULL,
    Odvin_ID INT NOT NULL,
    Hidvar_ID INT NOT NULL,
    FOREIGN KEY (Odvin_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Hidvar_ID) REFERENCES Person(Person_ID)
);

CREATE TABLE Cultural_Affiliation (
    Affiliation_ID INT PRIMARY KEY,
    Odvin_ID INT NOT NULL,
    Hidvar_ID INT NOT NULL,
    Culture_ID INT NOT NULL,
    FOREIGN KEY (Odvin_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Hidvar_ID) REFERENCES Person(Person_ID),
    FOREIGN KEY (Culture_ID) REFERENCES Diaspar(Diaspar_ID)
);

CREATE TABLE Storage_Influence (
    Influence_ID INT PRIMARY KEY,
    Storage_ID INT NOT NULL,
    Diaspar_ID INT NOT NULL,
    FOREIGN KEY (Storage_ID) REFERENCES Memory_Storage(Storage_ID),
    FOREIGN KEY (Diaspar_ID) REFERENCES Diaspar(Diaspar_ID)
);