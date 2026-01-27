-- 1. Добавляем роли (без изменений)
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_ADMIN'),
                                 (2, 'ROLE_USER');


-- 2. Добавляем пользователей (без изменений)
INSERT INTO users (id, username, email, password) VALUES
                                                      (1, 'Admin', 'admin@mail.ru', '$2a$10$dntXFvve5VQlWB2hWOoIQ.2PD0BN535EnhVS.CU/mRSmHzWcJjopq'),
                                                      (2, 'User', 'user@mail.ru', '$2a$10$e2/DT5AwiYOEOnjKSdKX6.4CazcZGmGouwK5V1tZ3f.XCR1vGLh5e');


SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));


-- 3. Назначаем роли (без изменений)
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),  -- Admin → ROLE_ADMIN
                                              (1, 2),  -- Admin → ROLE_USER
                                              (2, 2);  -- User → ROLE_USER


-- 4. Добавляем типы категорий (category_type)
INSERT INTO category_type (name_type) VALUES
                                          ('Тип блюда'),
                                          ('Кухня'),
                                          ('Диета'),
                                          ('Сезонность');

-- 5. Добавляем значения категорий (category_value)
-- Для типа «Тип блюда»
INSERT INTO category_value (type_id, category_value) VALUES
                                                         (1, 'Салат'),
                                                         (1, 'Суп'),
                                                         (1, 'Десерт'),
                                                         (1, 'Основное блюдо');


-- Для типа «Кухня» (опционально, можно расширить)
INSERT INTO category_value (type_id, category_value) VALUES
                                                         (2, 'Русская'),
                                                         (2, 'Украинская'),
                                                         (2, 'Итальянская'),
                                                         (2, 'Американская');


-- Для типа «Диета» (опционально)
INSERT INTO category_value (type_id, category_value) VALUES
                                                         (3, 'Вегетарианское'),
                                                         (3, 'Без глютена'),
                                                         (3, 'Низкокалорийное');


-- Для типа «Сезонность» (опционально)
INSERT INTO category_value (type_id, category_value) VALUES
                                                         (4, 'Весенний'),
                                                         (4, 'Летний'),
                                                         (4, 'Осенний'),
                                                         (4, 'Зимний');


-- Синхронизируем sequence для category_value (если нужно)
-- SELECT setval('category_value_id_seq', (SELECT MAX(id) FROM category_value));


-- 6. Добавляем ингредиенты (без изменений)
INSERT INTO ingredient (id, name) VALUES
                                      (1, 'Картофель'),
                                      (2, 'Морковь'),
                                      (3, 'Свекла'),
                                      (4, 'Мясо'),
                                      (5, 'Яйца'),
                                      (6, 'Мука'),
                                      (7, 'Молоко'),
                                      (8, 'Сахар');


SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));

-- 7. Добавляем единицы измерения (без изменений)
INSERT INTO unit (code, label) VALUES
                                   ('G', 'г'),
                                   ('KG', 'кг'),
                                   ('ML', 'мл'),
                                   ('L', 'л'),
                                   ('PCS', 'шт');

-- 8. Добавляем рецепты (без изменений)
INSERT INTO recipe (id, name, description, image, user_id) VALUES
                                                               (1, 'Оливье', 'Классический новогодний салат', 'https://img.freepik.com/premium-psd/traditional-russian-salad-olivier-transparent-background_1269588-9267.jpg?semt=ais_hybrid', 1),
                                                               (2, 'Борщ', 'Традиционный украинский борщ', 'https://thumbs.dreamstime.com/b/традиционный-украинский-борщ-с-мясом-суп-украинская-кухня-307968340.jpg', 2),
                                                               (3, 'Панкейки', 'Американские блинчики на молоке', 'https://i.pinimg.com/736x/e0/bf/5a/e0bf5a1adef2226356cf8734a9509b9d.jpg', 1),
                                                               (4, 'Салат весенний', 'Весенний салат', 'https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9yZWNpcGVzLzgzNDAzL2NvdmVycy9FVWo3eDguanBlZw.jpg', 2),
                                                               (5, 'Суп весенний', 'Овощной суп', 'https://www.povarenok.ru/data/cache/2021sep/01/02/2896926_47607-640x480.jpg', 1),
                                                               (6, 'Блинчики', 'Русские блины', 'https://img.freepik.com/premium-photo/stack-delicious-crepes-white-background_495423-49220.jpg?semt=ais_hybrid', 2),
                                                               (7, 'Салат мимоза', 'Любимый праздничный салат', 'https://img.povar.ru/main-micro/9f/c9/7d/71/salat_quotmimozaquot_s_plavlenim_sirom-176054.jpg', 2),
                                                               (8, 'Солянка', 'Сытный суп, первое и второе одновременно', 'https://cdn.food.ru/unsigned/fit/640/480/ce/0/czM6Ly9tZWRpYS9waWN0dXJlcy9yZWNpcGVzLzEzMTMyMy9zdGVwcy8zZmN6VzguanBlZw.jpg', 1),
                                                               (9, 'Мороженное', 'Лакомство для жаркого дня', 'https://i.pinimg.com/474x/3c/ff/0f/3cff0f0f9fb8174b34bbf53b4b856d03.jpg?nii=t', 2);


SELECT setval('recipe_id_seq', (SELECT MAX(id) FROM recipe), true);


-- 9. Связываем рецепты с категориями (recipe_category)
-- Оливье → «Салат» (тип «Тип блюда»)
-- 9. Связываем рецепты с категориями (recipe_category)
-- Сначала удаляем старые связи, чтобы избежать дубликатов
DELETE FROM recipe_category;

INSERT INTO recipe_category (recipe_id, category_value_id) VALUES
                                                               -- Оливье → «Салат»
                                                               (1, 1),

                                                               -- Борщ → «Суп» и «Украинская»
                                                               (2, 2),  -- «Суп»
                                                               (2, 6),  -- «Украинская»

                                                               -- Панкейки → «Десерт» и «Американская»
                                                               (3, 3),  -- «Десерт»
                                                               (3, 8),  -- «Американская»

                                                               -- Салат весенний → «Салат» и «Весенний»
                                                               (4, 1),  -- «Салат»
                                                               (4, 9),  -- «Весенний»

                                                               -- Суп весенний → «Суп» и «Весенний»
                                                               (5, 2),  -- «Суп»
                                                               (5, 9),  -- «Весенний»

                                                               -- Блинчики → «Основное блюдо» и «Русская»
                                                               (6, 4),  -- «Основное блюдо»
                                                               (6, 5),  -- «Русская»

                                                               -- Салат мимоза → «Салат»
                                                               (7, 1),

                                                               -- Солянка → «Суп»
                                                               (8, 2),

                                                               -- Мороженое → «Десерт»
                                                               (9, 3);


-- 10.  INSERT INTO recipe_ingredient
-- === Оливье (1) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (1, 1, (SELECT id FROM unit WHERE code = 'KG'), '0.5'),
                                                                              (1, 2, (SELECT id FROM unit WHERE code = 'G'),  '200'),
                                                                              (1, 5, (SELECT id FROM unit WHERE code = 'PCS'),'4');

-- === Борщ (2) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (2, 1, (SELECT id FROM unit WHERE code = 'G'),  '300'),
                                                                              (2, 2, (SELECT id FROM unit WHERE code = 'G'),  '150'),
                                                                              (2, 3, (SELECT id FROM unit WHERE code = 'G'),  '200'),
                                                                              (2, 4, (SELECT id FROM unit WHERE code = 'KG'), '0.6');

-- === Панкейки (3) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (3, 6, (SELECT id FROM unit WHERE code = 'G'),  '250'),
                                                                              (3, 7, (SELECT id FROM unit WHERE code = 'ML'), '300'),
                                                                              (3, 5, (SELECT id FROM unit WHERE code = 'PCS'),'2'),
                                                                              (3, 8, (SELECT id FROM unit WHERE code = 'G'),  '50');

-- === Салат весенний (4) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (4, 1, (SELECT id FROM unit WHERE code = 'G'),  '300'),
                                                                              (4, 2, (SELECT id FROM unit WHERE code = 'G'),  '150'),
                                                                              (4, 5, (SELECT id FROM unit WHERE code = 'PCS'),'2');

-- === Суп весенний (5) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (5, 1, (SELECT id FROM unit WHERE code = 'G'), '200'),
                                                                              (5, 2, (SELECT id FROM unit WHERE code = 'G'), '100'),
                                                                              (5, 3, (SELECT id FROM unit WHERE code = 'G'), '150');

-- === Блинчики (6) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (6, 6, (SELECT id FROM unit WHERE code = 'G'),  '300'),
                                                                              (6, 7, (SELECT id FROM unit WHERE code = 'ML'), '500'),
                                                                              (6, 5, (SELECT id FROM unit WHERE code = 'PCS'),'3');

-- === Салат Мимоза (7) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (7, 5, (SELECT id FROM unit WHERE code = 'PCS'),'4'),
                                                                              (7, 2, (SELECT id FROM unit WHERE code = 'G'),  '200');

-- === Солянка (8) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (8, 4, (SELECT id FROM unit WHERE code = 'KG'), '0.7'),
                                                                              (8, 1, (SELECT id FROM unit WHERE code = 'G'),  '200'),
                                                                              (8, 2, (SELECT id FROM unit WHERE code = 'G'),  '150');

-- === Мороженое (9) ===
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, unit_id, amount) VALUES
                                                                              (9, 7, (SELECT id FROM unit WHERE code = 'ML'), '500'),
                                                                              (9, 8, (SELECT id FROM unit WHERE code = 'G'), '150');

--
-- 12. Добавляем шаги приготовления для рецептов
INSERT INTO cooking_step (recipe_id, step_number, description) VALUES
                                                                   -- Оливье
                                                                   (1, 1, 'Отварить картофель и морковь'),
                                                                   (1, 2, 'Нарезать овощи и яйца кубиками'),
                                                                   (1, 3, 'Смешать все ингредиенты с майонезом'),

                                                                   -- Борщ
                                                                   (2, 1, 'Отварить мясо до готовности'),
                                                                   (2, 2, 'Добавить нарезанную свеклу и морковь'),
                                                                   (2, 3, 'Добавить капусту и приправы, варить до готовности'),

                                                                   -- Панкейки
                                                                   (3, 1, 'Смешать муку, молоко, яйца и сахар'),
                                                                   (3, 2, 'Разогреть сковороду и обжаривать тесто'),
                                                                   (3, 3, 'Сервировать с сиропом или ягодами'),

                                                                   -- Салат весенний
                                                                   (4, 1, 'Нарезать свежие овощи'),
                                                                   (4, 2, 'Смешать с зеленью и заправкой'),

                                                                   -- Суп весенний
                                                                   (5, 1, 'Нарезать овощи'),
                                                                   (5, 2, 'Отварить овощи в бульоне'),
                                                                   (5, 3, 'Добавить соль и специи'),

                                                                   -- Блинчики
                                                                   (6, 1, 'Смешать муку, молоко, яйца и сахар'),
                                                                   (6, 2, 'Выпекать на сковороде тонкие блины'),
                                                                   (6, 3, 'Сервировать с начинкой по вкусу'),

                                                                   -- Салат мимоза
                                                                   (7, 1, 'Нарезать яйца, рыбу и сыр'),
                                                                   (7, 2, 'Сложить слоями с майонезом'),

                                                                   -- Солянка
                                                                   (8, 1, 'Нарезать мясо и колбасу'),
                                                                   (8, 2, 'Добавить огурцы, маслины и бульон'),
                                                                   (8, 3, 'Варить до готовности и подать с лимоном'),

                                                                   -- Мороженное
                                                                   (9, 1, 'Смешать ингредиенты для мороженного'),
                                                                   (9, 2, 'Заморозить до готовности');
