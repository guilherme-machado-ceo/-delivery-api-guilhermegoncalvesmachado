import React from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-hot-toast';

const RegisterPage: React.FC = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const navigate = useNavigate();

    const API_BASE_URL = 'http://localhost:9090/api';

    const onSubmit = async (data: any) => {
        try {
            await toast.promise(
                axios.post(`${API_BASE_URL}/auth/register`, {
                    nome: data.nome,
                    email: data.email,
                    senha: data.password,
                    roles: ['CLIENTE'] // Por padrão, registra como CLIENTE
                }),
                {
                    loading: 'Criando conta...',
                    success: 'Conta criada com sucesso! Você já pode fazer o login.',
                    error: 'Não foi possível criar a conta. Verifique os dados.',
                }
            );
            navigate('/login');
        } catch (error) {
            console.error('Erro no registro:', error);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8">
                <div>
                    <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                        Crie sua conta
                    </h2>
                </div>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
                    <div className="rounded-md shadow-sm -space-y-px">
                        <div>
                            <input
                                {...register('nome', { required: 'Nome é obrigatório' })}
                                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Nome completo"
                            />
                            {errors.nome && <p className="text-red-500 text-xs mt-1">{String(errors.nome.message)}</p>}
                        </div>
                        <div>
                            <input
                                {...register('email', { required: 'Email é obrigatório' })}
                                type="email"
                                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Endereço de email"
                            />
                            {errors.email && <p className="text-red-500 text-xs mt-1">{String(errors.email.message)}</p>}
                        </div>
                        <div>
                            <input
                                {...register('password', { required: 'Senha é obrigatória', minLength: { value: 6, message: 'A senha deve ter no mínimo 6 caracteres' } })}
                                type="password"
                                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Senha"
                            />
                            {errors.password && <p className="text-red-500 text-xs mt-1">{String(errors.password.message)}</p>}
                        </div>
                    </div>

                    <div>
                        <button
                            type="submit"
                            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                            Criar conta
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;
