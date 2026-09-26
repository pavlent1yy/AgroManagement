-- Создание типов перечислений PostgreSQL
CREATE TYPE user_role AS ENUM ('ADMIN', 'AGRONOMIST', 'MECHANIC', 'STOREKEEPER');
CREATE TYPE warehouse_operation_type AS ENUM ('INCOME', 'OUTCOME');

-- Пользователи системы
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    role user_role NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Планы посевных работ
CREATE TABLE field_plans (
    id BIGSERIAL PRIMARY KEY,
    culture VARCHAR(100) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    area NUMERIC(12, 2) NOT NULL CHECK (area > 0),
    season VARCHAR(20) NOT NULL,
    created_by VARCHAR(100) NOT NULL
);

-- Справочник техники
CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(100) NOT NULL
);

-- Учет работы техники
CREATE TABLE work_records (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT NOT NULL REFERENCES equipment(id),
    hours NUMERIC(8, 2) NOT NULL CHECK (hours >= 0),
    fuel_consumed NUMERIC(10, 2) NOT NULL CHECK (fuel_consumed >= 0),
    record_date DATE NOT NULL
);

-- Складские ресурсы
CREATE TABLE warehouse_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    quantity NUMERIC(12, 2) NOT NULL DEFAULT 0 CHECK (quantity >= 0)
);

-- Складские операции
CREATE TABLE warehouse_operations (
    id BIGSERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES warehouse_items(id),
    operation_type warehouse_operation_type NOT NULL,
    quantity NUMERIC(12, 2) NOT NULL CHECK (quantity > 0),
    operation_date DATE NOT NULL
);

-- Журнал аудита
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_field_plans_season ON field_plans(season);
CREATE INDEX idx_work_records_date ON work_records(record_date);
CREATE INDEX idx_warehouse_operations_item ON warehouse_operations(item_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
