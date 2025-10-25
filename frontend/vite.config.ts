import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// O nome do seu repositório no GitHub
const repoName = 'delivery-api-guilhermegoncalvesmachado';

export default defineConfig(({ command }) => {
  const isProduction = command === 'build';

  return {
    plugins: [react()],
    base: isProduction ? `/${repoName}/` : '/',
    build: {
      outDir: 'dist',
    },
  };
});
