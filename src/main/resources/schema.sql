CREATE TABLE IF NOT EXISTS MEASUREMENT
(
    measurement_key VARCHAR(200) PRIMARY KEY,
    name        VARCHAR(200)
    location        VARCHAR(200)
);


CREATE TABLE IF NOT EXISTS TEMPERATURE_MEASUREMENT_POINT
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    measurement_key VARCHAR(200),
    timestamp       datetime,
    temperature  INT,
    state VARCHAR(20),
    CONSTRAINT FOREIGN_KEY_MEASUREMENT FOREIGN KEY (measurement_key) references MEASUREMENT on delete cascade,
);
