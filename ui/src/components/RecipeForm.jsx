import React, { useState, useEffect } from 'react';

function RecipeForm({ selectedRecipe, onClear }) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [servingSize, setServingSize] = useState('');
    const [cookingTimeInMinutes, setCookingTimeInMinutes] = useState('');
    const [ingredients, setIngredients] = useState('');
    const [steps, setSteps] = useState('');
    const [tags, setTags] = useState([]);

    const placeholderIngredients =
        '2 tbsp salt\n1 cup whole milk\n3 cloves garlic (crushed)';

    const placeholderSteps =
        'Bring a large pot of salted water to a rolling boil.' +
        '\nSauté the crushed garlic cloves in olive oil until golden fragrant.' +
        '\nToss the pasta directly into the sauce and ladle in hot pasta water.';

    useEffect(() => {
        if (selectedRecipe) {
            setName(selectedRecipe.name || '');
            setDescription(selectedRecipe.description || '');
            setServingSize(selectedRecipe.servingSize || '');
            setCookingTimeInMinutes(selectedRecipe.cookingTimeInMinutes || '');
            // setIngredients(selectedRecipe.ingredients || '');
            // setSteps(selectedRecipe.steps || '');
            setTags(selectedRecipe.tags || []);

            // Map Ingredients array to textarea
            if (selectedRecipe.ingredients && selectedRecipe.ingredients.length > 0) {
                const ingredientLines = selectedRecipe.ingredients.map(ing =>
                    `${ing.quantity || ''} ${ing.unit || ''} ${ing.name || ''}`.trim()
                );
                setIngredients(ingredientLines.join('\n'));
            } else {
                setIngredients('');
            }

            // Map instruction steps array to textarea
            if (selectedRecipe.steps && selectedRecipe.steps.length > 0) {
                const sortedSteps = [...selectedRecipe.steps].sort((a, b) => a.stepCount - b.stepCount);
                const stepLines = sortedSteps.map(s => s.instruction);
                setSteps(stepLines.join('\n'));
            } else {
                setSteps('');
            }
        } else {
            setName('');
            setDescription('');
            setServingSize('');
            setCookingTimeInMinutes('');
            setIngredients('');
            setSteps('');
            setTags([]);
        }
    }, [selectedRecipe]);

    const handleSave = (e) => {
        e.preventDefault();

        // parse textarea payload for instructions and steps
        const ingredientArray = ingredients.split('\n')
            .filter(line => line.trim() !== '')
            .map(line => {
                const parts = line.trim().split(' ');
                return {
                    quantity: parts[0] || '',
                    unit: parts[1] || '',
                    name: parts.slice(2).join(' ') || ''
                };
            });

        const stepArray = steps.split('\n')
            .filter(line => line.trim() !== '')
            .map((line, idx) => (
                {
                    stepCount: idx + 1,
                    instruction: line.trim() //already trimmed in filter
                }
            ));

        const fullRecipePayload = {
            name,
            description,
            servingSize,
            cookingTimeInMinutes,
            ingredients: ingredientArray,
            steps: stepArray,
            tags
        };
        console.log("Submitting:", fullRecipePayload);
    };

    return (
        <div style={{
            backgroundColor: '#ffffff',
            padding: '30px',
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
            color: '#333333' // Force dark text
        }}>
            <h3 style={{ margin: '0 0 20px 0', color: '#212529', borderBottom: '2px solid #dee2e6', paddingBottom: '10px' }}>
                {selectedRecipe?.id ? `Edit Recipe: ${name}` : "Create New Recipe"}
            </h3>

            <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>

                {/* --- Scalar Fields Row (3 columns) --- */}
                <div style={{ display: 'flex', gap: '20px' }}>
                    <div style={{ flex: '2' }}>
                        <label className='labelStyle'>Recipe Name</label>
                        <input type="text" value={name}
                               onChange={e => setName(e.target.value)}
                               className='inputStyle' required
                               placeholder='Best Recipe Ever'
                        />
                    </div>
                    <div style={{ flex: '1' }}>
                        <label className='labelStyle'>Servings</label>
                        <input type="number" value={servingSize}
                               onChange={e =>
                                   setServingSize(e.target.value)}
                               className='inputStyle'
                               placeholder='3'
                        />
                    </div>
                    <div style={{ flex: '1' }}>
                        <label className='labelStyle'>Cooking Time (Mins)</label>
                        <input type="number" value={cookingTimeInMinutes}
                               onChange={e =>
                                   setCookingTimeInMinutes(e.target.value)}
                               className='inputStyle'
                               placeholder='15'
                        />
                    </div>
                </div>

                {/* --- Description Block --- */}
                <div>
                    <label className='labelStyle'>Description</label>
                    <textarea
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                        className='inputStyle'
                        placeholder='This is the best recipe in the world!'
                    />
                </div>

                <hr style={{ border: 0, borderTop: '1px solid #dee2e6', margin: '5px 0' }} />

                {/* --- Dynamic Content Area (Ingredients & Steps side-by-side) --- */}
                <div style={{ display: 'flex', gap: '30px' }}>

                    {/* Ingredients Section */}
                    <div style={{ flex: '1', backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '6px' }}>
                        <label className='labelStyle'>Ingredients List</label>
                        <textarea
                            value={ingredients}
                            onChange={e => setIngredients(e.target.value)}
                            className='inputStyle textareaStyle'
                            placeholder={selectedRecipe ? 'No ingredients added' : placeholderIngredients}
                        />
                        <span style={{ display: 'block', marginTop: '6px', fontSize: '12px', color: '#6c757d', fontStyle: 'italic'}}>
                            * Format: One entry per line, space separated (e.g., "2 tbsp salt").
                        </span>
                    </div>

                    {/* Instructions Section */}
                    <div style={{ flex: '1', backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '6px' }}>
                        <label className='labelStyle'>Instructions</label>
                        <textarea
                            value={steps}
                            onChange={e => setSteps(e.target.value)}
                            className='inputStyle textareaStyle'
                            placeholder={selectedRecipe ? 'No instructions added' : placeholderSteps}
                        />
                        <span style={{ display: 'block', marginTop: '6px', fontSize: '12px', color: '#6c757d', fontStyle: 'italic'}}>
                            * Format: Type your directions out naturally, hitting Enter for each new step.
                        </span>
                    </div>
                </div>

                <div style={{ flex: '1', backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '6px', width: '35%' }}>
                    <h4 style={{ margin: '0 0 10px 0', color: '#495057' }}>Tags</h4>
                    {tags.length === 0 ? (
                        <p style={{ fontSize: '13px', color: '#6c757d', fontStyle: 'italic' }}>No tags found for recipe.</p>
                    ) : (
                        <div>
                            {tags.map((tag, idx) => (
                                <p key={idx}>{tag.tag}</p>
                            ))}
                        </div>
                    )}
                </div>

                {/* --- Form Control Actions --- */}
                <div style={{ display: 'flex', gap: '12px', marginTop: '10px', justifyContent: 'flex-start' }}>
                    <button type="submit" style={{
                        padding: '10px 24px',
                        backgroundColor: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '6px',
                        fontWeight: 'bold',
                        cursor: 'pointer'
                    }}>
                        {selectedRecipe?.id ? "Update Recipe" : "Create Recipe"}
                    </button>
                    <button type="button" onClick={onClear} style={{
                        padding: '10px 24px',
                        backgroundColor: '#6c757d',
                        color: 'white',
                        border: 'none',
                        borderRadius: '6px',
                        fontWeight: 'bold',
                        cursor: 'pointer'
                    }}>
                        Clear Fields
                    </button>
                </div>
            </form>
        </div>
    );
}

export default RecipeForm;