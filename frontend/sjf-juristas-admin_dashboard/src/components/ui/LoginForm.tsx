'use client';

import { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import axios, { AxiosError } from 'axios';
import { Eye, EyeOff, Lock, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Alert, AlertDescription } from '@/components/ui/alert';
import styles from './LoginForm.module.css';
import api from '@/lib/api';

export const LoginForm = () => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const auth = useAuth();
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');
        try
        {
            const response = await api.post('/admin/auth/login', { login: login, senha: password });
            const { token, ...userData } = response.data;
            auth.login(userData);
            router.push('/dashboard');
        } 
        catch (err: unknown) 
        {
            if (err instanceof AxiosError) 
            {
                setError(err.response?.data?.message || 'Falha ao fazer login. Verifique suas credenciais.');
            } 
            else 
            {
                setError('Ocorreu um erro inesperado.');
            }
        } 
        finally 
        {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background via-muted/50 to-primary/10 p-4">
            <div className="w-full max-w-md">
                {/* Logo */}
                <div className="text-center mb-8">
                
                </div>

                {/* Título */}
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-foreground mb-2">SJF Juristas</h1>
                    <p className={`text-muted-foreground ${styles.secondaryText}`}>Dashboard Administrativo</p>
                </div>

                {/* Login */}
                <Card className="shadow-elevated gradient-card border-0">
                          <CardHeader className="text-center pb-4">
                            <CardTitle className="text-2xl font-bold text-foreground">Fazer Login</CardTitle>
                            <CardDescription className={`text-muted-foreground ${styles.secondaryText}`}>
                              Acesse o painel administrativo com suas credenciais
                            </CardDescription>
                          </CardHeader>
                          <CardContent>
                            <form onSubmit={handleSubmit} className="space-y-6">
                              {error && (
                                <Alert variant="destructive">
                                  <AlertDescription>{error}</AlertDescription>
                                </Alert>
                              )}
                
                              <div className={`space-y-2 ${styles.cardItem}`}>
                                <Label htmlFor="username" className="text-foreground font-medium">
                                  Usuário
                                </Label>
                                <div className="relative">
                                  <User className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                                  <Input
                                    id="username"
                                    type="text"
                                    placeholder="Digite seu usuário"
                                    value={login}
                                    onChange={(e) => setLogin(e.target.value)}
                                    className="pl-10 transition-colors focus:ring-2 focus:ring-primary"
                                    required
                                  />
                                </div>
                              </div>
                
                              <div className={`space-y-2 ${styles.cardItem}`}>
                                <Label htmlFor="password" className="text-foreground font-medium">
                                  Senha
                                </Label>
                                <div className="relative">
                                  <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                                  <Input
                                    id="password"
                                    type={showPassword ? 'text' : 'password'}
                                    placeholder="Digite sua senha"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="pl-10 pr-10 transition-colors focus:ring-2 focus:ring-primary"
                                    required
                                  />
                                  <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="absolute right-3 top-3 text-muted-foreground hover:text-foreground transition-colors"
                                  >
                                    {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                                  </button>
                                </div>
                              </div>
                
                              <Button 
                                type="submit" 
                                className={`w-full gradient-primary text-primary-foreground font-semibold py-3 shadow-glow hover:shadow-elevated transition-all ${styles.signInButton}`}
                                disabled={isLoading}
                                onClick={handleSubmit}
                              >
                                {isLoading ? 'Entrando...' : 'Entrar'}
                              </Button>
                
                              <div className="text-center">
                                <button
                                  type="button"
                                  className={`text-sm text-primary hover:text-primary-hover transition-colors font-medium ${styles.forgotPasswordButton}`}
                                >
                                  Esqueci minha senha
                                </button>
                              </div>
                            </form>
                          </CardContent>
                        </Card>
            </div>
        </div>
    );
};