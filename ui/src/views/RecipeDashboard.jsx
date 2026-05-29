import React, { useState, useEffect } from 'react';
import { getAllRecipes, getRecipeById } from '../services/recipeService';
import { toast } from 'sonner';
import RecipeList from '../components/RecipeList.jsx';
import RecipeForm from '../components/RecipeForm.jsx';

function RecipeDashboard() {
    const [recipes, setRecipes] = useState([]);
    const [currentRecipe, setCurrentRecipe] = useState(null);

    useEffect(() => {
        handleSearch();
    }, []);

    // onclick search result for full payload
    const handleSelectRecipe = (leanRecipe) => {
        getRecipeById(leanRecipe.id)
            .then(response => {
                setCurrentRecipe(response.data || []);
            })
            .catch(error => {
                console.error(`Error fetching full payload of recipe ID ${leanRecipe.id}:`, error)
                const errorMessage = error.response?.data?.message || 'Error fetching recipes.';
                toast.error(`Error fetching recipe ID ${leanRecipe.id}`, {
                    description: errorMessage,
                    duration: 5000
                })
            });
    };

    // applied filters search
    const handleSearch = (filterPayload) => {
        getAllRecipes(filterPayload)
            .then(response => {
                setRecipes(response.data.content || [])
            })
            .catch(error => {
                console.error('Error fetching recipes with selected payload:', error)
                const errorMessage = error.response?.data?.message || 'Error fetching recipes.';
                toast.error('Error fetching recipes with selected payload', {
                    description: errorMessage,
                    duration: 5000
                })
            });
    };

    const handleRefresh = () => {
        setCurrentRecipe(null);
        handleSearch();
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
                    onSearch={handleSearch}
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
                    onRefresh={handleRefresh}
                />
            </div>

        </div>
    );
}

export default RecipeDashboard;