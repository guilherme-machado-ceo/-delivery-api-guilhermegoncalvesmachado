import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Cliente, ClienteFormData } from '../types/Cliente';
import { clienteService } from '../services/clienteService';
import { X, Save, User, Mail, Phone, MapPin } from 'lucide-react';
import toast from 'react-hot-toast';

interface ClienteFormProps {
  cliente?: Cliente | null;
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

const ClienteForm: React.FC<ClienteFormProps> = ({ cliente, isOpen, onClose, onSuccess }) => {
  const [isLoading, setIsLoading] = useState(false);
  const isEditing = !!cliente;

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm<ClienteFormData>({
    defaultValues: {
      nome: '',
      email: '',
      telefone: '',
      endereco: ''
    }
  });

  useEffect(() => {
    if (cliente) {
      reset({
        nome: cliente.nome,
        email: cliente.email,
        telefone: cliente.telefone,
        endereco: cliente.endereco
      });
    } else {
      reset({
        nome: '',
        email: '',
        telefone: '',
        endereco: ''
      });
    }
  }, [cliente, reset]);

  const onSubmit = async (data: ClienteFormData) => {
    setIsLoading(true);
    
    try {
      if (isEditing && cliente) {
        await clienteService.atualizar(cliente.id, data);
        toast.success('Cliente atualizado com sucesso!');
      } else {
        await clienteService.cadastrar(data);
        toast.success('Cliente cadastrado com sucesso!');
      }
      
      onSuccess();
      onClose();
    } catch (error: any) {
      console.error('Erro ao salvar cliente:', error);
      
      if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else if (error.response?.data?.fieldErrors) {
        const fieldErrors = error.response.data.fieldErrors;
        fieldErrors.forEach((fieldError: any) => {
          toast.error(`${fieldError.field}: ${fieldError.message}`);
        });
      } else {
        toast.error('Erro ao salvar cliente');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-md w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-900">
            {isEditing ? 'Editar Cliente' : 'Novo Cliente'}
          </h2>
          <button
            onClick={handleClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
            disabled={isLoading}
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-4">
          {/* Nome */}
          <div>
            <label htmlFor="nome" className="block text-sm font-medium text-gray-700 mb-1">
              Nome Completo *
            </label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                id="nome"
                type="text"
                {...register('nome', {
                  required: 'Nome é obrigatório',
                  minLength: { value: 2, message: 'Nome deve ter pelo menos 2 caracteres' },
                  maxLength: { value: 100, message: 'Nome deve ter no máximo 100 caracteres' }
                })}
                className={`w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 outline-none transition-colors ${
                  errors.nome ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Digite o nome completo"
                disabled={isLoading}
              />
            </div>
            {errors.nome && (
              <p className="mt-1 text-sm text-red-600">{errors.nome.message}</p>
            )}
          </div>

          {/* Email */}
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email *
            </label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                id="email"
                type="email"
                {...register('email', {
                  required: 'Email é obrigatório',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: 'Email deve ter um formato válido'
                  }
                })}
                className={`w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 outline-none transition-colors ${
                  errors.email ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Digite o email"
                disabled={isLoading}
              />
            </div>
            {errors.email && (
              <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
            )}
          </div>

          {/* Telefone */}
          <div>
            <label htmlFor="telefone" className="block text-sm font-medium text-gray-700 mb-1">
              Telefone
            </label>
            <div className="relative">
              <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                id="telefone"
                type="tel"
                {...register('telefone', {
                  pattern: {
                    value: /^\d{10,11}$/,
                    message: 'Telefone deve conter 10 ou 11 dígitos'
                  }
                })}
                className={`w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 outline-none transition-colors ${
                  errors.telefone ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="11999999999"
                disabled={isLoading}
              />
            </div>
            {errors.telefone && (
              <p className="mt-1 text-sm text-red-600">{errors.telefone.message}</p>
            )}
          </div>

          {/* Endereço */}
          <div>
            <label htmlFor="endereco" className="block text-sm font-medium text-gray-700 mb-1">
              Endereço *
            </label>
            <div className="relative">
              <MapPin className="absolute left-3 top-3 h-5 w-5 text-gray-400" />
              <textarea
                id="endereco"
                {...register('endereco', {
                  required: 'Endereço é obrigatório',
                  maxLength: { value: 255, message: 'Endereço deve ter no máximo 255 caracteres' }
                })}
                rows={3}
                className={`w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 outline-none transition-colors resize-none ${
                  errors.endereco ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Digite o endereço completo"
                disabled={isLoading}
              />
            </div>
            {errors.endereco && (
              <p className="mt-1 text-sm text-red-600">{errors.endereco.message}</p>
            )}
          </div>

          {/* Buttons */}
          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={handleClose}
              className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
              disabled={isLoading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 focus:ring-2 focus:ring-orange-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center"
            >
              {isLoading ? (
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
              ) : (
                <Save className="h-4 w-4 mr-2" />
              )}
              {isLoading ? 'Salvando...' : 'Salvar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ClienteForm;