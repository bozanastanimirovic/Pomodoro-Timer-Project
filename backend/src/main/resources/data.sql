INSERT INTO roles (role_name) VALUES
('ROLE_USER'),
('ROLE_ADMIN');

INSERT INTO users (username, email, name, surname, password, session_counter, is_verified, role_id)
VALUES ('johnsmith', 'john.smith@example.com', 'John', 'Smith', '$2y$10$CR6EZYGB63QCciiBvYua/ufMLO1meMkQW3HyHj0vzrT0efpi7Z98W', 0, false, 1),
('janeclark', 'jane.clark@example.com', 'Jane', 'Clark', '$2y$10$CR6EZYGB63QCciiBvYua/ufMLO1meMkQW3HyHj0vzrT0efpi7Z98W', 0, false, 2),
('markwalker', 'mark.walker@example.com', 'Mark', 'Walker', '$2y$10$CR6EZYGB63QCciiBvYua/ufMLO1meMkQW3HyHj0vzrT0efpi7Z98W', 0, false, 1),
('lucyhale', 'lucy.hale@example.com', 'Lucy', 'White', '$2y$10$CR6EZYGB63QCciiBvYua/ufMLO1meMkQW3HyHj0vzrT0efpi7Z98W', 0, false, 1),
('alexblack', 'alex.black@example.com', 'Alex', 'Black', '$2y$10$CR6EZYGB63QCciiBvYua/ufMLO1meMkQW3HyHj0vzrT0efpi7Z98W', 0, false, 2);

INSERT INTO team (team_name)
VALUES ('Development Team'),
('Design Team'),
('QA Team'),
('HR Team'),
('Marketing Team');

INSERT INTO task (name, sessions_completed, is_finished, user_id, team_id)
VALUES ('Develop Feature A', 5, false, 1, 1), --
('Design New UI', 3, false, 2, 2),
('Test Feature B', 4, false, 3, 3),
('Recruit HR', 2, false, 4, 4),
('Create Marketing Campaign', 7, false, 5, 5);  -- user_id = 5, team_id = 5

INSERT INTO session (time_left, session_type, is_paused, is_finished, date, user_id)
VALUES (120, 'POMODORO', false, false, '2024-11-07', 1),
(90, 'LONG_BREAK', false, false, '2024-11-07', 2),
(60, 'SHORT_BREAK', false, false, '2024-11-07', 3),
(30, 'SHORT_BREAK', true, false, '2024-11-07', 4),
(100, 'POMODORO', false, false, '2024-11-07', 5);

INSERT INTO user_team (user_id, team_id)
VALUES (1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(1, 2),
(3, 5);




