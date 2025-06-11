import { theme } from '@/config/theme';

const Footer = () => {
  return (
    <footer className="bg-card text-center py-5 mt-10 border-t" style={{borderColor: theme.colors.border, color: theme.colors.textSecondary}}>
      <div className="container mx-auto px-5">
        <p>© 2025 SJF Juristas. Todos os direitos reservados.</p>
        <p className="mt-2">
          <a href="#" className="hover:underline" style={{color: theme.colors.primary}}>Termos de Uso</a> | <a href="#" className="hover:underline" style={{color: theme.colors.primary}}>Política de Privacidade</a>
        </p>
      </div>
    </footer>
  );
};

export default Footer;