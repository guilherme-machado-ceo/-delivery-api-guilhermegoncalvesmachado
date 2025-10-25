import React, { createContext, useContext, useState, ReactNode } from 'react';
import { toast } from 'react-hot-toast';

interface Produto {
    id: number;
    nome: string;
    preco: number;
}

interface CartItem extends Produto {
    quantidade: number;
}

interface CartContextType {
    cartItems: CartItem[];
    addToCart: (produto: Produto) => void;
    removeFromCart: (produtoId: number) => void;
    clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
    const context = useContext(CartContext);
    if (context === undefined) {
        throw new Error('useCart must be used within a CartProvider');
    }
    return context;
};

interface CartProviderProps {
    children: ReactNode;
}

export const CartProvider: React.FC<CartProviderProps> = ({ children }) => {
    const [cartItems, setCartItems] = useState<CartItem[]>([]);

    const addToCart = (produto: Produto) => {
        setCartItems(prevItems => {
            const itemExists = prevItems.find(item => item.id === produto.id);
            if (itemExists) {
                return prevItems.map(item =>
                    item.id === produto.id ? { ...item, quantidade: item.quantidade + 1 } : item
                );
            }
            return [...prevItems, { ...produto, quantidade: 1 }];
        });
        toast.success(`${produto.nome} adicionado ao carrinho!`);
    };

    const removeFromCart = (produtoId: number) => {
        setCartItems(prevItems => prevItems.filter(item => item.id !== produtoId));
        toast.error('Item removido do carrinho.');
    };

    const clearCart = () => {
        setCartItems([]);
        toast.success('Carrinho esvaziado.');
    };

    const value: CartContextType = {
        cartItems,
        addToCart,
        removeFromCart,
        clearCart,
    };

    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    );
};
