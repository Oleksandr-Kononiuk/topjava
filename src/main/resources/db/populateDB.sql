DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-01-30 10:00', 'Завтрак', 500),
       (100000, '2020-01-30 13:00', 'Обед', 1000),
       (100000, '2020-01-30 20:00', 'Ужин', 500),
       (100000, '2020-01-31 00:00', 'Еда на граничное значение', 100),
       (100000, '2020-01-31 10:00', 'Завтрак', 1000),
       (100000, '2020-01-31 13:00', 'Обед', 500),
       (100000, '2020-01-31 20:00', 'Ужин', 410),
       (100001, '2021-04-27 17:25:54', 'Lunch', 1021),
       (100000, '2020-11-04 16:57:43', 'Lunch', 1516),
       (100001, '2021-02-23 13:13:34', 'Breakfast', 458);