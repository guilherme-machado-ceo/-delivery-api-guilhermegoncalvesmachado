import React from 'react';
import { useCart } from '../contexts/CartContext';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-hot-toast';

const CartPage: React.FC = () => {
    const { cartItems, removeFromCart, clearCart } = useCart();
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const total = cartItems.reduce((sum, item) => sum + item.preco * item.quantidade, 0);

    const handleCheckout = () => {
        if (!isAuthenticated) {
            toast.error('Você precisa fazer login para finalizar o pedido.');
            navigate('/login');
            return;
        }
        // Lógica para chamar a API de criação de pedido
        toast.success('Pedido enviado com sucesso!');
        clearCart();
        navigate('/');
    };

    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">Carrinho de Compras</h1>
            {cartItems.length === 0 ? (
                <p>Seu carrinho está vazio.</p>
            ) : (
                <div>
                    <div className="space-y-4">
                        {cartItems.map(item => (
                            <div key={item.id} className="bg-white rounded-lg shadow p-4 flex justify-between items-center">
                                <div>
                                    <h2 className="text-xl font-bold">{item.nome}</h2>
                                    <p>Quantidade: {item.quantidade}</p>
                                </div>
                                <div className="text-right">
                                    <p className="font-semibold">R$ {(item.preco * item.quantidade).toFixed(2)}</p>
                                    <button onClick={() => removeFromCart(item.id)} className="text-red-500 text-sm mt-1">Remover</button>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 text-right">
                        <h2 className="text-2xl font-bold">Total: R$ {total.toFixed(2)}</h2>
                        <button
                            onClick={handleCheckout}
                            className="mt-4 bg-green-600 text-white px-6 py-3 rounded-md hover:bg-green-700"
                        >
                            Finalizar Pedido
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CartPage;
