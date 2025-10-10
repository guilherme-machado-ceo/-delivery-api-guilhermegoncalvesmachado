import React from 'react';
import Header from './components/Header';
import Footer from './components/Footer';
import Hero from './components/Hero';
import Features from './components/Features';
import ApiDemo from './components/ApiDemo';
import TechStack from './components/TechStack';
import './index.css';

function App() {
  return (
    <div className="App">
      <Header />
      <main>
        <Hero />
        <Features />
        <TechStack />
        <ApiDemo />
      </main>
      <Footer />
    </div>
  );
}

export default App;