CREATE TABLE client
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(50) NOT NULL,
    salary   DECIMAL(15, 2),
    birthday datetime,
    active   BOOLEAN
);

INSERT
INTO client (name, salary, birthday, active)
VALUES ('João', 3500.00, '1990-05-12', true),
       ('Maria', 4200.50, '1985-11-23', true),
       ('Costa', 100.00, '1992-03-08', true),
       ('Ana', 5100.00, '1988-07-19', true),
       ('Pedro Alves', 3900.00, '1995-01-30', false);