import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './contexts/AuthContext';
import Header from './components/Header';
import Footer from './components/Footer';
import Hero from './components/Hero';
import Features from './components/Features';
import ApiDemo from './components/ApiDemo';
import TechStack from './components/TechStack';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import './index.css';

// Landing Page Component
const LandingPage = () => (
  <>
    <Hero />
    <Features />
    <TechStack />
    <ApiDemo />
  </>
);

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Toaster 
            position="top-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: '#363636',
                color: '#fff',
              },
              success: {
                duration: 3000,
                iconTheme: {
                  primary: '#10B981',
                  secondary: '#fff',
                },
              },
              error: {
                duration: 4000,
                iconTheme: {
                  primary: '#EF4444',
                  secondary: '#fff',
                },
              },
            }}
          />
          
          <Header />
          
          <main>
            <Routes>
              {/* Landing Page */}
              <Route path="/" element={<LandingPage />} />
              
              {/* Dashboard - Protegido */}
              <Route 
                path="/dashboard" 
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                } 
              />
              
              {/* Clientes - Protegido */}
              <Route 
                path="/clientes" 
                element={
                  <ProtectedRoute requiredRoles={['USER', 'ADMIN', 'MANAGER']}>
                    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                      <div className="text-center">
                        <h1 className="text-2xl font-bold text-gray-900 mb-4">Gestão de Clientes</h1>
                        <p className="text-gray-600">Em desenvolvimento...</p>
                      </div>
                    </div>
                  </ProtectedRoute>
                } 
              />
              
              {/* Restaurantes - Protegido */}
              <Route 
                path="/restaurantes" 
                element={
                  <ProtectedRoute requiredRoles={['ADMIN', 'MANAGER']}>
                    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                      <div className="text-center">
                        <h1 className="text-2xl font-bold text-gray-900 mb-4">Gestão de Restaurantes</h1>
                        <p className="text-gray-600">Em desenvolvimento...</p>
                      </div>
                    </div>
                  </ProtectedRoute>
                } 
              />
              
              {/* Produtos - Protegido */}
              <Route 
                path="/produtos" 
                element={
                  <ProtectedRoute requiredRoles={['ADMIN', 'MANAGER']}>
                    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                      <div className="text-center">
                        <h1 className="text-2xl font-bold text-gray-900 mb-4">Gestão de Produtos</h1>
                        <p className="text-gray-600">Em desenvolvimento...</p>
                      </div>
                    </div>
                  </ProtectedRoute>
                } 
              />
              
              {/* Pedidos - Protegido */}
              <Route 
                path="/pedidos" 
                element={
                  <ProtectedRoute requiredRoles={['USER', 'ADMIN', 'MANAGER']}>
                    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                      <div className="text-center">
                        <h1 className="text-2xl font-bold text-gray-900 mb-4">Gestão de Pedidos</h1>
                        <p className="text-gray-600">Em desenvolvimento...</p>
                      </div>
                    </div>
                  </ProtectedRoute>
                } 
              />
              
              {/* Sobre */}
              <Route 
                path="/about" 
                element={
                  <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                    <div className="text-center max-w-2xl mx-auto px-4">
                      <h1 className="text-3xl font-bold text-gray-900 mb-4">Sobre o DeliveryTech</h1>
                      <p className="text-gray-600 mb-6">
                        Sistema completo de delivery desenvolvido por Guilherme Gonçalves Machado 
                        utilizando Spring Boot, React, TypeScript e as melhores práticas de desenvolvimento.
                      </p>
                      <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                        <h2 className="text-xl font-semibold mb-4">Tecnologias Utilizadas</h2>
                        <div className="grid grid-cols-2 gap-4 text-left">
                          <div>
                            <h3 className="font-medium text-gray-900">Backend</h3>
                            <ul className="text-sm text-gray-600 mt-2 space-y-1">
                              <li>• Spring Boot 3.2.2</li>
                              <li>• Spring Security + JWT</li>
                              <li>• Spring Data JPA</li>
                              <li>• Swagger/OpenAPI</li>
                              <li>• Bean Validation</li>
                            </ul>
                          </div>
                          <div>
                            <h3 className="font-medium text-gray-900">Frontend</h3>
                            <ul className="text-sm text-gray-600 mt-2 space-y-1">
                              <li>• React 18 + TypeScript</li>
                              <li>• Tailwind CSS</li>
                              <li>• React Router</li>
                              <li>• Axios</li>
                              <li>• React Hook Form</li>
                            </ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                } 
              />
            </Routes>
          </main>
          
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;