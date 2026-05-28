--Searches would most often target to:
--name in recipes table
--tg in recipe_tags table

CREATE INDEX idx_recipe_name ON recipes(name);
CREATE INDEX idx_recipe_tags_tag ON recipe_tags(tag);