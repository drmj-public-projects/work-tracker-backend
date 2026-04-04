-- =========================================
-- 🧹 CLEAN DATABASE
-- =========================================

DELETE FROM work_sessions;
DELETE FROM places;
DELETE FROM organization_settings;
DELETE FROM user_organizations;
DELETE FROM users;
DELETE FROM organizations;

-- =========================================
-- USERS
-- =========================================

INSERT INTO users (id, email, name)
VALUES
(uuid_generate_v4(), 'test@example.com', 'User Demo'),
(uuid_generate_v4(), 'employee1@example.com', 'Employee One'),
(uuid_generate_v4(), 'employee2@example.com', 'Employee Two'),
(uuid_generate_v4(), 'outsider@example.com', 'Outsider User');

-- =========================================
-- ORGANIZATIONS
-- =========================================

INSERT INTO organizations (id, name)
VALUES
(uuid_generate_v4(), 'Demo Company'),
(uuid_generate_v4(), 'Strict Company');

-- =========================================
-- USER ↔ ORGANIZATION
-- =========================================

INSERT INTO user_organizations (user_id, organization_id, role)
VALUES
-- Demo Company
((SELECT id FROM users WHERE email='test@example.com'),
 (SELECT id FROM organizations WHERE name='Demo Company'),
 'EMPLOYER'),

((SELECT id FROM users WHERE email='employee1@example.com'),
 (SELECT id FROM organizations WHERE name='Demo Company'),
 'EMPLOYEE'),

-- Strict Company
((SELECT id FROM users WHERE email='employee2@example.com'),
 (SELECT id FROM organizations WHERE name='Strict Company'),
 'EMPLOYEE');

-- =========================================
-- ORGANIZATION SETTINGS
-- =========================================

INSERT INTO organization_settings (
    organization_id,
    require_location,
    allow_manual_entries,
    allow_edit_after_submit
)
VALUES
-- Demo Company (flexible)
(
 (SELECT id FROM organizations WHERE name='Demo Company'),
 FALSE,
 TRUE,
 TRUE
),

-- Strict Company (restrictiva)
(
 (SELECT id FROM organizations WHERE name='Strict Company'),
 TRUE,
 FALSE,
 FALSE
);

-- =========================================
-- PLACES
-- =========================================

INSERT INTO places (organization_id, name, description)
VALUES
-- Demo Company
(
 (SELECT id FROM organizations WHERE name='Demo Company'),
 'Miss Toby Home',
 'Cleaning job in Miss Toby home'
),
(
 (SELECT id FROM organizations WHERE name='Demo Company'),
 'Miss Chani Home',
 'Cleaning job in Miss Chani home'
),

-- Strict Company
(
 (SELECT id FROM organizations WHERE name='Strict Company'),
 'Office HQ',
 'Main office building'
);

-- =========================================
-- WORK SESSIONS
-- =========================================

-- COMPLETED SESSION
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time, duration_minutes,
    notes, status, entry_type, source
)
VALUES (
    (SELECT id FROM users WHERE email='employee1@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Toby Home'),
    NOW() - INTERVAL '5 hours',
    NOW() - INTERVAL '3 hours',
    120,
    'Completed cleaning job',
    'COMPLETED',
    'TIMER',
    'WEB'
);

-- ACTIVE SESSION (bloquea nuevos start)
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, status, entry_type, source
)
VALUES (
    (SELECT id FROM users WHERE email='employee1@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Chani Home'),
    NOW() - INTERVAL '1 hour',
    'ACTIVE',
    'TIMER',
    'MOBILE'
);

-- MANUAL SESSION
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time, duration_minutes,
    notes, status, entry_type, source
)
VALUES (
    (SELECT id FROM users WHERE email='employee1@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Toby Home'),
    NOW() - INTERVAL '2 days',
    NOW() - INTERVAL '2 days' + INTERVAL '3 hours',
    180,
    'Manual entry test',
    'COMPLETED',
    'MANUAL',
    'WEB'
);

-- STRICT ORG (requiere location en lógica backend)
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, status, entry_type, source
)
VALUES (
    (SELECT id FROM users WHERE email='employee2@example.com'),
    (SELECT id FROM organizations WHERE name='Strict Company'),
    (SELECT id FROM places WHERE name='Office HQ'),
    NOW() - INTERVAL '30 minutes',
    'ACTIVE',
    'TIMER',
    'MOBILE'
);

-- EDITED SESSION
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time, duration_minutes,
    notes, status, entry_type,
    is_edited, edited_at
)
VALUES (
    (SELECT id FROM users WHERE email='employee1@example.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    (SELECT id FROM places WHERE name='Miss Toby Home'),
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day' + INTERVAL '2 hours',
    120,
    'Edited session',
    'COMPLETED',
    'MANUAL',
    TRUE,
    NOW()
);