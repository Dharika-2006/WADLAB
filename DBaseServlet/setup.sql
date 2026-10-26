CREATE DATABASE IF NOT EXISTS school;
USE school;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    m1 INT,
    m2 INT,
    m3 INT,
    total INT,
    average DOUBLE,
    grade CHAR(1)
);