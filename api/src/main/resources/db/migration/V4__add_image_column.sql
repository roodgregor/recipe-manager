ALTER TABLE recipes
ADD COLUMN IF NOT EXISTS recipe_image TEXT;

UPDATE recipes SET recipe_image = 'https://www.foodandwine.com/thmb/KXJgXSgNZ1tUKFxZJ1bYXP7D668=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/pasta-aglio-e-olio-FT-RECIPE0324-e53bdb7ca42d4377ab4f014a2b58ff4e.jpg'
WHERE id = 1;

UPDATE recipes SET recipe_image = 'https://www.seriouseats.com/thmb/32jSN6iigPUrGq1iPJymu0HNXrs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/20241101-SEA-OneSkilletLemonChicken-MorganHuntGlaze-Hero2-10-109b1c056c1247f9b676698b408529d0.jpg'
WHERE id = 2;