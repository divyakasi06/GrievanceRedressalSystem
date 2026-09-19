
-- Grievance Redressal System --

DROP DATABASE IF EXISTS grievance_system;
CREATE DATABASE grievance_system;
USE grievance_system;
CREATE TABLE departments (
    dept_code VARCHAR(10) PRIMARY KEY,
    dept_name VARCHAR(60) NOT NULL
);

INSERT INTO departments (dept_code, dept_name) VALUES
('IT',    'Information Technology'),
('CSE',   'Computer Science Engineering'),
('ECE',   'Electronics and Communication Engineering'),
('EEE',   'Electrical and Electronics Engineering'),
('MECH',  'Mechanical Engineering'),
('CIVIL', 'Civil Engineering');
CREATE TABLE users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('student','hod','principal') NOT NULL,
    college    VARCHAR(100) NOT NULL,
    department VARCHAR(10),
    FOREIGN KEY (department) REFERENCES departments(dept_code),
    CONSTRAINT chk_dept_required
        CHECK (role = 'principal' OR department IS NOT NULL)
);
CREATE TABLE grievances (
    grievance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id   VARCHAR(50) NOT NULL,
    department   VARCHAR(10) NOT NULL,
    category     VARCHAR(100),
    description  TEXT,
    status       VARCHAR(20) DEFAULT 'Pending',
    remarks      VARCHAR(255) DEFAULT '',
    FOREIGN KEY (department) REFERENCES departments(dept_code),
    FOREIGN KEY (student_id) REFERENCES users(username)
);

INSERT INTO users (username, password, role, college, department) VALUES
('S101', 'pass123', 'student', 'My College', 'IT'),
('S201', 'pass123', 'student', 'My College', 'CSE'),
('S301', 'pass123', 'student', 'My College', 'ECE');

INSERT INTO users (username, password, role, college, department) VALUES
('hod_it',  'hodpass', 'hod', 'My College', 'IT'),
('hod_cse', 'hodpass', 'hod', 'My College', 'CSE'),
('hod_ece', 'hodpass', 'hod', 'My College', 'ECE');

INSERT INTO users (username, password, role, college, department) VALUES
('principal1', 'adminpass', 'principal', 'My College', NULL);

INSERT INTO grievances (student_id, department, category, description, status, remarks) VALUES
('S101', 'IT',  'Lab Equipment', 'Projector not working in Lab 2', 'Pending', ''),
('S201', 'CSE', 'Wifi',          'Wifi is very slow in the CSE block', 'Pending', ''),
('S301', 'ECE', 'Timetable',     'Clash between two exams on the same day', 'Pending', '');