import { useState, useEffect } from 'react';
import { Restaurante } from '../types/Restaurante';
import { restauranteService } from '../services/restauranteService';
import { Store, Plus, Search, Edit, ToggleLeft, ToggleRight } from 'lucide-react';
import toast from 'react-hot-toast';

const Restaurantes = () => {
  const [restaurantes, setRestaurantes] = useState<Restaurante[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadRestaurantes();
  }, []);

  const loadRestaurantes = async () => {
    try {
      setIsLoading(true);
      const data = await restauranteService.listarRestaurantes();
      setRestaurantes(data);
    } catch (error) {
      console.error('Erro ao carregar restaurantes:', error);
      toast.error('Erro ao carregar restaurantes');
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleStatus = async (restaurante: Restaurante) => {
    try {
      await restauranteService.alterarStatus(restaurante.id);
      toast.success(`Restaurante ${restaurante.ativo ? 'desativado' : 'ativado'} com sucesso!`);
      loadRestaurantes();
    } catch (error) {
      console.error('Erro ao alterar status:', error);
      toast.error('Erro ao alterar status do restaurante');
    }
  };

  const filteredRestaurantes = restaurantes.filter(restaurante =>
    restaurante.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    restaurante.categoria.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando restaurantes...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                <Store className="h-8 w-8 mr-3 text-orange-600" />
                Gestão de Restaurantes
              </h1>
              <p className="text-gray-600 mt-2">
                Gerencie todos os restaurantes parceiros
              </p>
            </div>
            <button className="flex items-center px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors">
              <Plus className="h-5 w-5 mr-2" />
              Novo Restaurante
            </button>
          </div>
        </div>

        {/* Search */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6 border border-gray-200">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar por nome ou categoria..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 outline-none"
            />
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total de Restaurantes</p>
                <p className="text-2xl font-bold text-gray-900">{restaurantes.length}</p>
              </div>
              <div className="bg-blue-50 p-3 rounded-lg">
                <Store className="h-6 w-6 text-blue-600" />
              </div>
            </div>
          </div>
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Restaurantes Ativos</p>
                <p className="text-2xl font-bold text-green-600">
                  {restaurantes.filter(r => r.ativo).length}
                </p>
              </div>
              <div className="bg-green-50 p-3 rounded-lg">
                <ToggleRight className="h-6 w-6 text-green-600" />
              </div>
            </div>
          </div>
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Avaliação Média</p>
                <p className="text-2xl font-bold text-yellow-600">
                  {restaurantes.length > 0 
                    ? (restaurantes.reduce((sum, r) => sum + r.avaliacao, 0) / restaurantes.length).toFixed(1)
                    : '0.0'
                  }
                </p>
              </div>
              <div className="bg-yellow-50 p-3 rounded-lg">
                <Store className="h-6 w-6 text-yellow-600" />
              </div>
            </div>
          </div>
        </div>

        {/* Table */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Restaurante
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Categoria
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Taxa/Avaliação
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredRestaurantes.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="px-6 py-12 text-center text-gray-500">
                      <Store className="h-12 w-12 mx-auto mb-4 text-gray-300" />
                      <p className="text-lg font-medium">Nenhum restaurante encontrado</p>
                      <p className="text-sm">
                        {searchTerm 
                          ? 'Tente ajustar os filtros de busca'
                          : 'Cadastre o primeiro restaurante para começar'
                        }
                      </p>
                    </td>
                  </tr>
                ) : (
                  filteredRestaurantes.map((restaurante) => (
                    <tr key={restaurante.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div className="flex-shrink-0 h-10 w-10">
                            <div className="h-10 w-10 rounded-full bg-orange-100 flex items-center justify-center">
                              <span className="text-orange-600 font-medium">
                                {restaurante.nome.charAt(0).toUpperCase()}
                              </span>
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {restaurante.nome}
                            </div>
                            <div className="text-sm text-gray-500">
                              {restaurante.endereco}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className="text-sm text-gray-900">{restaurante.categoria}</span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="space-y-1">
                          <div className="text-sm text-gray-900">
                            R$ {restaurante.taxaEntrega.toFixed(2)}
                          </div>
                          <div className="text-sm text-yellow-600">
                            ⭐ {restaurante.avaliacao.toFixed(1)}
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          restaurante.ativo 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {restaurante.ativo ? 'Ativo' : 'Inativo'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <div className="flex items-center space-x-2">
                          <button
                            className="text-orange-600 hover:text-orange-900 transition-colors"
                            title="Editar restaurante"
                          >
                            <Edit className="h-4 w-4" />
                          </button>
                          <button
                            onClick={() => handleToggleStatus(restaurante)}
                            className={`transition-colors ${
                              restaurante.ativo 
                                ? 'text-red-600 hover:text-red-900' 
                                : 'text-green-600 hover:text-green-900'
                            }`}
                            title={restaurante.ativo ? 'Desativar restaurante' : 'Ativar restaurante'}
                          >
                            {restaurante.ativo ? (
                              <ToggleLeft className="h-4 w-4" />
                            ) : (
                              <ToggleRight className="h-4 w-4" />
                            )}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Restaurantes;