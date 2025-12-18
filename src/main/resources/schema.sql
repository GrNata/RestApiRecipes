DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS recipe_category CASCADE;
DROP TABLE IF EXISTS cooking_step CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;

-- Сначала удалить старые таблицы, если есть
DROP TABLE IF EXISTS user_favorite CASCADE;  -- удаляем старую таблицу избранного, если есть
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Таблица ролей
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- (Опционально) связь пользователь-роли, если планируешь использовать роли
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY(user_id, role_id)
);


CREATE TABLE category (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          image VARCHAR(500)
);

CREATE TABLE ingredient (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            unit VARCHAR(50)
);

CREATE TABLE recipe (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        image VARCHAR(500),
                        user_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);
-- ON DELETE SET NULL — если пользователь удалён, поле станет NULL.

CREATE TABLE recipe_category (
                                 recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                                 category_id BIGINT NOT NULL REFERENCES category(id) ON DELETE CASCADE,
                                 PRIMARY KEY (recipe_id, category_id)
);

CREATE TABLE recipe_ingredient (
                                   recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                                   ingredient_id BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                                   amount VARCHAR(255),
                                   PRIMARY KEY (recipe_id, ingredient_id)
);

CREATE TABLE cooking_step (
                              id BIGSERIAL PRIMARY KEY,
                              recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                              step_number INT NOT NULL,
                              description TEXT NOT NULL
);




-- Таблица избранного
CREATE TABLE user_favorite (
                               user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                               PRIMARY KEY(user_id, recipe_id)
);