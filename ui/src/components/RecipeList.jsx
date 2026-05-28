import React from 'react';

function RecipeList({ recipes, onSelectRecipe, onNewRecipeClick }) {
    return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <button
                onClick={onNewRecipeClick}
                style={{
                    padding: '12px',
                    backgroundColor: '#28a745',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer',
                    fontWeight: 'bold',
                    fontSize: '15px'
                }}
            >
                Create New Recipe +
            </button>

            <input
                type="text"
                placeholder="Search your awesome recipes..."
                style={{ padding: '10px', background: '#fcfcfc', borderRadius: '4px',
                    fontSize: '14px', border: '1px solid #ccc' }}
            />

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
                            background: '#fcfcfc',
                            transition: 'background 0.2s'
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