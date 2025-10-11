import { Pizza, Github, Linkedin, Mail } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="bg-gray-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="col-span-1 md:col-span-2">
            <div className="flex items-center space-x-2 mb-4">
              <Pizza className="h-8 w-8 text-orange-500" />
              <span className="text-xl font-bold">DeliveryTech</span>
            </div>
            <p className="text-gray-300 mb-4 max-w-md">
              Sistema moderno de delivery desenvolvido com Spring Boot e React.
            </p>
          </div>
        </div>
        <div className="border-t border-gray-800 mt-8 pt-8 text-center">
          <p className="text-gray-400">© 2025 DeliveryTech</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;