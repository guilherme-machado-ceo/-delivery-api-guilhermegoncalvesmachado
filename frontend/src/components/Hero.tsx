import React from 'react';
import { ArrowRight, Play, Code, Database, Shield } from 'lucide-react';

const Hero = () => {
  return (
    <section className="bg-gradient-to-br from-orange-50 to-red-50 py-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          {/* Conteúdo Principal */}
          <div>
            <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-6">
              DeliveryTech
              <span className="block text-orange-600">API Enterprise</span>
            </h1>
            <p className="text-xl text-gray-600 mb-8 leading-relaxed">
              Sistema completo de delivery desenvolvido com Spring Boot e React. 
              Inclui autenticação JWT, documentação Swagger, validações robustas 
              e arquitetura enterprise pronta para produção.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 mb-8">
              <a
                href="/swagger-ui.html"
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center px-6 py-3 bg-orange-600 text-white font-semibold rounded-lg hover:bg-orange-700 transition-colors"
              >
                <Play className="h-5 w-5 mr-2" />
                Testar API
                <ArrowRight className="h-5 w-5 ml-2" />
              </a>
              <a
                href="https://github.com/guilherme-machado-ceo/-delivery-api-guilhermegoncalvesmachado"
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center px-6 py-3 border-2 border-gray-300 text-gray-700 font-semibold rounded-lg hover:border-orange-600 hover:text-orange-600 transition-colors"
              >
                <Code className="h-5 w-5 mr-2" />
                Ver Código
              </a>
            </div>

            {/* Estatísticas */}
            <div className="grid grid-cols-3 gap-6">
              <div className="text-center">
                <div className="text-2xl font-bold text-orange-600">20+</div>
                <div className="text-sm text-gray-600">Endpoints</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-orange-600">100%</div>
                <div className="text-sm text-gray-600">Documentado</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-orange-600">JWT</div>
                <div className="text-sm text-gray-600">Segurança</div>
              </div>
            </div>
          </div>

          {/* Demonstração Visual */}
          <div className="relative">
            <div className="bg-white rounded-2xl shadow-2xl p-6 transform rotate-3 hover:rotate-0 transition-transform duration-300">
              <div className="flex items-center justify-between mb-4">
                <div className="flex space-x-2">
                  <div className="w-3 h-3 bg-red-500 rounded-full"></div>
                  <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
                  <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                </div>
                <span className="text-sm text-gray-500">API Response</span>
              </div>
              
              <div className="bg-gray-900 rounded-lg p-4 text-green-400 font-mono text-sm">
                <div className="mb-2">
                  <span className="text-blue-400">GET</span> /api/restaurantes
                </div>
                <div className="text-gray-500">// Response</div>
                <div>{'{'}</div>
                <div className="ml-4">
                  <div><span className="text-yellow-400">"status"</span>: <span className="text-green-400">"success"</span>,</div>
                  <div><span className="text-yellow-400">"data"</span>: [</div>
                  <div className="ml-4">
                    <div>{'{'}</div>
                    <div className="ml-4">
                      <div><span className="text-yellow-400">"id"</span>: <span className="text-blue-400">1</span>,</div>
                      <div><span className="text-yellow-400">"nome"</span>: <span className="text-green-400">"Pizzaria do João"</span>,</div>
                      <div><span className="text-yellow-400">"categoria"</span>: <span className="text-green-400">"Pizzaria"</span></div>
                    </div>
                    <div>{'}'}</div>
                  </div>
                  <div>]</div>
                </div>
                <div>{'}'}</div>
              </div>
            </div>

            {/* Ícones Flutuantes */}
            <div className="absolute -top-4 -left-4 bg-orange-500 text-white p-3 rounded-full shadow-lg">
              <Database className="h-6 w-6" />
            </div>
            <div className="absolute -bottom-4 -right-4 bg-blue-500 text-white p-3 rounded-full shadow-lg">
              <Shield className="h-6 w-6" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Hero;