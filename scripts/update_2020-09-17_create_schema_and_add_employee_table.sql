CREATE SCHEMA crud;

CREATE TABLE crud.employees (
    id  SERIAL PRIMARY KEY,
    first_name  VARCHAR (255) NOT NULL,
    last_name   VARCHAR (255) NOT NULL,
    middle_name VARCHAR (255) NOT NULL,
    date_of_birth   TIMESTAMP NOT NULL,
    date_added  TIMESTAMP NOT NULL,
    date_updated    TIMESTAMP NOT NULL,
    account_number INTEGER NOT NULL,
    is_deleted    BOOLEAN DEFAULT FALSE
);

INSERT INTO crud.employees(first_name, last_name, middle_name, date_of_birth, date_added, date_updated, account_number) VALUES
('first1', 'second1', 'middle1', '1995.05.05', now(), now(), 1),
('first2', 'second2', 'middle2', '1995.05.05', now(), now(), 2);