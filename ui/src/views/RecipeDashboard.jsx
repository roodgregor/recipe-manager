import React, { useState, useEffect } from 'react';
import { getAllRecipes, getRecipeById } from '../services/recipeService';
import RecipeList from '../components/RecipeList.jsx';
import RecipeForm from '../components/RecipeForm.jsx';

function RecipeDashboard() {
    const [recipes, setRecipes] = useState([]);
    const [currentRecipe, setCurrentRecipe] = useState(null);

    useEffect(() => {
        getAllRecipes()
            .then((response) => {
                setRecipes(response.data.content)
            })
            .catch((error) => console.error("Error fetching recipes:", error));
    }, []);

    // onclick search result for full payload
    const handleSelectRecipe = (leanRecipe) => {
        console.log("clicked! Recipe name: " + leanRecipe.name);
        getRecipeById(leanRecipe.id)
            .then(response => {
                console.log(currentRecipe);
                console.log(response.data);
                console.log('content: ' + response.data.content);
                setCurrentRecipe(response.data);
            })
            .catch(error => {
                console.error(`Error fetching full payload of recipe ID ${leanRecipe.id}:`, error)
            });
    };

    const handleClearForm = () => {
        setCurrentRecipe(null);
    };

    return (
        // Master Flex Container filling the viewport
        <div style={{ display: 'flex', height: '100vh', width: '100vw', margin: 0, overflow: 'hidden',
            fontFamily: 'sans-serif', backgroundColor: '#fcfcfc' }}>

            {/* Left Panel: Fixed width element in the flex line */}
            <div style={{
                flex: '0 0 350px',
                borderRight: '1px solid #e0e0e0',
                padding: '20px',
                overflowY: 'auto',
                boxSizing: 'border-box',
                backgroundColor: '#f8f9fa'
            }}>
                <RecipeList
                    recipes={recipes}
                    onSelectRecipe={handleSelectRecipe}
                    onNewRecipeClick={handleClearForm}
                />
            </div>

            {/* Right Panel: Dynamically takes up all remaining space */}
            <div style={{
                flex: '1',
                minWidth: '0px',
                padding: '30px',
                overflowY: 'auto',
                boxSizing: 'border-box'
            }}>
                <RecipeForm
                    selectedRecipe={currentRecipe}
                    onClear={handleClearForm}
                />
            </div>

        </div>
    );
}

export default RecipeDashboard;