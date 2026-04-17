    -- USERS
    CREATE INDEX idx_users_email ON users(email);

    -- USER_ORGANIZATIONS
    CREATE INDEX idx_uo_user ON user_organizations(user_id);
    CREATE INDEX idx_uo_org ON user_organizations(organization_id);

    -- PLACES
    CREATE INDEX idx_places_org ON places(organization_id);

    -- WORK SESSIONS
    CREATE INDEX idx_ws_user ON work_sessions(user_id);
    CREATE INDEX idx_ws_org ON work_sessions(organization_id);
    CREATE INDEX idx_ws_place ON work_sessions(place_id);
    CREATE INDEX idx_ws_start_time ON work_sessions(start_time);
    CREATE INDEX idx_ws_status ON work_sessions(status);

    --Index complex for reports
    CREATE INDEX idx_ws_user_start ON work_sessions(user_id, start_time);
    CREATE INDEX idx_ws_org_start ON work_sessions(organization_id, start_time);

    -- HOURLY_RATES
    CREATE INDEX idx_hr_main_lookup ON hourly_rates(user_id, organization_id, place_id, valid_from DESC);
    CREATE INDEX idx_hr_valid_range ON hourly_rates(valid_from, valid_to);
    CREATE INDEX idx_hr_org ON hourly_rates(organization_id);
    CREATE INDEX idx_hr_user ON hourly_rates(user_id);
    CREATE UNIQUE INDEX uq_hr_unique_rate
    ON hourly_rates(user_id, organization_id, place_id, valid_from)
    WHERE is_deleted = FALSE;