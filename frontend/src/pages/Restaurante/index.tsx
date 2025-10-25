import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useCart } from '../contexts/CartContext';

interface Produto {
    id: number;
    nome: string;
    descricao: string;
    preco: number;
}

const RestaurantePage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [produtos, setProdutos] = useState<Produto[]>([]);
    const [loading, setLoading] = useState(true);
    const { addToCart } = useCart();
    const API_BASE_URL = 'http://localhost:9090/api';

    useEffect(() => {
        if (id) {
            const fetchProdutos = async () => {
                try {
                    setLoading(true);
                    const response = await axios.get(`${API_BASE_URL}/produtos/restaurante/${id}`);
                    setProdutos(response.data.data);
                } catch (error) {
                    console.error('Erro ao buscar produtos:', error);
                } finally {
                    setLoading(false);
                }
            };
            fetchProdutos();
        }
    }, [id]);

    if (loading) {
        return <div className="text-center p-8">Carregando cardápio...</div>;
    }

    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">Cardápio</h1>
            <div className="space-y-4">
                {produtos.map(produto => (
                    <div key={produto.id} className="bg-white rounded-lg shadow p-4 flex justify-between items-center">
                        <div>
                            <h2 className="text-xl font-bold">{produto.nome}</h2>
                            <p className="text-gray-600">{produto.descricao}</p>
                        </div>
                        <div className="text-right">
                            <p className="font-semibold mb-2">R$ {produto.preco.toFixed(2)}</p>
                            <button
                                onClick={() => addToCart(produto)}
                                className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                            >
                                Adicionar
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default RestaurantePage;
