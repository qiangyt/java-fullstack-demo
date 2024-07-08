-- DROP TABLE IF EXISTS `demo_user`;
-- password: 123456A@b 
INSERT INTO `demo_user` (id, name, password, email, created_at) 
                 VALUES ('1', 'admin', '$2a$10$EUuXFXIaMcgOqGjJVqSs1.7AZw6jbwL.pI6HfcrW9gHWCv53FUi9W', 'u1@example.com', now());

INSERT INTO `demo_message` (id, content, post_id, parent_id, created_at, created_by)
                    VALUES ('1', 'c1', NULL, NULL, '2023-01-01 12:00:00.000', '1');

INSERT INTO `demo_message` (id, content, post_id, parent_id, created_at, created_by)
                    VALUES ('2', 'c1-1', '1', '1', '2023-01-01 12:01:00.000', '1');
INSERT INTO `demo_message` (id, content, post_id, parent_id, created_at, created_by)
                    VALUES ('3', 'c1-2', '1', '1', '2023-01-01 12:02:00.000', '1');
INSERT INTO `demo_message` (id, content, post_id, parent_id, created_at, created_by)
                    VALUES ('4', 'c1-2-1', '1', '3', '2023-01-01 12:03:00.000', '1');

INSERT INTO `demo_message` (id, content, post_id, parent_id, created_at, created_by)
                    VALUES ('5', 'c2', NULL, NULL, '2023-01-01 12:04:00.000', '1');