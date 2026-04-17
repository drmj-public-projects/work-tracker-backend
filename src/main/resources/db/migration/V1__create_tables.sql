-- EXTENSION
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    name VARCHAR(255),
    user_timezone VARCHAR(100) DEFAULT 'America/La_Paz',

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE
);
-- ORGANIZATIONS
CREATE TABLE organizations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE
);
-- USER_ORGANIZATIONS
CREATE TABLE user_organizations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL, -- EMPLOYEE, EMPLOYER, ADMIN

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_uo_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_uo_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uq_user_org UNIQUE (user_id, organization_id)
);
-- ORGANIZATION SETTINGS
CREATE TABLE organization_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL UNIQUE,

    require_location BOOLEAN DEFAULT FALSE,
    allow_manual_entries BOOLEAN DEFAULT TRUE,
    allow_edit_after_submit BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_org_settings
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id)
);
-- PLACES
CREATE TABLE places (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,
    description TEXT,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_places_org FOREIGN KEY (organization_id) REFERENCES organizations(id)
);
-- WORK SESSIONS
CREATE TABLE work_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    user_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    place_id UUID NOT NULL,

    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,

    duration_minutes INTEGER,
    break_minutes INTEGER DEFAULT 0,

    notes TEXT,

    status VARCHAR(20) DEFAULT 'COMPLETED', -- ACTIVE, COMPLETED, PENDING, REJECTED
    entry_type VARCHAR(20) DEFAULT 'MANUAL', -- MANUAL, TIMER
    source VARCHAR(20), -- WEB, MOBILE

    hourly_rate NUMERIC(10,2) NOT NULL,
    total_pay NUMERIC(10,2),

    -- ubicación opcional
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    location_accuracy DECIMAL(10, 2),

    -- auditoría
    is_edited BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_ws_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ws_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_ws_place FOREIGN KEY (place_id) REFERENCES places(id),

    CONSTRAINT chk_time_valid CHECK (
        end_time IS NULL OR end_time > start_time
    )
);

-- HOURLY RATES
CREATE TABLE hourly_rates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    user_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    place_id UUID NOT NULL,

    rate NUMERIC(10,2) NOT NULL,

    valid_from TIMESTAMP WITH TIME ZONE NOT NULL,
    valid_to TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_hr_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_hr_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT fk_hr_place FOREIGN KEY (place_id) REFERENCES places(id),

    CONSTRAINT chk_hr_valid_dates CHECK (
        valid_to IS NULL OR valid_to > valid_from
    )
);