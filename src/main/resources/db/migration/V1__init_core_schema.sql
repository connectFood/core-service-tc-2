------------------------------------------------------------
-- EXTENSIONS & SCHEMA
------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS core;

------------------------------------------------------------
-- FUNCTION: update updated_at
------------------------------------------------------------
CREATE OR REPLACE FUNCTION core.set_updated_at()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at := NOW();
RETURN NEW;
END $$;

------------------------------------------------------------
-- TABLE: users_type
------------------------------------------------------------
CREATE TABLE core.users_type (
                               id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               uuid         UUID NOT NULL DEFAULT gen_random_uuid(),
                               name         VARCHAR(255) NOT NULL,
                               description  TEXT,
                               created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               version      INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.users_type
  ADD CONSTRAINT uq_users_type_uuid UNIQUE (uuid),
  ADD CONSTRAINT uq_users_type_name UNIQUE (name);

------------------------------------------------------------
-- TABLE: users
------------------------------------------------------------
CREATE TABLE core.users (
                          id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          uuid           UUID NOT NULL DEFAULT gen_random_uuid(),
                          full_name      VARCHAR(255) NOT NULL,
                          email          CITEXT NOT NULL,
                          password       VARCHAR(255) NOT NULL,
                          users_type_id  BIGINT NOT NULL REFERENCES core.users_type(id),
                          created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          version        INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.users
  ADD CONSTRAINT uq_users_uuid UNIQUE (uuid),
  ADD CONSTRAINT uq_users_email UNIQUE (email);

CREATE INDEX idx_users_type ON core.users(users_type_id);

------------------------------------------------------------
-- TABLE: address
------------------------------------------------------------
CREATE TABLE core.address (
                            id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            uuid          UUID NOT NULL DEFAULT gen_random_uuid(),
                            street        VARCHAR(255) NOT NULL,
                            number        VARCHAR(50) NOT NULL,
                            complement    VARCHAR(255),
                            neighborhood  VARCHAR(255) NOT NULL,
                            city          VARCHAR(255) NOT NULL,
                            state         VARCHAR(255) NOT NULL,
                            zip_code      VARCHAR(30) NOT NULL,
                            country       VARCHAR(100) NOT NULL,
                            created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                            updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                            version       INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.address
  ADD CONSTRAINT uq_address_uuid UNIQUE (uuid);

------------------------------------------------------------
-- TABLE: users_address (N:N)
------------------------------------------------------------
CREATE TABLE core.users_address (
                                  id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  uuid        UUID NOT NULL DEFAULT gen_random_uuid(),
                                  user_id     BIGINT NOT NULL REFERENCES core.users(id) ON DELETE CASCADE,
                                  address_id  BIGINT NOT NULL REFERENCES core.address(id) ON DELETE CASCADE,
                                  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  version     INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.users_address
  ADD CONSTRAINT uq_users_address_uuid UNIQUE (uuid);

CREATE INDEX idx_users_address_user ON core.users_address(user_id);
CREATE INDEX idx_users_address_address ON core.users_address(address_id);

------------------------------------------------------------
-- TABLE: restaurant_type
------------------------------------------------------------
CREATE TABLE core.restaurant_type (
                                    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    uuid        UUID NOT NULL DEFAULT gen_random_uuid(),
                                    name        VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                    version     INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant_type
  ADD CONSTRAINT uq_restaurant_type_uuid UNIQUE (uuid);

------------------------------------------------------------
-- TABLE: restaurant
------------------------------------------------------------
CREATE TABLE core.restaurant (
                               id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               uuid              UUID NOT NULL DEFAULT gen_random_uuid(),
                               name              VARCHAR(255) NOT NULL,
                               restaurant_type_id BIGINT NOT NULL REFERENCES core.restaurant_type(id),
                               created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               version           INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant
  ADD CONSTRAINT uq_restaurant_uuid UNIQUE (uuid);

CREATE INDEX idx_restaurant_type ON core.restaurant(restaurant_type_id);

------------------------------------------------------------
-- TABLE: restaurant_address (1:N)
------------------------------------------------------------
CREATE TABLE core.restaurant_address (
                                       id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       uuid          UUID NOT NULL DEFAULT gen_random_uuid(),
                                       restaurant_id BIGINT NOT NULL REFERENCES core.restaurant(id) ON DELETE CASCADE,
                                       address_id    BIGINT NOT NULL REFERENCES core.address(id) ON DELETE CASCADE,
                                       created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                       updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                       version       INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant_address
  ADD CONSTRAINT uq_restaurant_address_uuid UNIQUE (uuid);

------------------------------------------------------------
-- TABLE: users_restaurant (N:N) – Dono ou staff
------------------------------------------------------------
CREATE TABLE core.users_restaurant (
                                     id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     uuid          UUID NOT NULL DEFAULT gen_random_uuid(),
                                     user_id       BIGINT NOT NULL REFERENCES core.users(id) ON DELETE CASCADE,
                                     restaurant_id BIGINT NOT NULL REFERENCES core.restaurant(id) ON DELETE CASCADE,
                                     created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     version       INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.users_restaurant
  ADD CONSTRAINT uq_users_restaurant_uuid UNIQUE (uuid);

CREATE INDEX idx_users_restaurant_user ON core.users_restaurant(user_id);
CREATE INDEX idx_users_restaurant_rest ON core.users_restaurant(restaurant_id);

------------------------------------------------------------
-- TABLE: restaurant_items
------------------------------------------------------------
CREATE TABLE core.restaurant_items (
                                     id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     uuid          UUID NOT NULL DEFAULT gen_random_uuid(),
                                     restaurant_id BIGINT NOT NULL REFERENCES core.restaurant(id) ON DELETE CASCADE,
                                     name          VARCHAR(255) NOT NULL,
                                     description   TEXT,
                                     value         NUMERIC(10,2) NOT NULL,
                                     request_type  VARCHAR(255), -- consumir no local / delivery / etc
                                     created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                     version       INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant_items
  ADD CONSTRAINT uq_restaurant_items_uuid UNIQUE (uuid);

CREATE INDEX idx_items_restaurant ON core.restaurant_items(restaurant_id);

------------------------------------------------------------
-- TABLE: restaurant_items_images
------------------------------------------------------------
CREATE TABLE core.restaurant_items_images (
                                            id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                            uuid                 UUID NOT NULL DEFAULT gen_random_uuid(),
                                            restaurant_items_id  BIGINT NOT NULL REFERENCES core.restaurant_items(id) ON DELETE CASCADE,
                                            name                 VARCHAR(255),
                                            description          TEXT,
                                            path                 TEXT NOT NULL,
                                            created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                            updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                            version              INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant_items_images
  ADD CONSTRAINT uq_restaurant_items_images_uuid UNIQUE (uuid);

------------------------------------------------------------
-- TABLE: restaurant_opening_hours
------------------------------------------------------------
CREATE TABLE core.restaurant_opening_hours (
                                             id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                             uuid          UUID NOT NULL DEFAULT gen_random_uuid(),
                                             restaurant_id BIGINT NOT NULL REFERENCES core.restaurant(id) ON DELETE CASCADE,
                                             day_week      INTEGER NOT NULL, -- 0-6
                                             start_time    TIME NOT NULL,
                                             end_time      TIME NOT NULL,
                                             created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                             updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                             version       INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE core.restaurant_opening_hours
  ADD CONSTRAINT uq_restaurant_opening_uuid UNIQUE (uuid);

CREATE INDEX idx_opening_hours_restaurant ON core.restaurant_opening_hours(restaurant_id);

------------------------------------------------------------
-- TRIGGERS (updated_at)
------------------------------------------------------------

-- users_type
CREATE TRIGGER trg_users_type_set_updated_at
  BEFORE UPDATE ON core.users_type
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- users
CREATE TRIGGER trg_users_set_updated_at
  BEFORE UPDATE ON core.users
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- address
CREATE TRIGGER trg_address_set_updated_at
  BEFORE UPDATE ON core.address
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- users_address
CREATE TRIGGER trg_users_address_set_updated_at
  BEFORE UPDATE ON core.users_address
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant_type
CREATE TRIGGER trg_restaurant_type_set_updated_at
  BEFORE UPDATE ON core.restaurant_type
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant
CREATE TRIGGER trg_restaurant_set_updated_at
  BEFORE UPDATE ON core.restaurant
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant_address
CREATE TRIGGER trg_restaurant_address_set_updated_at
  BEFORE UPDATE ON core.restaurant_address
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- users_restaurant
CREATE TRIGGER trg_users_restaurant_set_updated_at
  BEFORE UPDATE ON core.users_restaurant
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant_items
CREATE TRIGGER trg_restaurant_items_set_updated_at
  BEFORE UPDATE ON core.restaurant_items
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant_items_images
CREATE TRIGGER trg_items_images_set_updated_at
  BEFORE UPDATE ON core.restaurant_items_images
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

-- restaurant_opening_hours
CREATE TRIGGER trg_opening_hours_set_updated_at
  BEFORE UPDATE ON core.restaurant_opening_hours
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();
