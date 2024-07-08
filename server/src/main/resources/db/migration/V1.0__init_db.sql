SET FOREIGN_KEY_CHECKS=0;

-- DROP TABLE IF EXISTS `demo_user`;
CREATE TABLE `demo_user` (
    `id`                 CHAR(22)     CHARACTER SET latin1   NOT NULL,
    
    `name`               VARCHAR(20)   NOT NULL,
    `password`           VARCHAR(60)   CHARACTER SET latin1  NOT NULL, -- hashed
    `email`              VARCHAR(100)  NOT NULL,

    `created_at`         DATETIME(3)   NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB, DEFAULT CHARACTER SET utf8mb4;

CREATE INDEX `demo_user_idx_id` ON `demo_user`(`id`(8));
CREATE UNIQUE INDEX `demo_user_idx_name` ON `demo_user`(`name`);
CREATE UNIQUE INDEX `demo_user_idx_email` ON `demo_user`(`email`);


CREATE TABLE `demo_message` (
    `id`                 CHAR(22)      CHARACTER SET latin1   NOT NULL,
    
    `content`            VARCHAR(200)  NOT NULL,
    `post_id`            CHAR(22)      CHARACTER SET latin1,
    `parent_id`          CHAR(22)      CHARACTER SET latin1,

    `created_at`         DATETIME(3)   NOT NULL,
    `created_by`         CHAR(22)      CHARACTER SET latin1   NOT NULL,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB, DEFAULT CHARACTER SET utf8mb4;

CREATE INDEX `demo_message_idx_id` ON `demo_message`(`id`(8));
CREATE INDEX `demo_message_idx_created_at` ON `demo_message`(`created_at`);

CREATE INDEX `demo_comment_idx_post_id` ON `demo_message`(`post_id`(8));
CREATE INDEX `demo_comment_idx_parent_id` ON `demo_message`(`parent_id`(8));


