import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

interface Restaurante {
    id: number;
    nome: string;
    categoria: string;
    taxaEntrega: number;
    avaliacao: number;
}

const HomePage: React.FC = () => {
    const [restaurantes, setRestaurantes] = useState<Restaurante[]>([]);
    const [loading, setLoading] = useState(true);
    const API_BASE_URL = 'http://localhost:9090/api';

    useEffect(() => {
        const fetchRestaurantes = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`${API_BASE_URL}/restaurantes`);
                setRestaurantes(response.data.data.content);
            } catch (error) {
                console.error('Erro ao buscar restaurantes:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchRestaurantes();
    }, []);

    if (loading) {
        return <div className="text-center p-8">Carregando restaurantes...</div>;
    }

    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">Restaurantes Disponíveis</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {restaurantes.map(restaurante => (
                    <Link to={`/restaurante/${restaurante.id}`} key={restaurante.id} className="bg-white rounded-lg shadow p-4 hover:shadow-lg transition-shadow">
                        <h2 className="text-xl font-bold">{restaurante.nome}</h2>
                        <p className="text-gray-600">{restaurante.categoria}</p>
                        <div className="flex justify-between items-center mt-4">
                            <span>Taxa: R$ {restaurante.taxaEntrega.toFixed(2)}</span>
                            <span>Avaliação: {restaurante.avaliacao.toFixed(1)} ★</span>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default HomePage;
