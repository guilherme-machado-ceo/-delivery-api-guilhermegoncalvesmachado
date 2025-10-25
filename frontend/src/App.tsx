// React import não necessário com JSX Transform
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './contexts/AuthContext';
import { CartProvider } from './contexts/CartContext';
import Header from './components/Header';
import LoginPage from './pages/Login';
import RegisterPage from './pages/Register';
import HomePage from './pages/Home';
import RestaurantePage from './pages/Restaurante';
import CartPage from './pages/Cart';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import './index.css';

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <Router>
          <div className="App">
            <Toaster
            position="top-right"
            toastOptions={{
              duration: 4000,
            }}
          />
          
          <Header />
          
          <main>
            <Routes>
              {/* Públicas */}
              <Route path="/" element={<HomePage />} />
              <Route path="/restaurante/:id" element={<RestaurantePage />} />
              <Route path="/cart" element={<CartPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              
              {/* Protegidas */}
              <Route 
                path="/dashboard" 
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                } 
              />
              
            </Routes>
          </main>
        </div>
      </Router>
    </CartProvider>
    </AuthProvider>
  );
}

export default App;
