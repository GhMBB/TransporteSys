-- Script de inicialización de la base de datos
-- Ejecutar como usuario postgres

-- Crear la base de datos
CREATE DATABASE transportesys
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Spain.1252'
    LC_CTYPE = 'Spanish_Spain.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Comentario
COMMENT ON DATABASE transportesys IS 'Base de datos del sistema de transporte urbano';
