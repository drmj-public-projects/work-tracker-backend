ALTER TABLE organization_settings
ADD COLUMN time_zone VARCHAR(50) NOT NULL DEFAULT 'UTC';
