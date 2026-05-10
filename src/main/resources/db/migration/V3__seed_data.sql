-- =========================================
-- CLEAN DATABASE (orden correcto por FK)
-- =========================================

DELETE FROM work_sessions;
DELETE FROM hourly_rates;
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
    (uuid_generate_v4(), 'admin@demo.com', 'Admin Demo'),
    (uuid_generate_v4(), 'employee1@demo.com', 'Employee One'),
    (uuid_generate_v4(), 'employee2@demo.com', 'Employee Two'),
    (uuid_generate_v4(), 'outsider@demo.com', 'Outsider User');

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
(
    (SELECT id FROM users WHERE email='admin@demo.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    'ADMIN'
),
(
    (SELECT id FROM users WHERE email='employee1@demo.com'),
    (SELECT id FROM organizations WHERE name='Demo Company'),
    'EMPLOYEE'
),

-- Strict Company
(
    (SELECT id FROM users WHERE email='employee2@demo.com'),
    (SELECT id FROM organizations WHERE name='Strict Company'),
    'EMPLOYEE'
);

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
    (
        (SELECT id FROM organizations WHERE name='Demo Company'),
        FALSE, TRUE, TRUE
    ),
    (
        (SELECT id FROM organizations WHERE name='Strict Company'),
        TRUE, FALSE, FALSE
    );

-- =========================================
-- PLACES (con geolocalización)
-- =========================================

INSERT INTO places (
    organization_id, name, description,
    latitude, longitude, radius_meters
)
VALUES
    (
        (SELECT id FROM organizations WHERE name='Demo Company'),
        'Miss Toby Home',
        'Cleaning job',
        -17.3895, -66.1568, 100
    ),
    (
        (SELECT id FROM organizations WHERE name='Demo Company'),
        'Miss Chani Home',
        'Cleaning job',
        -17.3700, -66.1400, 120
    ),
    (
        (SELECT id FROM organizations WHERE name='Strict Company'),
        'Office HQ',
        'Main office',
        -17.3935, -66.1570, 80
    );

-- =========================================
-- HOURLY RATES
-- =========================================

-- Employee1 historial
INSERT INTO hourly_rates (
    user_id, organization_id, place_id,
    rate, valid_from, valid_to
)
VALUES
    (
        (SELECT id FROM users WHERE email='employee1@demo.com'),
        (SELECT id FROM organizations WHERE name='Demo Company'),
        (SELECT id FROM places WHERE name='Miss Toby Home'),
        8.00,
        NOW() - INTERVAL '60 days',
        NOW() - INTERVAL '30 days'
    ),
    (
        (SELECT id FROM users WHERE email='employee1@demo.com'),
        (SELECT id FROM organizations WHERE name='Demo Company'),
        (SELECT id FROM places WHERE name='Miss Toby Home'),
        10.00,
        NOW() - INTERVAL '30 days',
        NULL
    );

-- otro lugar
INSERT INTO hourly_rates (
    user_id, organization_id, place_id,
    rate, valid_from
)
VALUES
    (
        (SELECT id FROM users WHERE email='employee1@demo.com'),
        (SELECT id FROM organizations WHERE name='Demo Company'),
        (SELECT id FROM places WHERE name='Miss Chani Home'),
        12.50,
        NOW() - INTERVAL '15 days'
    );

-- Employee2 aumento
INSERT INTO hourly_rates (
    user_id, organization_id, place_id,
    rate, valid_from
)
VALUES
    (
        (SELECT id FROM users WHERE email='employee2@demo.com'),
        (SELECT id FROM organizations WHERE name='Strict Company'),
        (SELECT id FROM places WHERE name='Office HQ'),
        15.00,
        NOW() - INTERVAL '20 days'
    ),
    (
        (SELECT id FROM users WHERE email='employee2@demo.com'),
        (SELECT id FROM organizations WHERE name='Strict Company'),
        (SELECT id FROM places WHERE name='Office HQ'),
        18.00,
        NOW() - INTERVAL '2 days'
    );

-- =========================================
-- WORK SESSIONS (con total_pay y ubicación)
-- =========================================

-- COMPLETED
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time,
    duration_minutes, break_minutes,
    notes, status, entry_type, source,
    hourly_rate, total_pay,
    latitude, longitude
)
VALUES (
           (SELECT id FROM users WHERE email='employee1@demo.com'),
           (SELECT id FROM organizations WHERE name='Demo Company'),
           (SELECT id FROM places WHERE name='Miss Toby Home'),
           NOW() - INTERVAL '5 hours',
           NOW() - INTERVAL '3 hours',
           120, 10,
           'Cleaning job',
           'COMPLETED',
           'TIMER',
           'WEB',
           10.00,
           20.00,
           -17.3896, -66.1569
       );

-- ACTIVE
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time,
    status, entry_type, source,
    hourly_rate,
    latitude, longitude
)
VALUES (
           (SELECT id FROM users WHERE email='employee1@demo.com'),
           (SELECT id FROM organizations WHERE name='Demo Company'),
           (SELECT id FROM places WHERE name='Miss Chani Home'),
           NOW() - INTERVAL '1 hour',
           'ACTIVE',
           'TIMER',
           'MOBILE',
           12.50,
           -17.3701, -66.1401
       );

-- MANUAL
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time,
    duration_minutes,
    notes, status, entry_type,
    hourly_rate, total_pay
)
VALUES (
           (SELECT id FROM users WHERE email='employee1@demo.com'),
           (SELECT id FROM organizations WHERE name='Demo Company'),
           (SELECT id FROM places WHERE name='Miss Toby Home'),
           NOW() - INTERVAL '2 days',
           NOW() - INTERVAL '2 days' + INTERVAL '3 hours',
           180,
           'Manual entry',
           'COMPLETED',
           'MANUAL',
           10.00,
           30.00
       );

-- EDITED
INSERT INTO work_sessions (
    user_id, organization_id, place_id,
    start_time, end_time,
    duration_minutes,
    status,
    is_edited, edited_at,
    hourly_rate, total_pay
)
VALUES (
           (SELECT id FROM users WHERE email='employee1@demo.com'),
           (SELECT id FROM organizations WHERE name='Demo Company'),
           (SELECT id FROM places WHERE name='Miss Toby Home'),
           NOW() - INTERVAL '1 day',
           NOW() - INTERVAL '1 day' + INTERVAL '2 hours',
           120,
           'COMPLETED',
           TRUE,
           NOW(),
           10.00,
           20.00
       );