DELIMITER //

-- Trigger para INSERT en 'roles'
CREATE TRIGGER after_role_insert
    AFTER INSERT ON roles FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('INSERT', 'roles', CONCAT('Rol creado: ', NEW.role_name), NOW());
END; //

-- Trigger para UPDATE en 'roles'
CREATE TRIGGER after_role_update
    AFTER UPDATE ON roles FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('UPDATE', 'roles', CONCAT('Rol actualizado: ', NEW.role_name), NOW());
END; //

-- Trigger para DELETE en 'roles'
CREATE TRIGGER after_role_delete
    AFTER DELETE ON roles FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('DELETE', 'roles', CONCAT('Rol eliminado: ', OLD.role_name), NOW());
END; //

-- Trigger para INSERT en 'administrators'
CREATE TRIGGER after_administrator_insert
    AFTER INSERT ON administrators FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('INSERT', 'administrators', CONCAT('Administrador creado: ', NEW.admin_name), NOW());
END; //

-- Trigger para UPDATE en 'administrators'
CREATE TRIGGER after_administrator_update
    AFTER UPDATE ON administrators FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('UPDATE', 'administrators', CONCAT('Administrador actualizado: ', NEW.admin_name), NOW());
END; //

-- Trigger para DELETE en 'administrators'
CREATE TRIGGER after_administrator_delete
    AFTER DELETE ON administrators FOR EACH ROW
BEGIN
    INSERT INTO log (operation_type, affected_table, operation_description, operation_date)
    VALUES ('DELETE', 'administrators', CONCAT('Administrador eliminado: ', OLD.admin_name), NOW());
END; //

DELIMITER ;