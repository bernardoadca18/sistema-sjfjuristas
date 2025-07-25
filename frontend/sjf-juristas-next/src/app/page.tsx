import Header from "./components/Header";
import Hero from "./components/Hero";
import Features from "./components/Features";
import Footer from "./components/Footer";
import Chat from "./components/Chat";

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen">
      <Header/>
      <main className="flex-grow">
        <Hero />
        <Features />
        <div className={`mb-32`}></div>
        <Chat />
      </main>
      <Footer />
    </div>
  );
}