import axios from 'axios';

// Best done as an environment variable from AWS if real production environment
const API_BASE_URL = "http://localhost:8080/api/v1/recipes";

export const getAllRecipes = () => {
    return axios.get(API_BASE_URL);
};

export const getRecipeById = (id) => {
    return axios.get(`${API_BASE_URL}/${id}`);
};