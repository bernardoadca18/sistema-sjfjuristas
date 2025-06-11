import { Rocket, Gem, Lock, Smartphone } from 'lucide-react';
import { theme } from '@/config/theme';

const features = [
  {
    icon: <Rocket size={48} />,
    title: 'Agilidade',
    description: 'Processo 100% online, sem filas e sem complicação.',
  },
  {
    icon: <Gem size={48} />,
    title: 'Parcelas Flexíveis',
    description: 'Pagamento diário via PIX, adaptado à sua realidade.',
  },
  {
    icon: <Lock size={48} />,
    title: 'Segurança',
    description: 'Seus dados protegidos com a mais alta tecnologia.',
  },
  {
    icon: <Smartphone size={48} />,
    title: 'Controle Total',
    description: 'Acompanhe seu empréstimo pelo nosso aplicativo exclusivo.',
  },
];

const Features = () => {
  return (
    <section className="py-16 bg-background">
      <div className="container mx-auto px-5">
        <h3
          style={{ color: theme.colors.primary }}
          className="text-4xl font-bold text-center mb-12"
        >
          Por que escolher a SJF Juristas?
        </h3>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 text-center">
          {features.map((feature, index) => (
            <div
              style={{ color: theme.colors.primary }}
              key={index}
              className="bg-card p-8 rounded-lg shadow-md border border-border transition-transform duration-300 hover:-translate-y-2"
            >
              <div
                style={{ color: theme.colors.primary }}
                className="flex justify-center mb-4"
              >
                {feature.icon}
              </div>
              <h4
                style={{ color: theme.colors.primary }}
                className="text-xl font-bold mb-2"
              >
                {feature.title}
              </h4>
              <p style={{ color: theme.colors.textSecondary }}>
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Features;