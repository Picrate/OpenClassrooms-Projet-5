/*
 SQL SCHEMA CREATION SCRIPT FOR Test Yoga
 Version : 1.0
 Author : Patrice ALLARY
*/

--- TEACHERS
INSERT INTO `TEACHERS` (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN')
;
--- USERS (Password: test!1234)
INSERT INTO `USERS` (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('User', 'User', false, 'user@studio.com', '$2a$10$uM4jAI0NOYrGWpdB1BZkvup0421QcXWqtawxqHD1OwfF2g34pOrMC')
;
--- YOGA SESSIONS
INSERT INTO SESSIONS (NAME, DESCRIPTION, DATE, TEACHER_ID, CREATED_AT)
VALUES ( 'Session 1', 'First Yoga Session', '2024-04-01', 1, '2024-01-01'),
       ( 'Session 2', 'Second Yoga Session', '2024-04-01', 2, '2024-01-01')
;
--- SUBSCRIBED USERS
INSERT INTO PARTICIPATE (user_id, session_id)
VALUES ( 2, 2);