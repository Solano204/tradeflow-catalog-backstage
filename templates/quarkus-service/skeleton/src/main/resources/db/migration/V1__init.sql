-- ============================================================
-- Initial schema for ${{ values.serviceName }}
-- ============================================================

CREATE TABLE IF NOT EXISTS outbox (
    id           VARCHAR(36)  PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type   VARCHAR(100) NOT NULL,
    payload      JSONB        NOT NULL,
    aggregate_id VARCHAR(36),
    published    BOOLEAN      NOT NULL DEFAULT FALSE,
    published_at TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_outbox_unpublished
    ON outbox (published, created_at)
    WHERE published = FALSE;

-- TODO: Add your domain tables here