INSERT INTO category (name) VALUES
                                ('Супы'),
                                ('Салаты'),
                                ('Десерты');

INSERT INTO ingredient (id, name, unit) VALUES
                                  (1,'Картофель', 'г'),
                                  (2,'Морковь', 'г'),
                                  (3,'Свекла', 'г'),
                                  (4,'Мясо', 'г'),
                                  (5,'Яйца', 'шт'),
                                  (6,'Мука', 'г'),
                                  (7,'Молоко', 'мл'),
                                  (8,'Сахар', 'г');
-- Синхронизируем sequence с последним id
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));

INSERT INTO recipe (name, description, image) VALUES
                                                  ('Оливье', 'Классический новогодний салат', 'https://example.com/images/olivier.jpg'),
                                                  ('Борщ', 'Традиционный украинский борщ', 'https://example.com/images/borscht.jpg'),
                                                  ('Панкейки', 'Американские блинчики на молоке', 'https://example.com/images/pancakes.jpg');

-- ОЛИВЬЕ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (1, 2);

-- БОРЩ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (2, 1);

-- ПАНКЕЙКИ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (3, 3);

-- ИНГРЕДИЕНТЫ ОЛИВЬЕ
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount) VALUES
                                                                     (1, 1, '2 шт'),
                                                                     (1, 2, '1 шт'),
                                                                     (1, 5, '3 шт'),
                                                                     (1, 4, '200 г');

-- ИНГРЕДИЕНТЫ БОРЩА
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount) VALUES
                                                                     (2, 1, '3 шт'),
                                                                     (2, 2, '1 шт'),
                                                                     (2, 3, '1 шт'),
                                                                     (2, 4, '300 г');

-- ИНГРЕДИЕНТЫ ПАНКЕЙКОВ
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount) VALUES
                                                                     (3, 6, '200 г'),
                                                                     (3, 7, '250 мл'),
                                                                     (3, 8, '50 г'),
                                                                     (3, 5, '1 шт');

-- 🔥 COOKING STEPS

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


-- Добавляем роли
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_ADMIN'),
                                 (2, 'ROLE_USER');

-- Добавляем пользователей (пароли пока в явном виде, можно потом захешировать через BCrypt)
INSERT INTO users (id, username, email, password) VALUES
                                                      (1, 'Admin', 'admin@mail.ru', '$2a$10$...'),  -- BCrypt hash от "admin1971"
                                                      (2, 'User', 'user@mail.ru', '$2a$10$...');   -- BCrypt hash от "user1971"

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Назначаем роли
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),  -- Admin -> ROLE_ADMIN
                                              (2, 2);  -- User -> ROLE_USER