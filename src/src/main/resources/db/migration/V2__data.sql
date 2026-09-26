-- Начальные пользователи. Пароли: admin/admin, agro/agro, mech/mech, store/store.
INSERT INTO users (username, password, full_name, role, active) VALUES
('admin', '$2b$10$LwvXcfGiT1Cg/gqrVubZGO99SQ4ujdW6xEKYIgVfaBW.0wPpFSLhu', 'Администратор системы', 'ADMIN', TRUE),
('agro', '$2b$10$cdg0iQCpdIq.VxvJ3bdite6NQ708t33Sy7wxEO0FkYjP35294PNmS', 'Агронов Иван Петрович', 'AGRONOMIST', TRUE),
('mech', '$2b$10$jq4C8seRfUQk/FQ.osqcLeVQcIgNrTOE.5zMFd.2.qZ4p4tWBB5vS', 'Механик Сергей Викторович', 'MECHANIC', TRUE),
('store', '$2b$10$/iMgw2JIwwA1gDEEEsI85Od5zJyXYt9CZQQsrjnaX0BWs8fUvNT6i', 'Кладовщик Ольга Андреевна', 'STOREKEEPER', TRUE);

-- Справочная техника
INSERT INTO equipment (name, type) VALUES
('Трактор МТЗ-82', 'Трактор'),
('Комбайн НИВА СК-5М', 'Зерноуборочный комбайн'),
('Опрыскиватель ОП-2000', 'Опрыскиватель'),
('Плуг ПЛН-5-35', 'Почвообрабатывающее орудие');

-- Начальные складские остатки
INSERT INTO warehouse_items (name, unit, quantity) VALUES
('Дизельное топливо', 'л', 2500.00),
('Семена пшеницы', 'кг', 12000.00),
('Минеральное удобрение NPK', 'кг', 5000.00),
('Моторное масло 10W-40', 'л', 180.00);
