import React, { useState, useEffect } from 'react';
import { getAllRecipes, getRecipeById } from '../services/recipeService';
import { toast } from 'sonner';
import RecipeList from '../components/RecipeList.jsx';
import RecipeForm from '../components/RecipeForm.jsx';

function RecipeDashboard() {
    const [recipes, setRecipes] = useState([]);
    const [currentRecipe, setCurrentRecipe] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(0);
    const [currentFilters, setCurrentFilters] = useState({});
    const PAGE_SIZE = 5;

    useEffect(() => {
        handleSearch({});
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

    const fetchRecipesHelper = (filterPayload, page) => {
        getAllRecipes(filterPayload, page, PAGE_SIZE)
            .then(response => {
                setRecipes(response.data.content || []);
                setTotalPages(response.data.totalPages || 0);
                setCurrentPage(response.data.number || 0);
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

    // applied filters search
    const handleSearch = (filterPayload) => {
        setCurrentPage(0); // Reset to page 0 on a new search
        setCurrentFilters(filterPayload);
        fetchRecipesHelper(filterPayload, 0); // call fetch recipes filter
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
        fetchRecipesHelper(currentFilters, newPage);
    }

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
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onSelectRecipe={handleSelectRecipe}
                    onSearch={handleSearch}
                    onPageChange={handlePageChange}
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