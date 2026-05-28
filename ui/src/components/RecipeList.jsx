import React, {useState} from 'react';

function RecipeList({ recipes, onSelectRecipe, onNewRecipeClick, onSearch }) {
    const [name, setName] = useState('');
    const [servingSize, setServingSize] = useState('');
    const [tag, setTag] = useState('');
    const [instruction, setInstruction] = useState('');
    const [hasIngredient, setHasIngredient] = useState('');
    const [excludesIngredient, setExcludesIngredient] = useState('');

    // For clarity and cleanliness, toggle UI advanced filters section
    const [showAdvanced, setShowAdvanced] = useState(false);

    const commaSeparatedToArray = (str) =>
        str ? str.split(',').map(item => item.trim()).filter(Boolean) : undefined;

    // Handler for filters
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        onSearch({
            name: name || undefined,
            servingSize: servingSize ? parseInt(servingSize, 10) : undefined,
            // Matches RecipeSearchRequest field names exactly
            tags: commaSeparatedToArray(tag),
            instructions: commaSeparatedToArray(instruction),
            includeIngredients: commaSeparatedToArray(hasIngredient),
            excludeIngredients: commaSeparatedToArray(excludesIngredient)
        });
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <h1>Recipe Manager</h1>

            <form onSubmit={handleSearchSubmit} className='search-box'>
                <div>
                    <input
                        type="text"
                        placeholder="Search your awesome recipes..."
                        className='input-style'
                        value={name}
                        onChange={e => setName(e.target.value)}
                    />
                </div>

                {/* Toggle Advanced Filters */}
                <span
                    onClick={() => setShowAdvanced(!showAdvanced)}
                    style={{ fontSize: '12px', color: '#007bff', cursor: 'pointer',
                        userSelect: 'none', textAlign: 'right' }}
                >
                    {showAdvanced ? '▴ Hide Advanced Filters' : '▾ Show Advanced Filters'}
                </span>

                {showAdvanced && (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', borderTop: '1px solid #ddd', paddingTop: '10px' }}>
                        <div>
                            <label style={{ fontSize: '11px', color: '#666' }}>Exact Serving Size</label>
                            <input type="number" value={servingSize} 
                                   onChange={e => 
                                       setServingSize(e.target.value)} className='input-style' />
                        </div>
                        <div>
                            <label style={{ fontSize: '11px', color: '#666' }}>Tag (e.g., vegetarian)</label>
                            <input type="text" value={tag} onChange={e => 
                                setTag(e.target.value)} className='input-style' />
                        </div>
                        <div>
                            <label style={{ fontSize: '11px', color: '#666' }}>Instruction Contains</label>
                            <input type="text" value={instruction}
                                   onChange={e =>
                                       setInstruction(e.target.value)}
                                   className='input-style' />
                        </div>
                        <div>
                            <label style={{ fontSize: '11px', color: '#666' }}>Includes Ingredient</label>
                            <input type="text" value={hasIngredient}
                                   onChange={e =>
                                       setHasIngredient(e.target.value)}
                                   className='input-style' />
                        </div>
                        <div>
                            <label style={{ fontSize: '11px', color: '#666' }}>Excludes Ingredient</label>
                            <input type="text" value={excludesIngredient}
                                   onChange={e =>
                                       setExcludesIngredient(e.target.value)}
                                   className='input-style' />
                        </div>
                    </div>
                )}

                <button type="submit" className='highlighted-button'>
                    Search Recipes
                </button>
            </form>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                {recipes.map((recipe) => (
                    <div
                        key={recipe.id}
                        onClick={() => onSelectRecipe(recipe)}
                        style={{
                            padding: '15px',
                            borderRadius: '6px',
                            border: '1px solid #eee',
                            cursor: 'pointer',
                            background: '#fcfcfc'
                        }}
                    >
                        <h5 style={{ margin: '0 0 5px 0', color: '#000' }}>{recipe.name}</h5>
                        <p style={{ margin: '0 0 10px 0', fontSize: '12px', color: '#000' }}>
                            {recipe.description}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default RecipeList;