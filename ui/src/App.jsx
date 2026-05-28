import React from 'react';
import RecipeDashboard from './views/RecipeDashboard';
import { Toaster } from 'sonner';

function App() {
  return (
      <div className="App">
          <Toaster position="top-right" richColors />
          <RecipeDashboard />
      </div>
  );
}

export default App
