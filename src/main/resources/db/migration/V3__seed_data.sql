-- USER
INSERT INTO users (email, name)
VALUES ('test@example.com', 'User Demo');

-- ORGANIZATION
INSERT INTO organizations (name)
VALUES ('Demo Company');

-- USER ↔ ORGANIZATION
INSERT INTO user_organizations (user_id, organization_id, role)
VALUES (
    (SELECT id FROM users WHERE email='test@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    'EMPLOYER'
);

-- SETTINGS
INSERT INTO organization_settings (organization_id)
VALUES (
    (SELECT id FROM organizations WHERE name='Demo Company')
);

-- PLACES
INSERT INTO places (organization_id, name, description)
VALUES
(
    (SELECT id FROM organizations WHERE name='Demo Company'),
    'Miss Toby Home',
    'Cleaning job in Miss Toby home'
),
(
    (SELECT id FROM organizations WHERE name='Demo Company'),
    'Miss Chani Home',
    'Cleaning job in Miss Chani home'
);

-- WORK SESSION (cerrada)
INSERT INTO work_sessions (
    user_id,
    organization_id,
    place_id,
    start_time,
    end_time,
    duration_minutes,
    notes,
    source
)
VALUES (
    (SELECT id FROM users WHERE email='test@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Toby Home'),
    NOW() - INTERVAL '2 hours',
    NOW(),
    120,
    'Initial completed session',
    'WEB'
);

-- WORK SESSION (activa)
INSERT INTO work_sessions (
    user_id,
    organization_id,
    place_id,
    start_time,
    status,
    source
)
VALUES (
    (SELECT id FROM users WHERE email='test@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Chani Home'),
    NOW() - INTERVAL '1 hour',
    'ACTIVE',
    'MOBILE'
);