CREATE TABLE test.chat_messages
(
    id          BIGINT auto_increment NOT NULL,
    session_id  varchar(100) NULL,
    `role`      varchar(100) NULL,
    content     TEXT NULL,
    create_time DATETIME NULL,
    CONSTRAINT chat_messages_pk PRIMARY KEY (id)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

CREATE TABLE test.chat_sessions
(
    id          varchar(100) NOT NULL,
    create_by   BIGINT       NOT NULL,
    create_time DATETIME NULL,
    CONSTRAINT chat_sessions_pk PRIMARY KEY (id)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

CREATE TABLE test.users
(
    id         BIGINT auto_increment NOT NULL,
    user_name  varchar(100)      NOT NULL,
    password   varchar(100)      NOT NULL,
    first_name varchar(100) NULL,
    last_name  varchar(100) NULL,
    status     TINYINT DEFAULT 1 NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_unique UNIQUE KEY (user_name)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

