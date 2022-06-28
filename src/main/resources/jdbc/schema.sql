CREATE TABLE users (
    id VARCHAR(40) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(144) NOT NULL,
    updatedBy VARCHAR(144) NOT NULL,

    name VARCHAR(144) NOT NULL,
    email VARCHAR(144) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
ALTER TABLE users ADD CONSTRAINT PK_users PRIMARY KEY (id);

CREATE TABLE roles (
    id VARCHAR(40) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(144) NOT NULL,
    updatedBy VARCHAR(144) NOT NULL,

    name VARCHAR(144) NOT NULL UNIQUE,
);
ALTER TABLE roles ADD CONSTRAINT PK_roles PRIMARY KEY (id);

CREATE TABLE users_principal (
    id VARCHAR(40) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(144) NOT NULL,
    updatedBy VARCHAR(144) NOT NULL,

    user_id VARCHAR(144) NOT NULL,
    role_id VARCHAR(144) NOT NULL,
);
ALTER TABLE users_principal ADD CONSTRAINT PK_users_principal PRIMARY KEY (id);
ALTER TABLE users_principal ADD CONSTRAINT UN_users_principal UNIQUE (user_id, role_id);
ALTER TABLE users_principal ADD CONSTRAINT FK_users_principal_user_id FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE users_principal ADD CONSTRAINT FK_users_principal_role_id FOREIGN KEY (role_id) REFERENCES roles(id);
