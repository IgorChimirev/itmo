SELECT 
    "Н_УЧЕНИКИ"."ГРУППА" AS "Группа",
    AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, "Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ"))) AS "Средний возраст"
FROM 
    "Н_ЛЮДИ"
INNER JOIN 
    "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
GROUP BY 
    "Н_УЧЕНИКИ"."ГРУППА"
HAVING 
    AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, "Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ"))) < (
        SELECT 
            MAX(EXTRACT(YEAR FROM AGE(CURRENT_DATE, "Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ")))
        FROM 
            "Н_ЛЮДИ"
        INNER JOIN 
            "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
        WHERE 
            "Н_УЧЕНИКИ"."ГРУППА" = '1101'
    );