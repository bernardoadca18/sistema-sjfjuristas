import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#DAA520',
        primaryDark: '#B8860B',
        primaryLight: '#FAFAD2',
        accent: '#FFD700',
        background: '#F8F9FA',
        card: '#FFFFFF',
        text: '#212529',
        textSecondary: '#6c757d',
        textOnPrimary: '#FFFFFF',
        textOnAccent: '#3B2700',
        border: '#DEE2E6',
      },
    },
  },
  plugins: [],
};
export default config;