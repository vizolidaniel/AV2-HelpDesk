-- PASSWORD = admin
INSERT INTO users (
    id,
    createdAt,
    updatedAt,
    createdBy,
    updatedBy,
    name,
    email,
    password
) VALUES (
    'b0501818-f1a8-11ec-8ea0-0242ac120002',
    '2022-06-21T00:00:00',
    '2022-06-21T00:00:00',
    'admin@admin.com.br',
    'admin@admin.com.br',
    'Admin',
    'admin@admin.com.br',
    '$2a$12$.wIt6/gZOCOzadiaKlrTluu3nIDSM7X4kIIciNZlbq.AyMW7FIENC'
);

INSERT INTO roles (
    id,
    createdAt,
    updatedAt,
    createdBy,
    updatedBy,
    name,
) VALUES (
    '9c4f933e-f6d0-11ec-b939-0242ac120002',
    '2022-06-21T00:00:00',
    '2022-06-21T00:00:00',
    'admin@admin.com.br',
    'admin@admin.com.br',
    'ADMIN'
);

INSERT INTO users_principal (
    id,
    createdAt,
    updatedAt,
    createdBy,
    updatedBy,
    role_id,
    user_id
) VALUES (
    '9f760dc2-f6d0-11ec-b939-0242ac120002',
    '2022-06-21T00:00:00',
    '2022-06-21T00:00:00',
    'admin@admin.com.br',
    'admin@admin.com.br',
    '9c4f933e-f6d0-11ec-b939-0242ac120002',
    'b0501818-f1a8-11ec-8ea0-0242ac120002'
);
