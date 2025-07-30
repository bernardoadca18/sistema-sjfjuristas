import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { Colors } from '@/constants/Colors';
import { useEffect, useState } from 'react';
import { Cliente } from '@/types/Cliente';
import { getPerfil } from '@/services/clienteService';
import { formatCpf } from '@/utils/formatCpf';
import { formatTelefone } from '@/utils/formatTelefone';
import { Link, Stack } from 'expo-router';

export default function ProfileScreen() {
  const { signOut, user } = useAuth();
  const [userData, setUserData] = useState<Cliente | null>(null);

  const fetchUserData = async () => {
      try
      {
          const response = await getPerfil();
          setUserData(response);
          console.log(response);
      }
      catch (error) {
          console.error("Erro ao buscar dados do usuário." + error);
      }
  }

  const handleAlterarSenha = () => {

  }

  const handleEditarDadosCadastrais = () => {

  }

  useEffect(() => {
      fetchUserData();
  }, []);

  return (
    <>
        <Text style={styles.pageTitle}>Perfil</Text>
            <View style={styles.container}>
                <Stack.Screen options={{title: 'Perfil'}}/>
                {
                    (user && userData) &&
                    <View style={styles.card}>
                        <Text style={styles.title}>{userData?.nomeCompleto}</Text>
                        <Text style={styles.subtitle}>{userData?.email}</Text>
                        <Text style={styles.subtitle}>CPF: {formatCpf(userData?.cpf as string)}</Text>
                        <Text style={styles.subtitle}>Telefone: {formatTelefone(userData?.telefoneWhatsapp as string)}</Text>

                    </View>
                }

                <View style={styles.buttonContainer}>
                    <Text style={styles.title}>{"Configurações da Conta"}</Text>
                    <Link href={`/(tabs)/editar-cadastro`} asChild>
                        <TouchableOpacity style={styles.regularButton}>
                            <Text style={styles.regularButtonText}>Editar dados Cadastrais</Text>
                        </TouchableOpacity>
                    </Link>

                    <Link href={`/(tabs)/pix`} asChild>
                        <TouchableOpacity style={styles.regularButton}>
                            <Text style={styles.regularButtonText}>Gerenciar Chaves PIX</Text>
                        </TouchableOpacity>
                    </Link>

                    <Link href={`/(tabs)/atualizar-senha`} asChild>
                        <TouchableOpacity style={styles.regularButton}>
                            <Text style={styles.regularButtonText}>Alterar Senha</Text>
                        </TouchableOpacity>
                    </Link>

                    <TouchableOpacity onPress={signOut} style={styles.exitButton}>
                        <Text style={styles.exitButtonText}>Sair da Conta</Text>
                    </TouchableOpacity>
                </View>
            </View>
    </>
  );
}

const styles = StyleSheet.create({
    container: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 20 },
    title: { fontSize: 22, fontWeight: 'bold', marginBottom: 20, color: Colors.light.primary },
    subtitle: { fontSize: 16, color: 'gray', marginBottom: 40 },
    exitButton: { backgroundColor: Colors.light.red, padding: 16, borderRadius: 8, width: 320 },
    exitButtonText: { color: Colors.light.textOnPrimary, fontSize: 20, fontWeight: 'bold', textAlign: 'center' },
    regularButton: { backgroundColor: Colors.light.primary, padding: 16, borderRadius: 8, width: 320 },
    regularButtonText: { color: Colors.light.textOnPrimary, fontSize: 20, fontWeight: 'bold', textAlign: 'center' },
    buttonContainer: { gap: 16, alignItems: 'center', justifyContent: 'center' },
    card: 
    {
        backgroundColor: Colors.light.card,
        borderRadius: 16,
        padding: 32,
        marginBottom: 20,
        alignItems: 'center',
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 2
        },
        shadowOpacity: 0.1,
        shadowRadius: 3.84,
        elevation: 5,
    },
    pageTitle: {
        fontSize: 30,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        textAlign: 'center',
        marginVertical: 20,
    },

});