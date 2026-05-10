    -- USERS
    CREATE INDEX idx_users_email ON users(email);

    -- USER_ORGANIZATIONS
    CREATE INDEX idx_uo_user ON user_organizations(user_id);
    CREATE INDEX idx_uo_org ON user_organizations(organization_id);

    -- PLACES
    CREATE INDEX idx_places_org ON places(organization_id);

    -- unicidad real por organización
    CREATE UNIQUE INDEX ux_places_name_org
        ON places (organization_id, LOWER(name))
        WHERE is_deleted = FALSE;

    -- WORK SESSIONS
    CREATE INDEX idx_ws_user ON work_sessions(user_id);
    CREATE INDEX idx_ws_org ON work_sessions(organization_id);
    CREATE INDEX idx_ws_place ON work_sessions(place_id);
    CREATE INDEX idx_ws_start_time ON work_sessions(start_time);

    -- optimizados
    CREATE INDEX idx_ws_active
        ON work_sessions(user_id, status)
        WHERE status = 'ACTIVE' AND is_deleted = FALSE;

    CREATE INDEX idx_ws_report
        ON work_sessions(organization_id, start_time DESC)
        WHERE is_deleted = FALSE;

    -- HOURLY RATES
    CREATE INDEX idx_hr_lookup
        ON hourly_rates(user_id, organization_id, place_id, valid_from DESC);

    CREATE UNIQUE INDEX uq_hr_unique_rate
        ON hourly_rates(user_id, organization_id, place_id, valid_from)
        WHERE is_deleted = FALSE;