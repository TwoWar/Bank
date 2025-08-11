CREATE SEQUENCE IF NOT EXISTS outbox_operation_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE outbox_operation
(
    id           BIGINT  NOT NULL,
    user_id      BIGINT,
    id_operation BIGINT,
    money        INTEGER NOT NULL,
    date         TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_outbox_operation PRIMARY KEY (id)
);