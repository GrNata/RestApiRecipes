DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS recipe_category CASCADE;
DROP TABLE IF EXISTS cooking_step CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;

DROP TABLE IF EXISTS user_favorite CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Таблица ролей
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица пользователей
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Связь пользователь-роли
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY(user_id, role_id),
                            FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Таблица категорий
CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          image VARCHAR(500)
);

-- Таблица ингредиентов
CREATE TABLE ingredient (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            unit VARCHAR(50)
);

-- Таблица рецептов
CREATE TABLE recipe (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        image VARCHAR(500),
                        user_id BIGINT,
                        FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Связь рецепт-категория
CREATE TABLE recipe_category (
                                 recipe_id BIGINT NOT NULL,
                                 category_id BIGINT NOT NULL,
                                 PRIMARY KEY(recipe_id, category_id),
                                 FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
                                 FOREIGN KEY(category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Связь рецепт-ингредиент
CREATE TABLE recipe_ingredient (
                                   recipe_id BIGINT NOT NULL,
                                   ingredient_id BIGINT NOT NULL,
                                   amount VARCHAR(255),
                                   PRIMARY KEY(recipe_id, ingredient_id),
                                   FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
                                   FOREIGN KEY(ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

-- Таблица шагов приготовления
CREATE TABLE cooking_step (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              recipe_id BIGINT NOT NULL,
                              step_number INT NOT NULL,
                              description TEXT NOT NULL,
                              FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
);

-- Таблица избранного пользователя
CREATE TABLE user_favorite (
                               user_id BIGINT NOT NULL,
                               recipe_id BIGINT NOT NULL,
                               PRIMARY KEY(user_id, recipe_id),
                               FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
);