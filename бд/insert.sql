
INSERT INTO Person (Person_ID, Name, Gender, Current_Location) 
VALUES
(1, 'Одвин', 'Мужской', 'Центральная площадь'),
(2, 'Хидвар', 'Небинарный', 'где-нибудь'),
(3, 'Аланис', 'Женский', 'Лес');

INSERT INTO Memory_Storage (Storage_ID, Title, Location) 
VALUES
(101, 'Вечный архив', 'Подземелье'),
(102, 'Хранилище времени', 'Башня созвездий');

INSERT INTO Diaspar (Diaspar_ID, Culture_City_Name, Location, Parent_Diaspar_ID)
VALUES (1, 'Central City', 'V', NULL);

INSERT INTO Diaspar (Diaspar_ID, Culture_City_Name, Location, Parent_Diaspar_ID)
VALUES (2, 'O', 'F121', 1);

INSERT INTO Diaspar_Person (ID, Diaspar_ID, Person_ID)
VALUES (1, 1, 101);

INSERT INTO Diaspar_Person (ID, Diaspar_ID, Person_ID)
VALUES (2, 2, 102);


INSERT INTO Forest_Inhabitants (Inhabitant_ID, Name) 
VALUES
(301, 'сталкер'),
(302, 'дуб'); 

INSERT INTO Night (Night_ID, Time_of_Day, Stars_Present) 
VALUES
(401, '20:00:00', TRUE),
(402, '22:30:00', FALSE);


INSERT INTO Mystery (Mystery_ID, Description, Person_ID, Diaspar_ID) 
VALUES
(501, 'Исчезновение людей', 1, 201),
(502, 'Феномен озера', 3, 202);

INSERT INTO Storage_Magic (Magic_ID, Diaspar_ID) 
VALUES
(601, 201),
(602, 202); 

INSERT INTO Odvin_Satisfaction (Satisfaction_ID, Emotional_State, Odvin_ID) 
VALUES
(701, 'Любопытство', 1),
(702, 'Спокойствие', 1);

INSERT INTO Cultural_Differences (Difference_ID, Characteristic, Odvin_Culture_ID, Hidvar_Culture_ID) 
VALUES
(801, 'Отношение ко времени', 201, 202),
(802, 'Ритуалы', 202, 201);


INSERT INTO Dinner (Dinner_ID, Time, Odvin_ID, Hidvar_ID) 
VALUES
(901, '2023-10-15 19:30:00', 1, 2);

INSERT INTO Shadow_Observation (Observation_ID, Start_Time, Inhabitant_ID, Odvin_ID, Night_ID) 
VALUES
(1001, '2023-10-15 21:00:00', 301, 1, 401),
(1002, '2023-10-15 23:00:00', 302, 3, 402);

INSERT INTO Communication (Communication_ID, Interpretation, Odvin_ID, Hidvar_ID) 
VALUES
(1101, 'Спор о природе', 1, 2);

INSERT INTO Cultural_Affiliation (Affiliation_ID, Odvin_ID, Hidvar_ID, Culture_ID) 
VALUES
(1201, 1, 2, 201);

INSERT INTO Storage_Influence (Influence_ID, Storage_ID, Diaspar_ID) 
VALUES
(1301, 101, 201),
(1302, 102, 202);