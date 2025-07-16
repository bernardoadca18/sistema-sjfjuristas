import { Button, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';
import { Colors } from '@/constants/Colors';
import { useEffect, useState } from 'react';
import { Cliente } from '@/types/Cliente';
import { getPerfil } from '@/services/clienteService';
import { formatCpf } from '@/utils/formatCpf';
import { formatTelefone } from '@/utils/formatTelefone';
import { Link } from 'expo-router';

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
    <View style={styles.container}>
      {
          (user && userData) &&
          <>
              <Text style={styles.title}>{userData?.nomeCompleto}</Text>
              <Text style={styles.subtitle}>{userData?.email}</Text>
              <Text style={styles.subtitle}>CPF: {formatCpf(userData?.cpf as string)}</Text>
              <Text style={styles.subtitle}>Telefone: {formatTelefone(userData?.telefoneWhatsapp as string)}</Text>

          </>
      }

      <View style={styles.buttonContainer}>
          <Text style={styles.title}>{"Configurações da Conta"}</Text>
          <Link href={`/(tabs)/pix`} asChild>
              <TouchableOpacity style={styles.regularButton}>
                  <Text style={styles.regularButtonText}>Editar dados Cadastrais</Text>
              </TouchableOpacity>
          </Link>

          <Link href={`/(tabs)/pix`} asChild>
              <TouchableOpacity style={styles.regularButton}>
                  <Text style={styles.regularButtonText}>Gerenciar Chaves PIX</Text>
              </TouchableOpacity>
          </Link>

          <TouchableOpacity onPress={handleAlterarSenha} style={styles.regularButton}>
              <Text style={styles.regularButtonText}>Alterar Senha</Text>
          </TouchableOpacity>

          <TouchableOpacity onPress={signOut} style={styles.exitButton}>
              <Text style={styles.exitButtonText}>Sair da Conta</Text>
          </TouchableOpacity>
      </View>
    </View>
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
    buttonContainer: { gap: 16, alignItems: 'center', justifyContent: 'center' }
});