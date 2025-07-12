import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, Alert, ActivityIndicator } from 'react-native';
import { Stack, useLocalSearchParams, useRouter } from 'expo-router';

import { gerarPixParaPagamento, anexarComprovante } from '@/services/pagamentoService';
import { Parcela } from '@/types/Emprestimo';
import formatCurrency from '@/utils/formatCurrency';
import formatDate from '@/utils/formatDate';
import * as DocumentPicker from 'expo-document-picker';
import * as Clipboard from 'expo-clipboard';
import { getParcelaById } from '@/services/emprestimoService';

const PaymentScreen = () => {
    const { id } = useLocalSearchParams<{ id: string }>();
    const router = useRouter();
    const [parcela, setParcela] = useState<Parcela | null>(null);
    const [loading, setLoading] = useState(true);
    const [pixLoading, setPixLoading] = useState(false);
    const [uploadLoading, setUploadLoading] = useState(false);

    useEffect(() => {
      const fetchParcela = async () => {
          try
          {
              if (id)
              {
                  const data = await getParcelaById(id);
                  setParcela(data);
              }
          }
          catch (error)
          {
              console.error('Erro ao buscar parcela: ', error);
              Alert.alert('Erro', 'Não foi possível carregar os detalhes da parcela.');
          }
          finally
          {
              setLoading(false);
          }
      }
      fetchParcela();
    }, [id]);

    const handleGerarPix = async () => {
        setPixLoading(true);
        try
        {
            if (parcela)
            {
              const pixData = await gerarPixParaPagamento(parcela.id);
              setParcela(pixData);
            }
        }
        catch (error)
        {
            console.error('Erro ao gerar PIX:', error);
            Alert.alert('Erro', 'Não foi possível gerar o código PIX para pagamento.');
        } 
        finally
        {
            setPixLoading(false);
        }
    }

    const handleAnexarComprovante = async () => {
        try 
        {
            const result = await DocumentPicker.getDocumentAsync(
            {
                type: ['image/*', 'application/pdf'],
            });

            if (!result.canceled && result.assets && result.assets.length > 0)
            {
                setUploadLoading(true);
                const asset = result.assets[0];
                const response = await fetch(asset.uri);
                const blob = await response.blob();
                
                if (parcela) {
                    await anexarComprovante(parcela.id, blob);
                    Alert.alert('Sucesso', 'Comprovante enviado com sucesso! Aguarde a confirmação.');
                    router.back();
                }
            }
        }
        catch (error)
        {
            console.error('Erro ao anexar comprovante:', error);
            Alert.alert('Erro', 'Não foi possível enviar o comprovante.');
        }
        finally
        {
            setUploadLoading(false);
        }
    };

    const copyToClipboard = async () => {
        if (parcela?.pixCopiaCola)
        {
            await Clipboard.setStringAsync(parcela.pixCopiaCola);
            Alert.alert('Copiado!', 'O código PIX foi copiado para a área de transferência.');
        }
    };

    if (loading)
    {
        return <ActivityIndicator style={{ flex: 1, justifyContent: 'center' }} size="large" />;
    }

    if (!parcela)
    {
        return <Text style={styles.errorText}>Parcela não encontrada.</Text>;
    }


    return (
      <View style={styles.container}>
          <Stack.Screen options={{ title: `Pagar Parcela ${parcela.numeroParcela}` }} />
          <View style={styles.card}>

              <Text style={styles.title}>Detalhes da Parcela</Text>

              <Text style={styles.detailText}>Valor: <Text style={styles.boldText}>{formatCurrency(parcela.valorTotalParcela)}</Text></Text>
              <Text style={styles.detailText}>Vencimento: <Text style={styles.boldText}>{formatDate(parcela.dataVencimento)}</Text></Text>
              <Text style={styles.detailText}>Status: <Text style={styles.boldText}>{parcela.statusPagamentoParcelaNome}</Text></Text>

              {
                  !parcela.pixCopiaCola ? (
                      <TouchableOpacity style={[styles.button, styles.primaryButton]} onPress={handleGerarPix} disabled={pixLoading}>
                          {
                              pixLoading ? <ActivityIndicator color={"#fff"} /> : <Text style={styles.buttonText}>Gerar PIX para Pagamento</Text>
                          }
                      </TouchableOpacity>
                  ) : (
                      <View style={styles.pixContainer}>
                          <Text style={styles.pixTitle}>
                              Pague com PIX
                          </Text>
                          {
                              parcela.pixQrCodeBase64 && (
                                  <Image
                                      style={styles.qrCode}
                                      source={{ uri: `data:image/png;base64,${parcela.pixQrCodeBase64}` }}
                                  />
                              )
                          }
                          <TouchableOpacity style={styles.copyButton} onPress={copyToClipboard}>
                              <Text style={styles.copyButtonText} numberOfLines={2}>{parcela.pixCopiaCola}</Text>
                          </TouchableOpacity>

                          <TouchableOpacity style={[styles.button, styles.secondaryButton]} onPress={handleAnexarComprovante} disabled={uploadLoading}>
                              {
                                  uploadLoading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>Anexar Comprovante</Text>
                              }
                          </TouchableOpacity>
                      </View>
                  )
              }

          </View>
      </View>
    );
};

const styles = StyleSheet.create({
    container: 
    {
        flex: 1,
        padding: 16,
        backgroundColor: '#f0f4f7',
    },
    card: 
    {
        backgroundColor: '#fff',
        borderRadius: 8,
        padding: 16,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },
    title: 
    {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 16,
        textAlign: 'center',
    },
    detailText: 
    {
        fontSize: 16,
        marginBottom: 8,
    },
    boldText: 
    {
        fontWeight: 'bold',
    },
    errorText: 
    {
        flex: 1,
        textAlign: 'center',
        marginTop: 20,
        fontSize: 16,
    },
    button: 
    {
        marginTop: 20,
        padding: 15,
        borderRadius: 8,
        alignItems: 'center',
    },
    primaryButton: 
    {
        backgroundColor: '#DAA520',
    },
    secondaryButton: 
    {
        backgroundColor: '#333',
    },
    buttonText: 
    {
        color: '#fff',
        fontWeight: 'bold',
        fontSize: 16,
    },
    pixContainer: 
    {
        alignItems: 'center',
        marginTop: 20,
        borderTopWidth: 1,
        borderTopColor: '#eee',
        paddingTop: 20,
    },
    pixTitle: 
    {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    qrCode: 
    {
        width: 200,
        height: 200,
        marginVertical: 16,
    },
    copyButton: 
    {
        padding: 10,
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 5,
        marginBottom: 16,
        width: '100%',
    },
    copyButtonText: 
    {
        textAlign: 'center',
        color: '#555',
    }
});

export default PaymentScreen;
