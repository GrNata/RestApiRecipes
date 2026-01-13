-- Добавляем роли
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_ADMIN'),
                                 (2, 'ROLE_USER');

-- Добавляем пользователей (пароли пока в явном виде, можно потом захешировать через BCrypt)
INSERT INTO users (id, username, email, password) VALUES
                                                      (1, 'Admin', 'admin@mail.ru', '$2a$10$dntXFvve5VQlWB2hWOoIQ.2PD0BN535EnhVS.CU/mRSmHzWcJjopq'),  -- BCrypt hash от "admin1971"
                                                      (2, 'User', 'user@mail.ru', '$2a$10$e2/DT5AwiYOEOnjKSdKX6.4CazcZGmGouwK5V1tZ3f.XCR1vGLh5e');   -- BCrypt hash от "user1971"

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Назначаем роли
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),  -- Admin -> ROLE_ADMIN
                                              (1, 2),  -- Admin → ROLE_USER (если нужно)
                                              (2, 2);  -- User -> ROLE_USER



INSERT INTO category (name, image) VALUES
                                ('Супы', 'https://png.pngtree.com/png-vector/20250321/ourmid/pngtree-a-bowl-of-warm-chicken-soup-png-image_15818283.png'),
                                ('Салаты', 'https://img.freepik.com/free-psd/fresh-vibrant-vegetable-salad-glass-bowl-transparent-background_84443-31142.jpg?semt=ais_hybrid&w=740'),
                                ('Десерты', 'https://i.pinimg.com/736x/88/5e/98/885e984fec8dfb19af7086aafafef24f.jpg');

INSERT INTO ingredient (id, name) VALUES
                                  (1,'Картофель'),
                                  (2,'Морковь'),
                                  (3,'Свекла'),
                                  (4,'Мясо'),
                                  (5,'Яйца'),
                                  (6,'Мука'),
                                  (7,'Молоко'),
                                  (8,'Сахар');
-- Синхронизируем sequence с последним id
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));


-- Units (единицы измерения)
INSERT INTO unit(code, label) VALUES
                                  ('G', 'г'),
                                  ('KG', 'кг'),
                                  ('ML', 'мл'),
                                  ('L', 'л'),
                                  ('PCS', 'шт');




INSERT INTO recipe (id, name, description, image, user_id) VALUES
                                                  (1,'Оливье', 'Классический новогодний салат', 'https://img.freepik.com/premium-psd/traditional-russian-salad-olivier-transparent-background_1269588-9267.jpg?semt=ais_hybrid', 1),
                                                  (2, 'Борщ', 'Традиционный украинский борщ', 'https://thumbs.dreamstime.com/b/традиционный-украинский-борщ-с-мясом-суп-украинская-кухня-307968340.jpg', 2),
                                                  (3, 'Панкейки', 'Американские блинчики на молоке', 'https://i.pinimg.com/736x/e0/bf/5a/e0bf5a1adef2226356cf8734a9509b9d.jpg', 1),
                                                  (4,'Салат весенний', 'Весенний салат', 'https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9yZWNpcGVzLzgzNDAzL2NvdmVycy9FVWo3eDguanBlZw.jpg', 2),
                                                  (5, 'Суп весенний', 'Овощной суп', 'https://www.povarenok.ru/data/cache/2021sep/01/02/2896926_47607-640x480.jpg', 1),
                                                  (6, 'Блинчики', 'Русские блины', 'https://img.freepik.com/premium-photo/stack-delicious-crepes-white-background_495423-49220.jpg?semt=ais_hybrid', 2),
                                                  (7,'Салат мимоза', 'Любимый праздничный салат', 'https://img.povar.ru/main-micro/9f/c9/7d/71/salat_quotmimozaquot_s_plavlenim_sirom-176054.jpg', 2),
                                                  (8, 'Солянка', 'Сытный суп, первое и второе одновременно', 'https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9yZWNpcGVzLzEzMTMyMy9zdGVwcy8zZmN6VzguanBlZw.jpg', 1),
                                                  (9, 'Мороженное', 'Лакомство для жаркого дня', 'https://i.pinimg.com/474x/3c/ff/0f/3cff0f0f9fb8174b34bbf53b4b856d03.jpg?nii=t', 2);

-- Синхронизируем sequence с последним id
SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM recipe), true);

-- ОЛИВЬЕ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (1, 2);

-- БОРЩ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (2, 1);

-- ПАНКЕЙКИ → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (3, 3);
-- Салат весенний → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (4, 2);
-- Суп весенний → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (5, 1);
-- Блинчики → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (6, 3);
-- Салат мимоза → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (7, 2);
-- Солянка → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (8, 1);
-- Мороженое → категории
INSERT INTO recipe_category (recipe_id, category_id) VALUES (9, 3);

-- ИНГРЕДИЕНТЫ ОЛИВЬЕ
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                     (1, 1, 5, '2'),
                                                                     (1, 2, 5, '1'),
                                                                     (1, 5, 5, '3'),
                                                                     (1, 4, 1, '200');

-- ИНГРЕДИЕНТЫ БОРЩА
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                     (2, 1, 5, '3'),
                                                                     (2, 2, 5, '1'),
                                                                     (2, 3, 5, '1'),
                                                                     (2, 4, 1, '300');

-- ИНГРЕДИЕНТЫ ПАНКЕЙКОВ
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                     (3, 6, 1, '200'),
                                                                     (3, 7, 3, '250'),
                                                                     (3, 8, 1, '50'),
                                                                     (3, 5, 5,'1');

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




-- Пример добавления избранного
INSERT INTO user_favorite (user_id, recipe_id) VALUES (1, 1);
INSERT INTO user_favorite (user_id, recipe_id) VALUES (2, 2);