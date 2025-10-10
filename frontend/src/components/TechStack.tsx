import React from 'react';

const TechStack = () => {
  const technologies = {
    backend: [
      { name: 'Spring Boot', version: '3.2.2', description: 'Framework Java enterprise' },
      { name: 'Spring Security', version: '6.x', description: 'Autenticação e autorização' },
      { name: 'JPA/Hibernate', version: '6.x', description: 'ORM e persistência' },
      { name: 'JWT', version: '0.12.3', description: 'Tokens de autenticação' },
      { name: 'Swagger/OpenAPI', version: '2.2.0', description: 'Documentação da API' },
      { name: 'H2 Database', version: '2.x', description: 'Banco de dados em memória' }
    ],
    frontend: [
      { name: 'React', version: '18.2.0', description: 'Biblioteca UI' },
      { name: 'TypeScript', version: '5.2.2', description: 'Tipagem estática' },
      { name: 'Vite', version: '5.0.0', description: 'Build tool moderna' },
      { name: 'Tailwind CSS', version: '3.3.6', description: 'Framework CSS' },
      { name: 'React Router', version: '6.20.1', description: 'Roteamento SPA' },
      { name: 'Axios', version: '1.6.2', description: 'Cliente HTTP' }
    ]
  };

  return (
    <section className="py-20 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            Stack Tecnológica
          </h2>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Tecnologias modernas e confiáveis para garantir performance, segurança e escalabilidade.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Backend */}
          <div className="bg-white rounded-2xl shadow-lg p-8">
            <div className="flex items-center mb-6">
              <div className="bg-green-500 w-12 h-12 rounded-lg flex items-center justify-center mr-4">
                <svg className="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zm0 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V8zm0 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1v-2z" clipRule="evenodd" />
                </svg>
              </div>
              <h3 className="text-2xl font-bold text-gray-900">Backend</h3>
            </div>
            
            <div className="space-y-4">
              {technologies.backend.map((tech, index) => (
                <div key={index} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <h4 className="font-semibold text-gray-900">{tech.name}</h4>
                    <p className="text-sm text-gray-600">{tech.description}</p>
                  </div>
                  <span className="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded">
                    {tech.version}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Frontend */}
          <div className="bg-white rounded-2xl shadow-lg p-8">
            <div className="flex items-center mb-6">
              <div className="bg-blue-500 w-12 h-12 rounded-lg flex items-center justify-center mr-4">
                <svg className="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M3 5a2 2 0 012-2h10a2 2 0 012 2v8a2 2 0 01-2 2h-2.22l.123.489.804.804A1 1 0 0113 18H7a1 1 0 01-.707-1.707l.804-.804L7.22 15H5a2 2 0 01-2-2V5zm5.771 7H5V5h10v7H8.771z" clipRule="evenodd" />
                </svg>
              </div>
              <h3 className="text-2xl font-bold text-gray-900">Frontend</h3>
            </div>
            
            <div className="space-y-4">
              {technologies.frontend.map((tech, index) => (
                <div key={index} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <h4 className="font-semibold text-gray-900">{tech.name}</h4>
                    <p className="text-sm text-gray-600">{tech.description}</p>
                  </div>
                  <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded">
                    {tech.version}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Arquitetura */}
        <div className="mt-16 bg-white rounded-2xl shadow-lg p-8">
          <h3 className="text-2xl font-bold text-gray-900 mb-6 text-center">Arquitetura do Sistema</h3>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-orange-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-orange-600">UI</span>
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">Frontend React</h4>
              <p className="text-sm text-gray-600">Interface moderna e responsiva com TypeScript e Tailwind CSS</p>
            </div>
            
            <div className="text-center">
              <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-green-600">API</span>
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">Backend Spring</h4>
              <p className="text-sm text-gray-600">API REST robusta com Spring Boot, Security e validações</p>
            </div>
            
            <div className="text-center">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl font-bold text-purple-600">DB</span>
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">Banco de Dados</h4>
              <p className="text-sm text-gray-600">Persistência com JPA/Hibernate e H2 Database</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default TechStack;