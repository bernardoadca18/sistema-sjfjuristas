"use client";

import { theme } from '@/config/theme';


const Hero = () => {

  const handleScrollToChat = () => {
    const chatSection = document.getElementById('chat-section');
    if (chatSection) {
      chatSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  };

  return (
    <section
      style={{
        background: `linear-gradient(to right, ${theme.colors.primaryDark}, ${theme.colors.primary})`,
        color: theme.colors.textOnPrimary
      }}
      className="py-16 px-8 text-center"
    >
      <h2 className="text-4xl font-bold mb-8 animate-fade-down">
        Dinheiro Rápido e Fácil para Realizar Seus Sonhos!
      </h2>
      <p className="text-lg mb-10 animate-fade-up">
        Solicite seu empréstimo online sem burocracia e com aprovação rápida.
      </p>
      <button
        onClick={handleScrollToChat}
        style={{ backgroundColor: theme.colors.accent, color: theme.colors.textOnAccent }}
        className="inline-block px-8 py-4 rounded-full font-bold text-lg transition-transform duration-300 hover:scale-105 animate-pulse"
      >
        Solicitar Agora!
      </button>
    </section>
  );
};

export default Hero;