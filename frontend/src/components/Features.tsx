import React from 'react';
import { Shield, Zap, Code, Database, Users, CheckCircle } from 'lucide-react';

const Features = () => {
  const features = [
    {
      icon: Shield,
      title: 'Segurança JWT',
      description: 'Autenticação robusta com JSON Web Tokens e controle de acesso baseado em roles.',
      color: 'bg-blue-500'
    },
    {
      icon: Zap,
      title: 'Performance',
      description: 'API otimizada com consultas eficientes e cache inteligente para máxima velocidade.',
      color: 'bg-yellow-500'
    },
    {
      icon: Code,
      title: 'Documentação Swagger',
      description: 'API completamente documentada com Swagger UI para testes e integração fácil.',
      color: 'bg-green-500'
    },
    {
      icon: Database,
      title: 'Banco Robusto',
      description: 'Estrutura de dados otimizada com JPA/Hibernate e validações em todas as camadas.',
      color: 'bg-purple-500'
    },
    {
      icon: Users,
      title: 'Multi-tenant',
      description: 'Suporte a múltiplos restaurantes e clientes com isolamento completo de dados.',
      color: 'bg-red-500'
    },
    {
      icon: CheckCircle,
      title: 'Validações',
      description: 'Validações customizadas e tratamento de erros centralizado para máxima confiabilidade.',
      color: 'bg-indigo-500'
    }
  ];

  return (
    <section className="py-20 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            Funcionalidades Enterprise
          </h2>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Uma API completa com todas as funcionalidades necessárias para um sistema de delivery moderno e escalável.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => {
            const Icon = feature.icon;
            return (
              <div
                key={index}
                className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 p-6 border border-gray-100"
              >
                <div className={`${feature.color} w-12 h-12 rounded-lg flex items-center justify-center mb-4`}>
                  <Icon className="h-6 w-6 text-white" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">
                  {feature.title}
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  {feature.description}
                </p>
              </div>
            );
          })}
        </div>

        {/* Estatísticas */}
        <div className="mt-16 bg-gradient-to-r from-orange-500 to-red-500 rounded-2xl p-8 text-white">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
            <div>
              <div className="text-3xl font-bold mb-2">20+</div>
              <div className="text-orange-100">Endpoints REST</div>
            </div>
            <div>
              <div className="text-3xl font-bold mb-2">100%</div>
              <div className="text-orange-100">Documentado</div>
            </div>
            <div>
              <div className="text-3xl font-bold mb-2">5</div>
              <div className="text-orange-100">Entidades</div>
            </div>
            <div>
              <div className="text-3xl font-bold mb-2">JWT</div>
              <div className="text-orange-100">Segurança</div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Features;