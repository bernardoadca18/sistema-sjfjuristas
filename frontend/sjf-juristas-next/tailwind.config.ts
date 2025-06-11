import type { Config } from 'tailwindcss'
import { theme } from './src/config/theme';

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: theme.colors,
      keyframes: {
        'fade-down': {
          '0%': { opacity: '0', transform: 'translateY(-20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        'fade-up': {
            '0%': { opacity: '0', transform: 'translateY(20px)' },
            '100%': { opacity: '1', transform: 'translateY(0)' },
        },
      },
      animation: {
        'fade-down': 'fade-down 1s ease-out',
        'fade-up': 'fade-up 1s ease-out',
      },
      borderRadius: {
        'custom': theme.borderRadius,
      }
    },
  },
  plugins: [],
}
export default config