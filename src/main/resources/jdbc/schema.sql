CREATE TABLE users (
    id VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(144) NOT NULL,
    updated_by VARCHAR(144) NOT NULL,

    name VARCHAR(144) NOT NULL,
    email VARCHAR(144) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
ALTER TABLE users ADD CONSTRAINT PK_users PRIMARY KEY (id);
ALTER TABLE users ADD CONSTRAINT FK_users_created_by_id FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE users ADD CONSTRAINT FK_users_updated_by_id FOREIGN KEY (updated_by) REFERENCES users(id);

CREATE TABLE roles (
    id VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(144) NOT NULL,
    updated_by VARCHAR(144) NOT NULL,

    name VARCHAR(144) NOT NULL UNIQUE,
);
ALTER TABLE roles ADD CONSTRAINT PK_roles PRIMARY KEY (id);
ALTER TABLE roles ADD CONSTRAINT FK_roles_created_by_id FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE roles ADD CONSTRAINT FK_roles_updated_by_id FOREIGN KEY (updated_by) REFERENCES users(id);

CREATE TABLE users_roles (
    id VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(144) NOT NULL,
    updated_by VARCHAR(144) NOT NULL,

    user_id VARCHAR(144) NOT NULL,
    role_id VARCHAR(144) NOT NULL,
);
ALTER TABLE users_roles ADD CONSTRAINT PK_users_roles PRIMARY KEY (id);
ALTER TABLE users_roles ADD CONSTRAINT UN_users_roles UNIQUE (user_id, role_id);
ALTER TABLE users_roles ADD CONSTRAINT FK_users_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE users_roles ADD CONSTRAINT FK_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles(id);
ALTER TABLE users_roles ADD CONSTRAINT FK_users_roles_created_by_id FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE users_roles ADD CONSTRAINT FK_users_roles_updated_by_id FOREIGN KEY (updated_by) REFERENCES users(id);

CREATE TABLE service_requests (
    id VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(144) NOT NULL,
    updated_by VARCHAR(144) NOT NULL,

    description VARCHAR(2050) NOT NULL,
    status VARCHAR(15) NOT NULL,
    analyzed_by VARCHAR(144)
);
ALTER TABLE service_requests ADD CONSTRAINT PK_service_requests PRIMARY KEY (id);
ALTER TABLE service_requests ADD CONSTRAINT FK_service_requests_created_by_id FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE service_requests ADD CONSTRAINT FK_service_requests_updated_by_id FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE service_requests ADD CONSTRAINT FK_service_requests_analyzed_by_id FOREIGN KEY (analyzed_by) REFERENCES users(id);

CREATE TABLE service_requests_comments (
    id VARCHAR(40) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(144) NOT NULL,
    updated_by VARCHAR(144) NOT NULL,

    comment VARCHAR(2050) NOT NULL,
    service_requests_id VARCHAR(40) NOT NULL,
);
ALTER TABLE service_requests_comments ADD CONSTRAINT PK_service_requests_comments PRIMARY KEY (id);
ALTER TABLE service_requests_comments ADD CONSTRAINT FK_service_requests_comments_created_by_id FOREIGN KEY (created_by) REFERENCES users(id);
ALTER TABLE service_requests_comments ADD CONSTRAINT FK_service_requests_comments_updated_by_id FOREIGN KEY (updated_by) REFERENCES users(id);
ALTER TABLE service_requests_comments ADD CONSTRAINT FK_service_requests_comments_service_requests_id FOREIGN KEY (service_requests_id) REFERENCES service_requests(id);
