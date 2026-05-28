import axios from 'axios';

// Best done as an environment variable from AWS if real production environment
const API_BASE_URL = "http://localhost:8080/api/v1/recipes";

export const getAllRecipes = (filters = {}, page = 0, size = 25) => {
    return axios.get(API_BASE_URL, {
        params: {
            ...filters,
            page,
            size
        },
        // Axios serializes array entry as tags=a&tags=b instead of tags[]=a
        paramsSerializer: {
            indexes: null
        }
    });
};

export const getRecipeById = (id) => {
    return axios.get(`${API_BASE_URL}/${id}`);
};

export const searchRecipes = () => {

};