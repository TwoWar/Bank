
CREATE TABLE card
(
    id           BIGINT NOT NULL,
    number       VARCHAR(255),
    name_surname VARCHAR(255),
    cvv          VARCHAR(255),
    name_bank    VARCHAR(255),
    money        INTEGER,
    CONSTRAINT pk_card PRIMARY KEY (id)
);