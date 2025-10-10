import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { 
  Users, 
  Store, 
  Package, 
  ShoppingCart, 
  TrendingUp, 
  DollarSign,
  Clock,
  CheckCircle
} from 'lucide-react';
import axios from 'axios';
import toast from 'react-hot-toast';

interface DashboardStats {
  totalClientes: number;
  totalRestaurantes: number;
  totalProdutos: number;
  totalPedidos: number;
  pedidosPendentes: number;
  pedidosEntregues: number;
  faturamentoTotal: number;
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<DashboardStats>({
    totalClientes: 0,
    totalRestaurantes: 0,
    totalProdutos: 0,
    totalPedidos: 0,
    pedidosPendentes: 0,
    pedidosEntregues: 0,
    faturamentoTotal: 0
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setIsLoading(true);
      
      // Simular dados do dashboard (em produção, viria de endpoints específicos)
      const [clientesRes, restaurantesRes, produtosRes, pedidosRes] = await Promise.allSettled([
        axios.get('/api/clientes'),
        axios.get('/api/restaurantes'),
        axios.get('/api/produtos'),
        axios.get('/api/pedidos/recentes')
      ]);

      // Processar resultados
      const clientes = clientesRes.status === 'fulfilled' ? clientesRes.value.data : [];
      const restaurantes = restaurantesRes.status === 'fulfilled' ? restaurantesRes.value.data : [];
      const produtos = produtosRes.status === 'fulfilled' ? produtosRes.value.data : [];
      const pedidos = pedidosRes.status === 'fulfilled' ? pedidosRes.value.data : [];

      setStats({
        totalClientes: Array.isArray(clientes) ? clientes.length : 0,
        totalRestaurantes: Array.isArray(restaurantes) ? restaurantes.length : 0,
        totalProdutos: Array.isArray(produtos) ? produtos.length : 0,
        totalPedidos: Array.isArray(pedidos) ? pedidos.length : 0,
        pedidosPendentes: Array.isArray(pedidos) ? pedidos.filter(p => p.status === 'PENDENTE').length : 0,
        pedidosEntregues: Array.isArray(pedidos) ? pedidos.filter(p => p.status === 'ENTREGUE').length : 0,
        faturamentoTotal: Array.isArray(pedidos) ? pedidos.reduce((sum, p) => sum + (p.total || 0), 0) : 0
      });
    } catch (error) {
      console.error('Erro ao carregar dados do dashboard:', error);
      toast.error('Erro ao carregar dados do dashboard');
    } finally {
      setIsLoading(false);
    }
  };

  const statCards = [
    {
      title: 'Total de Clientes',
      value: stats.totalClientes,
      icon: Users,
      color: 'bg-blue-500',
      bgColor: 'bg-blue-50',
      textColor: 'text-blue-600'
    },
    {
      title: 'Restaurantes',
      value: stats.totalRestaurantes,
      icon: Store,
      color: 'bg-green-500',
      bgColor: 'bg-green-50',
      textColor: 'text-green-600'
    },
    {
      title: 'Produtos',
      value: stats.totalProdutos,
      icon: Package,
      color: 'bg-purple-500',
      bgColor: 'bg-purple-50',
      textColor: 'text-purple-600'
    },
    {
      title: 'Total de Pedidos',
      value: stats.totalPedidos,
      icon: ShoppingCart,
      color: 'bg-orange-500',
      bgColor: 'bg-orange-50',
      textColor: 'text-orange-600'
    },
    {
      title: 'Pedidos Pendentes',
      value: stats.pedidosPendentes,
      icon: Clock,
      color: 'bg-yellow-500',
      bgColor: 'bg-yellow-50',
      textColor: 'text-yellow-600'
    },
    {
      title: 'Pedidos Entregues',
      value: stats.pedidosEntregues,
      icon: CheckCircle,
      color: 'bg-emerald-500',
      bgColor: 'bg-emerald-50',
      textColor: 'text-emerald-600'
    },
    {
      title: 'Faturamento Total',
      value: `R$ ${stats.faturamentoTotal.toFixed(2)}`,
      icon: DollarSign,
      color: 'bg-indigo-500',
      bgColor: 'bg-indigo-50',
      textColor: 'text-indigo-600'
    },
    {
      title: 'Crescimento',
      value: '+12%',
      icon: TrendingUp,
      color: 'bg-pink-500',
      bgColor: 'bg-pink-50',
      textColor: 'text-pink-600'
    }
  ];

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="text-gray-600 mt-2">
            Bem-vindo, {user?.username}! Aqui está um resumo do sistema.
          </p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {statCards.map((stat, index) => (
            <div key={index} className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                  <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
                </div>
                <div className={`${stat.bgColor} p-3 rounded-lg`}>
                  <stat.icon className={`h-6 w-6 ${stat.textColor}`} />
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Ações Rápidas */}
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Ações Rápidas</h2>
            <div className="grid grid-cols-2 gap-4">
              <button className="flex items-center justify-center p-4 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition-colors">
                <Users className="h-5 w-5 mr-2" />
                Novo Cliente
              </button>
              <button className="flex items-center justify-center p-4 bg-green-50 text-green-600 rounded-lg hover:bg-green-100 transition-colors">
                <Store className="h-5 w-5 mr-2" />
                Novo Restaurante
              </button>
              <button className="flex items-center justify-center p-4 bg-purple-50 text-purple-600 rounded-lg hover:bg-purple-100 transition-colors">
                <Package className="h-5 w-5 mr-2" />
                Novo Produto
              </button>
              <button className="flex items-center justify-center p-4 bg-orange-50 text-orange-600 rounded-lg hover:bg-orange-100 transition-colors">
                <ShoppingCart className="h-5 w-5 mr-2" />
                Novo Pedido
              </button>
            </div>
          </div>

          {/* Informações do Sistema */}
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Informações do Sistema</h2>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Versão da API:</span>
                <span className="font-medium">2.0.0 Enterprise</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Status:</span>
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  Online
                </span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Última atualização:</span>
                <span className="font-medium">10/10/2025</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Documentação:</span>
                <a 
                  href="http://localhost:8080/swagger-ui.html" 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="text-orange-600 hover:text-orange-700 font-medium"
                >
                  Swagger UI
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;