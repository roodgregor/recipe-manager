INSERT INTO recipes (name, serving_size, cooking_time_in_minutes, description) VALUES
    ('Classic Aglio Olio (Garlic & Oil Pasta)', 2, 45, 'A staple of Italian cuisine that relies on pantry staples to create a rich, flavorful sauce.'),
    ('Lemon Herb Roast Chicken Breast', 1, 30, 'A foolproof, juicy chicken recipe that pairs well with almost any side dish.')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO recipe_ingredients (recipe_id, quantity, unit, name) VALUES
    (1, 225, 'g', 'Spaghetti'),
    (1, 60, 'ml', 'Extra Virgin Olive Oil'),
    (1, 4, 'cloves', 'Garlic'),
    (1, 1, 'teaspoon', 'Red pepper flakes'),
    (2, 2, 'pieces', 'Skinless Chicken Breasts'),
    (2, 1, 'tablespoon', 'Fresh Lemon Juice'),
    (2, 1, 'tablespoon', 'Olive Oil'),
    (2, 0.5, 'teaspoon', 'Dried Oregano'),
    (2, 0.5, 'teaspoon', 'Garlic Powder');

INSERT INTO recipe_steps (recipe_id, step_count, instruction) VALUES
    (1, 1, 'Cook the spaghetti in a large pot of salted, boiling water until it is al dente according to package directions. Reserve 60ml (1/4 cup) of pasta water before draining.'),
    (1, 2, 'In a large skillet, heat the olive oil over medium-low heat. Add the sliced garlic and cook slowly until it turns golden and fragrant (about 1-2 minutes). Be careful not to burn it.'),
    (1, 3, 'Stir in the red pepper flakes and remove the skillet from the heat.'),
    (1, 4, 'Add the drained spaghetti, the reserved pasta water, and a pinch of salt to the skillet. Toss everything together vigorously until the pasta is coated in a glossy sauce.'),
    (1, 5, 'Season with black pepper, and top with Parmesan and parsley if desired.'),
    (2, 1, 'Preheat your oven to 200°C (400°F).'),
    (2, 2, 'Pat the chicken breasts dry with a paper towel and place them in a lightly greased baking dish.'),
    (2, 3, 'Drizzle the olive oil and lemon juice evenly over the chicken.'),
    (2, 4, 'Sprinkle the oregano, garlic powder, salt, and pepper over the top.'),
    (2, 5, 'Bake for 18–22 minutes, or until the internal temperature reaches 74 C (165 F).'),
    (2, 6, 'Let the chicken rest for 5 minutes before slicing and serving.');







