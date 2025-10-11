import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { 
  Pizza, 
  Users, 
  Store, 
  Package, 
  ShoppingCart, 
  Info, 
  LayoutDashboard,
  LogIn,
  LogOut,
  User,
  ChevronDown
} from 'lucide-react';

const Header = () => {
  const location = useLocation();
  const { isAuthenticated, user, logout } = useAuth();
  const [showUserMenu, setShowUserMenu] = useState(false);

  const publicNavItems = [
    { path: '/', label: 'Home', icon: Pizza, roles: undefined },
    { path: '/about', label: 'Sobre', icon: Info, roles: undefined },
  ];

  const privateNavItems = [
    { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard, roles: undefined },
    { path: '/clientes', label: 'Clientes', icon: Users, roles: ['USER', 'ADMIN', 'MANAGER'] },
    { path: '/restaurantes', label: 'Restaurantes', icon: Store, roles: ['ADMIN', 'MANAGER'] },
    { path: '/produtos', label: 'Produtos', icon: Package, roles: ['ADMIN', 'MANAGER'] },
    { path: '/pedidos', label: 'Pedidos', icon: ShoppingCart, roles: ['USER', 'ADMIN', 'MANAGER'] },
  ];

  const hasPermission = (requiredRoles?: string[]) => {
    if (!requiredRoles || !user) return true;
    return requiredRoles.some(role => 
      user.roles.includes(`ROLE_${role.toUpperCase()}`)
    );
  };

  const handleLogout = () => {
    logout();
    setShowUserMenu(false);
  };

  const navItems = isAuthenticated ? privateNavItems : publicNavItems;

  return (
    <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <Link to="/" className="flex items-center space-x-2">
            <Pizza className="h-8 w-8 text-orange-500" />
            <span className="text-xl font-bold text-gray-900">DeliveryTech</span>
          </Link>
          
          <nav className="hidden md:flex space-x-8">
            {navItems
              .filter(item => hasPermission(item.roles))
              .map(({ path, label, icon: Icon }) => (
                <Link
                  key={path}
                  to={path}
                  className={`flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                    location.pathname === path
                      ? 'text-orange-600 bg-orange-50'
                      : 'text-gray-600 hover:text-orange-600 hover:bg-gray-50'
                  }`}
                >
                  <Icon className="h-4 w-4" />
                  <span>{label}</span>
                </Link>
              ))}
          </nav>

          <div className="flex items-center space-x-4">
            {/* GitHub Link */}
            <a
              href="https://github.com/guilherme-machado-ceo/-delivery-api-guilhermegoncalvesmachado"
              target="_blank"
              rel="noopener noreferrer"
              className="text-gray-600 hover:text-orange-600 transition-colors"
            >
              <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 0C4.477 0 0 4.484 0 10.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0110 4.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.203 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.942.359.31.678.921.678 1.856 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0020 10.017C20 4.484 15.522 0 10 0z" clipRule="evenodd" />
              </svg>
            </a>

            {/* User Menu */}
            {isAuthenticated ? (
              <div className="relative">
                <button
                  onClick={() => setShowUserMenu(!showUserMenu)}
                  className="flex items-center space-x-2 text-gray-600 hover:text-orange-600 transition-colors"
                >
                  <User className="h-5 w-5" />
                  <span className="hidden sm:block">{user?.username}</span>
                  <ChevronDown className="h-4 w-4" />
                </button>

                {showUserMenu && (
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 border border-gray-200">
                    <div className="px-4 py-2 text-sm text-gray-700 border-b border-gray-200">
                      <div className="font-medium">{user?.username}</div>
                      <div className="text-xs text-gray-500">
                        {user?.roles.map(role => role.replace('ROLE_', '')).join(', ')}
                      </div>
                    </div>
                    <Link
                      to="/dashboard"
                      className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                      onClick={() => setShowUserMenu(false)}
                    >
                      <LayoutDashboard className="h-4 w-4 mr-2" />
                      Dashboard
                    </Link>
                    <button
                      onClick={handleLogout}
                      className="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      <LogOut className="h-4 w-4 mr-2" />
                      Sair
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <Link
                to="/dashboard"
                className="flex items-center space-x-1 px-4 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700 transition-colors"
              >
                <LogIn className="h-4 w-4" />
                <span>Entrar</span>
              </Link>
            )}
          </div>
        </div>
      </div>

      {/* Mobile Menu Overlay */}
      {showUserMenu && (
        <div 
          className="fixed inset-0 z-40 md:hidden" 
          onClick={() => setShowUserMenu(false)}
        />
      )}
    </header>
  );
};

export default Header;