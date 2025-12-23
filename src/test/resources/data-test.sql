-- Добавляем роли
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_ADMIN'),
                                 (2, 'ROLE_USER');

ALTER TABLE roles ALTER COLUMN id RESTART WITH 3;

-- Добавляем пользователей (пароли в BCrypt)
INSERT INTO users (id, username, email, password) VALUES
                                                      (1, 'Admin', 'admin@mail.ru', '$2a$10$dntXFvve5VQlWB2hWOoIQ.2PD0BN535EnhVS.CU/mRSmHzWcJjopq'),
                                                      (2, 'User', 'user@mail.ru', '$2a$10$e2/DT5AwiYOEOnjKSdKX6.4CazcZGmGouwK5V1tZ3f.XCR1vGLh5e');

ALTER TABLE users ALTER COLUMN id RESTART WITH 3;

-- Назначаем роли
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),
                                              (1, 2),
                                              (2, 2);

-- Категории
INSERT INTO category (id, name) VALUES
                                    (1, 'Супы'),
                                    (2, 'Салаты'),
                                    (3, 'Десерты');

ALTER TABLE category ALTER COLUMN id RESTART WITH 4;


-- Ингредиенты
INSERT INTO ingredient (id, name, unit) VALUES
                                            (1,'Картофель', 'г'),
                                            (2,'Морковь', 'г'),
                                            (3,'Свекла', 'г'),
                                            (4,'Мясо', 'г'),
                                            (5,'Яйца', 'шт'),
                                            (6,'Мука', 'г'),
                                            (7,'Молоко', 'мл'),
                                            (8,'Сахар', 'г');

ALTER TABLE ingredient ALTER COLUMN id RESTART WITH 9;

-- Рецепты
INSERT INTO recipe (id, name, description, image, user_id) VALUES
                                                               (1,'Оливье', 'Классический новогодний салат', 'https://example.com/images/olivier.jpg', 1),
                                                               (2, 'Борщ', 'Традиционный украинский борщ', 'https://example.com/images/borscht.jpg', 2),
                                                               (3, 'Панкейки', 'Американские блинчики на молоке', 'https://example.com/images/pancakes.jpg', 1);

ALTER TABLE recipe ALTER COLUMN id RESTART WITH 4;

-- Связь рецептов с категориями
INSERT INTO recipe_category (recipe_id, category_id) VALUES
                                                         (1, 2),
                                                         (2, 1),
                                                         (3, 3);

-- Связь рецептов с ингредиентами
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount) VALUES
                                                                     (1, 1, '2 шт'),
                                                                     (1, 2, '1 шт'),
                                                                     (1, 5, '3 шт'),
                                                                     (1, 4, '200 г'),
                                                                     (2, 1, '3 шт'),
                                                                     (2, 2, '1 шт'),
                                                                     (2, 3, '1 шт'),
                                                                     (2, 4, '300 г'),
                                                                     (3, 6, '200 г'),
                                                                     (3, 7, '250 мл'),
                                                                     (3, 8, '50 г'),
                                                                     (3, 5, '1 шт');

-- Шаги приготовления
-- ОЛИВЬЕ
INSERT INTO cooking_step (recipe_id, step_number, description) VALUES
                                                                   (1, 1, 'Отварить картофель, морковь и яйца до готовности'),
                                                                   (1, 2, 'Нарезать овощи и яйца кубиками'),
                                                                   (1, 3, 'Добавить мясо, огурцы и перемешать'),
                                                                   (1, 4, 'Заправить майонезом и подать');

-- БОРЩ
INSERT INTO cooking_step (recipe_id, step_number, description) VALUES
                                                                   (2, 1, 'Сварить мясной бульон'),
                                                                   (2, 2, 'Добавить картофель и морковь'),
                                                                   (2, 3, 'Потушить свеклу отдельно'),
                                                                   (2, 4, 'Соединить ингредиенты и варить 20 минут'),
                                                                   (2, 5, 'Подать со сметаной');

-- ПАНКЕЙКИ
INSERT INTO cooking_step (recipe_id, step_number, description) VALUES
                                                                   (3, 1, 'Смешать муку, сахар и щепотку соли'),
                                                                   (3, 2, 'Добавить молоко и яйцо, взбить тесто'),
                                                                   (3, 3, 'Вылить тесто порциями на сковороду'),
                                                                   (3, 4, 'Жарить до золотистой корочки');

-- Пример добавления избранного
INSERT INTO user_favorite (user_id, recipe_id) VALUES
                                                   (1, 1),
                                                   (2, 2);