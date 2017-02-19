DROP DATABASE ebdb;
DROP USER storeuser ;
CREATE USER storeuser WITH PASSWORD 'Password2';
CREATE DATABASE ebdb;
GRANT ALL ON DATABASE ebdb TO storeuser;
\connect ebdb
-- create schema store_user and give authorization to store_user
CREATE SCHEMA AUTHORIZATION storeuser;
ALTER ROLE storeuser SET search_path TO storeuser;
