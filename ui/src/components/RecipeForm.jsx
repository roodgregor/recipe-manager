import React, { useState, useEffect } from 'react';
import { createNewRecipe, updateRecipe, deleteRecipe} from '../services/recipeService';
import { toast } from 'sonner';

function RecipeForm({ selectedRecipe, onRefresh }) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [servingSize, setServingSize] = useState('');
    const [cookingTimeInMinutes, setCookingTimeInMinutes] = useState('');
    const [recipeImageUrl, setRecipeImageUrl] = useState('');
    const [updatedAt, setUpdatedAt] = useState('');
    const [ingredients, setIngredients] = useState('');
    const [steps, setSteps] = useState('');
    const [tags, setTags] = useState([]);

    const placeholderIngredients =
        '2 tbsp salt\n1 cup whole milk\n3 cloves garlic (crushed)';

    const placeholderSteps =
        'Bring a large pot of salted water to a rolling boil.' +
        '\nSauté the crushed garlic cloves in olive oil until golden fragrant.' +
        '\nToss the pasta directly into the sauce and ladle in hot pasta water.';

    const isEditMode = Boolean(selectedRecipe?.id);

    const formatDate = (rawDate) => {
        const date = new Date(rawDate);
        return new Intl.DateTimeFormat(undefined, {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            hour12: true
        }).format(date);
    };

    const clearForm = () => {
        setName('');
        setDescription('');
        setServingSize('');
        setCookingTimeInMinutes('');
        setRecipeImageUrl('')
        setUpdatedAt('');
        setIngredients('');
        setSteps('');
        setTags([]);
        onRefresh();
    };

    const handleDelete = async (e) => {
        e.preventDefault();

        toast("Delete recipe?", {
            description: "This will permanently remove your recipe.",
            action: {
                label: "Delete",
                onClick: () => {
                    toast.promise(deleteRecipe(selectedRecipe.id), {
                        loading: 'Deleting recipe...',
                        success: () => {
                            clearForm();
                            return 'Recipe deleted successfully!';
                        },
                        error: 'Failed to delete recipe.',
                    });
                }
            }
        });
    };

    // Create new recipe OR Update existing recipe
    const handleSave = async (e) => {
        e.preventDefault();

        // parse textarea payload for instructions and steps
        const lines = ingredients.split('\n').filter(line => line.trim() !== '');
        const ingredientArray = [];

        for (const line of lines) {
            const isNumericRegex = /^(\d+(\.\d+)?|\.\d+)$/;
            const parts = line.trim().split(' ');

            if (parts.length < 2 || !isNumericRegex.test(parts[0])) {
                toast.error('Error parsing Ingredients List', {
                    description: `Could not parse line '${line}'. Follow the format indicated in the note below the input field. (e.g. 2 tbsp salt)`,
                    duration: 5000
                });
                return;
            }

            ingredientArray.push({
                quantity: parts[0],
                unit: parts[1],
                name: parts.slice(2).join(' ') || ''
            });
        }

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
            recipeImageUrl,
            ingredients: ingredientArray,
            steps: stepArray,
            tags
        };
        console.log("Submitting: ", fullRecipePayload);

        try {
            let response;
            if (isEditMode) {
                // call update PUT API call
                response = await updateRecipe(selectedRecipe.id, fullRecipePayload);
                console.log('Recipe updated successfully: ', response.data);
                toast.success(`'${name}' updated successfully!`);
            } else {
                // call create POST API call
                response = await createNewRecipe(fullRecipePayload);
                console.log('Recipe created successfully: ', response.data);
                toast.success(`'${name}' created successfully!`);
                clearForm();
            }
        } catch (error) {
            console.error('Error:', error.response?.data || error.message);
            toast.error('Error creating recipe', {
                description: error.response?.data.message || error.message || 'Please verify the required fields before submitting again.',
                duration: 5000
            });
        }
    };

    useEffect(() => {
        if (selectedRecipe) {
            setName(selectedRecipe.name || '');
            setDescription(selectedRecipe.description || '');
            setServingSize(selectedRecipe.servingSize || '');
            setCookingTimeInMinutes(selectedRecipe.cookingTimeInMinutes || '');
            setRecipeImageUrl(selectedRecipe.recipeImageUrl || '');
            setUpdatedAt(selectedRecipe.updatedAt || '');
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
            clearForm();
        }
    }, [selectedRecipe]);

    return (
        <div style={{
            backgroundColor: '#ffffff',
            padding: '30px',
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
            color: '#333333' // Force dark text
        }}>
            <h3 style={{ margin: '0 0 20px 0', color: '#212529', borderBottom: '2px solid #dee2e6', paddingBottom: '10px' }}>
                {isEditMode ? `Edit Recipe: ${name}` : "Create New Recipe"}
            </h3>

            <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                {recipeImageUrl && (
                    <div>
                        <img
                            src={recipeImageUrl}
                            alt={name || "Recipe preview"}
                            style={{
                                width: '100%',
                                height: '350px',
                                maxHeight: '350px',
                                objectFit: 'cover'
                            }}
                            onError={(e) => {
                                // Fallback if the image URL is broken or returns a 404
                                e.target.src = 'https://placehold.co/600x400?text=No+Image+Found';
                            }}
                        />
                    </div>
                )}

                <div>
                    <input type="text" value=''
                           onChange={e => setRecipeImageUrl(e.target.value)}
                           className='input-style'
                           placeholder='Add/update the image URL for your recipe. Most_delicious_pasta.png'
                    />
                </div>

                {/* --- Scalar Fields Row (3 columns) --- */}
                <div style={{ display: 'flex', gap: '20px' }}>
                    <div style={{ flex: '3' }}>
                        <label className='label-style'>*Recipe Name</label>
                        <input type="text" value={name}
                               onChange={e => setName(e.target.value)}
                               className='input-style'
                               placeholder='Best Recipe Ever'
                               required
                        />
                    </div>
                    <div style={{ flex: '1' }}>
                        <label className='label-style'>*Servings</label>
                        <input type="number" value={servingSize}
                               onChange={e =>
                                   setServingSize(e.target.value)}
                               className='input-style'
                               placeholder='3'
                               required
                        />
                    </div>
                    <div style={{ flex: '1' }}>
                        <label className='label-style'>*Cooking Time (Mins)</label>
                        <input type="number" value={cookingTimeInMinutes}
                               onChange={e =>
                                   setCookingTimeInMinutes(e.target.value)}
                               className='input-style'
                               placeholder='15'
                               required
                        />
                    </div>
                </div>

                {/* --- Description Block --- */}
                <div>
                    <label className='label-style'>Description</label>
                    <textarea
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                        className='input-style'
                        placeholder='This is the best recipe in the world!'
                    />
                </div>

                <hr style={{ border: 0, borderTop: '1px solid #dee2e6', margin: '5px 0' }} />

                <div style={{ flex: '1', backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '6px' }}>
                    {tags.length === 0 ? (
                        <p style={{ fontSize: '13px', color: '#000000', fontStyle: 'italic' }}>
                            No tags found for recipe.
                        </p>
                    ) : (
                        <div className="tag-container">
                            {tags.map((tag, idx) => (
                                <span key={idx} className="tag-item">
                                  {tag.tag}
                                </span>
                            ))}
                        </div>
                    )}
                </div>

                {/* --- Dynamic Content Area (Ingredients & Steps side-by-side) --- */}
                <div style={{ display: 'flex', gap: '30px' }}>

                    {/* Ingredients Section */}
                    <div style={{ flex: '0 0 35%', backgroundColor: '#f8f9fa', padding: '15px',
                        borderRadius: '6px' }}>
                        <label className='label-style'>*Ingredients List</label>
                        <textarea
                            value={ingredients}
                            onChange={e =>
                                setIngredients(e.target.value)}
                            className='input-style textarea-style'
                            placeholder={selectedRecipe ? 'No ingredients added' : placeholderIngredients}
                            required
                        />
                        <span style={{ display: 'block', marginTop: '6px', fontSize: '12px', color: '#6c757d', fontStyle: 'italic'}}>
                            * Format: One entry per line, space separated (e.g., "2 tbsp salt").
                        </span>
                    </div>

                    {/* Instructions Section */}
                    <div style={{ flex: '1', backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '6px' }}>
                        <label className='label-style'>*Instructions</label>
                        <textarea
                            value={steps}
                            onChange={e => setSteps(e.target.value)}
                            className='input-style textarea-style'
                            placeholder={selectedRecipe ? 'No instructions added' : placeholderSteps}
                            required
                        />
                        <span style={{ display: 'block', marginTop: '6px', fontSize: '12px', color: '#6c757d', fontStyle: 'italic'}}>
                            * Format: Type your directions out naturally, hitting Enter for each new step.
                        </span>
                    </div>
                </div>

                {/* --- Form Control Actions --- */}
                <div style={{ display: 'flex', gap: '12px', marginTop: '10px', justifyContent: 'flex-start' }}>
                    <button type="submit" className='highlighted-button'>
                        {isEditMode ? "Update Recipe" : "Create Recipe"}
                    </button>
                    <button type="button" onClick={clearForm} className='highlighted-button' style={{backgroundColor: 'gray'}}>
                        Clear Fields
                    </button>
                    {isEditMode && (
                        <button type="button" onClick={handleDelete} className='highlighted-button' style={{backgroundColor: 'red'}}>
                            Delete Recipe
                        </button>
                    )}
                </div>
                {updatedAt && (
                    <label className='label-style' style=
                        {{marginRight: 'auto', fontStyle: 'italic', fontWeight: 'normal',
                        fontSize: '10px'}}>
                        Last Updated At: {formatDate(updatedAt)}
                    </label>
                )}
            </form>
        </div>
    );
}

export default RecipeForm;