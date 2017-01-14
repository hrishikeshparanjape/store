DROP DATABASE store_db;
DROP USER store_user ;
CREATE USER store_user WITH PASSWORD 'fred';
CREATE DATABASE store_db;
GRANT ALL ON DATABASE store_db TO store_user;
\connect store_db
-- create schema store_user and give authorization to store_user
CREATE SCHEMA AUTHORIZATION store_user;
ALTER ROLE store_user SET search_path TO store_user;
