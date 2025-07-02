import { Button, StyleSheet, Text, View } from 'react-native';
import { useAuth } from '../../context/AuthContext';

export default function ProfileScreen() {
  const { signOut, user } = useAuth();

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Perfil</Text>
      {user && <Text style={styles.subtitle}>Logado como: {user.nomeUsuario}</Text>}
      <Button title="Sair (Logout)" onPress={signOut} color="red" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 20 },
  title: { fontSize: 22, fontWeight: 'bold', marginBottom: 20 },
  subtitle: { fontSize: 16, color: 'gray', marginBottom: 40 },
});