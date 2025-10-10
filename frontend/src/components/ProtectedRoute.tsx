import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import LoginForm from './LoginForm';
import { Shield, AlertCircle } from 'lucide-react';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRoles?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRoles = [] }) => {
  const { isAuthenticated, user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md w-full">
          <LoginForm />
        </div>
      </div>
    );
  }

  // Verificar se o usuário tem as roles necessárias
  if (requiredRoles.length > 0 && user) {
    const hasRequiredRole = requiredRoles.some(role => 
      user.roles.includes(`ROLE_${role.toUpperCase()}`)
    );

    if (!hasRequiredRole) {
      return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
          <div className="max-w-md w-full text-center">
            <div className="bg-white rounded-lg shadow-lg p-6">
              <div className="mx-auto w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mb-4">
                <AlertCircle className="h-6 w-6 text-red-600" />
              </div>
              <h2 className="text-2xl font-bold text-gray-900 mb-2">Acesso Negado</h2>
              <p className="text-gray-600 mb-4">
                Você não tem permissão para acessar esta página.
              </p>
              <p className="text-sm text-gray-500">
                Roles necessárias: {requiredRoles.join(', ')}
              </p>
              <p className="text-sm text-gray-500">
                Suas roles: {user.roles.map(role => role.replace('ROLE_', '')).join(', ')}
              </p>
            </div>
          </div>
        </div>
      );
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;