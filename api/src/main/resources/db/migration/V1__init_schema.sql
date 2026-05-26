CREATE TABLE IF NOT EXISTS recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    serving_size INT,
    cooking_time_in_minutes INT,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
    -- Also wanted to add created_by/updated_by columns, but since the project doesn't have
    -- a user system, I figured it would be an additional complexity without much value at this stage
);

-- Opted to separate the table for recipe ingredients for better scalability and granularity, instead of
-- cramming the info into a TEXT column within the same table
-- Aware of the chance that recipe ingredients could duplicate a lot (e.g. a lot could have 2tbps of salt)
-- Accepting trade off for this case to avoid overengineering
CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT REFERENCES recipes(id) ON DELETE CASCADE,
    quantity NUMERIC(6,2),
    unit VARCHAR(60), -- unit of measure; grams, fl oz, pieces
    name VARCHAR(255) -- ingredient name
);

CREATE TABLE IF NOT EXISTS recipe_steps (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT REFERENCES recipes(id) ON DELETE CASCADE,
    step_count INT NOT NULL,
    instruction TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS recipe_tags (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    tag VARCHAR(50) NOT NULL
);