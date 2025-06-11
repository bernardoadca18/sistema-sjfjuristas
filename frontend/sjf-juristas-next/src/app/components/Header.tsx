import { theme } from '@/config/theme';

const Header = () => {
  return (
    <header style={{ backgroundColor: theme.colors.primary, color: theme.colors.textOnPrimary }} className="py-5 text-center shadow-md">
      <div className="container mx-auto p-8">
        <h1 className="text-4xl font-bold">SJF Juristas</h1>
      </div>
    </header>
  );
};

export default Header;