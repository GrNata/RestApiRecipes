-- Удаляем старые таблицы (включая прежнюю category)
DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS recipe_category CASCADE;
DROP TABLE IF EXISTS cooking_step CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS category CASCADE;          -- старая таблица категорий
DROP TABLE IF EXISTS category_value CASCADE;  -- на случай, если уже есть
DROP TABLE IF EXISTS category_type CASCADE;     -- на случай, если уже есть
DROP TABLE IF EXISTS recipe CASCADE;

DROP TABLE IF EXISTS user_favorite CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;

-- 1. Таблица ролей
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Связь пользователь–роли (опционально)
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

-- 4. Таблица токенов обновления
CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               user_email VARCHAR(100) NOT NULL,
                               expiry_date TIMESTAMP NOT NULL
);

-- 5. Таблица типов категорий (например: «Тип блюда», «Кухня», «Диета», «Сезонность»)
CREATE TABLE category_type (
                               id BIGSERIAL PRIMARY KEY,
                               name_type VARCHAR(100) NOT NULL UNIQUE  -- название типа категории
);

-- 6. Таблица значений категорий (например: «салат», «итальянская», «вегетарианское»)
CREATE TABLE category_value (
                                id BIGSERIAL PRIMARY KEY,
                                type_id BIGINT NOT NULL REFERENCES category_type(id) ON DELETE CASCADE,
                                category_value VARCHAR(100) NOT NULL,
    -- Уникальность комбинации: один тип → одно значение
                                CONSTRAINT uk_type_value UNIQUE (type_id, category_value)
);

-- 7. Таблица рецептов
CREATE TABLE recipe (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        image VARCHAR(500),
                        base_servings INT NOT NULL DEFAULT 1,
                        user_id BIGINT REFERENCES users(id) ON DELETE SET NULL  -- если пользователь удалён
);

-- 8. Связь рецепт–значение категории (многие-ко-многим)
CREATE TABLE recipe_category (
                                 recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                                 category_value_id BIGINT NOT NULL REFERENCES category_value(id) ON DELETE CASCADE,
                                 PRIMARY KEY (recipe_id, category_value_id)
);

-- 9. Таблица ингредиентов
CREATE TABLE ingredient (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            name_eng VARCHAR(255),
                            energy_kcal_100g INTEGER
);
--  OFF ты будешь дергать по name_eng, индекс сильно ускорит fallback-логику
CREATE INDEX idx_ingredient_name_eng ON ingredient(name_eng);

-- 10. Таблица единиц измерения
CREATE TABLE unit (
                      id BIGSERIAL PRIMARY KEY,
                      code VARCHAR(20) NOT NULL UNIQUE,
                      label VARCHAR(50) NOT NULL
);

-- 11. Таблица ингредиентов в рецепте
CREATE TABLE recipe_ingredient (
                                   recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                                   ingredient_id BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
                                   unit_id BIGINT NOT NULL REFERENCES unit(id),
                                   amount VARCHAR(255),
                                   PRIMARY KEY (recipe_id, ingredient_id)
);

-- 12. Таблица шагов приготовления
CREATE TABLE cooking_step (
                              id BIGSERIAL PRIMARY KEY,
                              recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                              step_number INT NOT NULL,
                              description TEXT NOT NULL
);

-- 13. Таблица избранного (пользователь → рецепт)
CREATE TABLE user_favorite (
                               user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
                               PRIMARY KEY (user_id, recipe_id)
);
