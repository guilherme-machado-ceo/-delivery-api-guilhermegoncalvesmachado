import React, { useState } from 'react';
import { Play, Copy, Check, ExternalLink } from 'lucide-react';

const ApiDemo = () => {
  const [activeTab, setActiveTab] = useState('auth');
  const [copied, setCopied] = useState('');

  const apiExamples = {
    auth: {
      title: 'Autenticação JWT',
      method: 'POST',
      endpoint: '/api/auth/login',
      request: {
        username: 'admin',
        password: 'admin123'
      },
      response: {
        accessToken: 'eyJhbGciOiJIUzUxMiJ9...',
        tokenType: 'Bearer',
        username: 'admin',
        roles: ['ROLE_ADMIN']
      }
    },
    clientes: {
      title: 'Listar Clientes',
      method: 'GET',
      endpoint: '/api/clientes',
      headers: {
        'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9...'
      },
      response: [
        {
          id: 1,
          nome: 'João Silva',
          email: 'joao@email.com',
          telefone: '11999999999',
          ativo: true
        }
      ]
    },
    restaurantes: {
      title: 'Criar Restaurante',
      method: 'POST',
      endpoint: '/api/restaurantes',
      headers: {
        'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9...',
        'Content-Type': 'application/json'
      },
      request: {
        nome: 'Pizzaria do João',
        categoria: 'Pizzaria',
        endereco: 'Rua das Pizzas, 123',
        taxaEntrega: 5.50
      },
      response: {
        id: 1,
        nome: 'Pizzaria do João',
        categoria: 'Pizzaria',
        endereco: 'Rua das Pizzas, 123',
        taxaEntrega: 5.50,
        ativo: true,
        avaliacao: null
      }
    },
    pedidos: {
      title: 'Criar Pedido',
      method: 'POST',
      endpoint: '/api/pedidos',
      headers: {
        'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9...',
        'Content-Type': 'application/json'
      },
      request: {
        clienteId: 1,
        restauranteId: 1,
        enderecoEntrega: 'Rua B, 456',
        cepEntrega: '01234-567',
        itens: [
          { produtoId: 1, quantidade: 2 },
          { produtoId: 2, quantidade: 1 }
        ]
      },
      response: {
        id: 1,
        status: 'PENDENTE',
        total: 45.90,
        taxaEntrega: 5.50,
        dataPedido: '2025-10-10T14:30:00'
      }
    }
  };

  const copyToClipboard = (text: string, type: string) => {
    navigator.clipboard.writeText(text);
    setCopied(type);
    setTimeout(() => setCopied(''), 2000);
  };

  const formatJson = (obj: any) => JSON.stringify(obj, null, 2);

  return (
    <section className="py-20 bg-gray-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Demonstração da API
          </h2>
          <p className="text-xl text-gray-300 max-w-3xl mx-auto">
            Explore os endpoints da API com exemplos reais. Teste diretamente no Swagger UI.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Tabs */}
          <div className="lg:col-span-1">
            <div className="bg-gray-800 rounded-lg p-4">
              <h3 className="text-lg font-semibold mb-4">Endpoints</h3>
              <div className="space-y-2">
                {Object.entries(apiExamples).map(([key, example]) => (
                  <button
                    key={key}
                    onClick={() => setActiveTab(key)}
                    className={`w-full text-left p-3 rounded-lg transition-colors ${
                      activeTab === key
                        ? 'bg-orange-600 text-white'
                        : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
                    }`}
                  >
                    <div className="flex items-center justify-between">
                      <span className="font-medium">{example.title}</span>
                      <span className={`text-xs px-2 py-1 rounded ${
                        example.method === 'GET' ? 'bg-green-600' :
                        example.method === 'POST' ? 'bg-blue-600' :
                        'bg-yellow-600'
                      }`}>
                        {example.method}
                      </span>
                    </div>
                  </button>
                ))}
              </div>
              
              <div className="mt-6 pt-6 border-t border-gray-700">
                <a
                  href="http://localhost:8080/swagger-ui.html"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center w-full bg-orange-600 hover:bg-orange-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors"
                >
                  <ExternalLink className="h-4 w-4 mr-2" />
                  Abrir Swagger UI
                </a>
              </div>
            </div>
          </div>

          {/* Content */}
          <div className="lg:col-span-2">
            {Object.entries(apiExamples).map(([key, example]) => (
              <div
                key={key}
                className={`${activeTab === key ? 'block' : 'hidden'}`}
              >
                <div className="bg-gray-800 rounded-lg overflow-hidden">
                  {/* Header */}
                  <div className="bg-gray-700 px-6 py-4 border-b border-gray-600">
                    <div className="flex items-center justify-between">
                      <h3 className="text-lg font-semibold">{example.title}</h3>
                      <div className="flex items-center space-x-2">
                        <span className={`text-xs px-2 py-1 rounded ${
                          example.method === 'GET' ? 'bg-green-600' :
                          example.method === 'POST' ? 'bg-blue-600' :
                          'bg-yellow-600'
                        }`}>
                          {example.method}
                        </span>
                        <code className="text-sm text-gray-300">{example.endpoint}</code>
                      </div>
                    </div>
                  </div>

                  {/* Headers */}
                  {example.headers && (
                    <div className="px-6 py-4 border-b border-gray-600">
                      <div className="flex items-center justify-between mb-2">
                        <h4 className="text-sm font-semibold text-gray-300">Headers</h4>
                        <button
                          onClick={() => copyToClipboard(formatJson(example.headers), `${key}-headers`)}
                          className="text-gray-400 hover:text-white transition-colors"
                        >
                          {copied === `${key}-headers` ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
                        </button>
                      </div>
                      <pre className="bg-gray-900 p-3 rounded text-sm text-green-400 overflow-x-auto">
                        {formatJson(example.headers)}
                      </pre>
                    </div>
                  )}

                  {/* Request */}
                  {example.request && (
                    <div className="px-6 py-4 border-b border-gray-600">
                      <div className="flex items-center justify-between mb-2">
                        <h4 className="text-sm font-semibold text-gray-300">Request Body</h4>
                        <button
                          onClick={() => copyToClipboard(formatJson(example.request), `${key}-request`)}
                          className="text-gray-400 hover:text-white transition-colors"
                        >
                          {copied === `${key}-request` ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
                        </button>
                      </div>
                      <pre className="bg-gray-900 p-3 rounded text-sm text-blue-400 overflow-x-auto">
                        {formatJson(example.request)}
                      </pre>
                    </div>
                  )}

                  {/* Response */}
                  <div className="px-6 py-4">
                    <div className="flex items-center justify-between mb-2">
                      <h4 className="text-sm font-semibold text-gray-300">Response</h4>
                      <button
                        onClick={() => copyToClipboard(formatJson(example.response), `${key}-response`)}
                        className="text-gray-400 hover:text-white transition-colors"
                      >
                        {copied === `${key}-response` ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
                      </button>
                    </div>
                    <pre className="bg-gray-900 p-3 rounded text-sm text-green-400 overflow-x-auto">
                      {formatJson(example.response)}
                    </pre>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Call to Action */}
        <div className="mt-16 text-center">
          <div className="bg-gradient-to-r from-orange-600 to-red-600 rounded-2xl p-8">
            <h3 className="text-2xl font-bold mb-4">Pronto para testar?</h3>
            <p className="text-orange-100 mb-6 max-w-2xl mx-auto">
              Acesse o Swagger UI para testar todos os endpoints interativamente. 
              Use as credenciais: admin/admin123 para acesso completo.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <a
                href="http://localhost:8080/swagger-ui.html"
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center px-6 py-3 bg-white text-orange-600 font-semibold rounded-lg hover:bg-gray-100 transition-colors"
              >
                <Play className="h-5 w-5 mr-2" />
                Testar API
              </a>
              <a
                href="https://github.com/guilherme-machado-ceo/-delivery-api-guilhermegoncalvesmachado"
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center px-6 py-3 border-2 border-white text-white font-semibold rounded-lg hover:bg-white hover:text-orange-600 transition-colors"
              >
                <ExternalLink className="h-5 w-5 mr-2" />
                Ver Código
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default ApiDemo;