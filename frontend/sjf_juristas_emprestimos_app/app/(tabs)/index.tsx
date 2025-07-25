import React, { useCallback, useEffect, useState } from 'react';
import { StyleSheet, Text, ActivityIndicator, ScrollView, RefreshControl } from 'react-native';
import EmprestimoWidget from '@/components/ui/EmprestimoWidget';
import { Colors } from '@/constants/Colors';
import { getEmprestimosAtivos, getParcelasForWidget, getProximaParcela } from '@/services/emprestimoService';
import { Emprestimo, Parcela } from '@/types/Emprestimo';
import { ChavePix } from '@/types/Cliente';
import { getChavePixAtiva } from '@/services/clienteService';


const HomeScreen: React.FC = () => {
    const [emprestimo, setEmprestimo] = useState<Emprestimo | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [proximaParcela, setProximaParcela] = useState<Parcela | null>();
    const [pagamentos, setPagamentos] = useState<Parcela[] | null>();
    const [chavePixAtiva, setChavePixAtiva] = useState<ChavePix | null>(null);


    const fetchEmprestimoData = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        setEmprestimo(null);
        setProximaParcela(null);
        setPagamentos(null);

        try 
        {
            const emprestimosAtivos = await getEmprestimosAtivos();
            console.log("[DEBUG] Empréstimos recebidos:", JSON.stringify(emprestimosAtivos, null, 2));

            if (emprestimosAtivos && emprestimosAtivos.length > 0) 
            {
                const primeiroEmprestimo = emprestimosAtivos[0];
                setEmprestimo(primeiroEmprestimo);
                try 
                {
                    const parcela = await getProximaParcela(primeiroEmprestimo.id);
                    console.log("[DEBUG] Próxima parcela recebida:", JSON.stringify(parcela, null, 2));
                    setProximaParcela(parcela);
                } 
                catch (parcelaError) 
                {
                    console.log("Nenhuma próxima parcela pendente encontrada, o que é normal.\n\nErro:" + parcelaError);
                    setProximaParcela(null); 
                }

                try
                {
                  const pagamentos = await getParcelasForWidget(primeiroEmprestimo.id);
                  console.log("[DEBUG] Pagamentos recebidos:", JSON.stringify(pagamentos, null, 2));
                  setPagamentos(pagamentos);
                }
                catch (pagamentoError)
                {
                  console.log("Pagamentos não encontrados. Erro: " + pagamentoError);
                  setPagamentos(null); 
                }
            } else {
                setEmprestimo(null);
            }
        } 
        catch (e) 
        {
            setError("Não foi possível carregar os dados do empréstimo.");
            console.error(e);
        } 
        finally 
        {
            setIsLoading(false);
        }
    }, []);

    const fetchChavePixAtiva = useCallback(async () => {
        setError(null);
        setChavePixAtiva(null);
        try 
        {
           const chavePixResponse = await getChavePixAtiva();
           console.log("[DEBUG] Chave Pix Ativa Recebida: ", JSON.stringify(chavePixResponse));
           setChavePixAtiva(chavePixResponse);
        } 
        catch (e) 
        {
            setError("Não foi possível carregar os dados da chave pix.");
            console.error(e);
        } 
        finally 
        {
            setIsLoading(false);
        }
    }, []);
    /*
    const fetchParcelasEmprestimo = async (emprestimo: Emprestimo) => {
      try
      {
        setError(null);
        const parcelasEmprestimo = await getParcelas(emprestimo.id);
        console.log("[DEBUG] Dados recebidos da API:", JSON.stringify(parcelasEmprestimo, null, 2));

        if (parcelasEmprestimo && parcelasEmprestimo.length > 0)
        {
          setParcelasEmprestimo(parcelasEmprestimo);
        }
        else
        {
          setParcelasEmprestimo(null);
        }
      } 
      catch (error)
      {
        setError("Não foi possível carregar os dados das parcelas do empréstimo.");
        console.error(error);
      }
    };
    */

    useEffect(() => {
      fetchEmprestimoData();
      fetchChavePixAtiva();

    }, [fetchEmprestimoData, fetchChavePixAtiva]);

    const onRefresh = React.useCallback(() => {
      setIsLoading(true);
      fetchEmprestimoData();
      fetchChavePixAtiva();
    }, [fetchEmprestimoData, fetchChavePixAtiva])

    const renderContent = () => {
      if (isLoading)
      {
        return <ActivityIndicator size={`large`} color={Colors.light.primary} style={{ marginTop: 50 }}></ActivityIndicator>;
      }

      if (emprestimo && proximaParcela)
      {
        return <EmprestimoWidget emprestimo={emprestimo} proximaParcela={proximaParcela} pagamentos={pagamentos} chavePixAtiva={chavePixAtiva}/>;
      }

      return <Text style={styles.infoText}>Nenhum empréstimo ativo encontrado.</Text>
    }

    return (
      <ScrollView 
        style={styles.container}
        contentContainerStyle={styles.contentContainer}
        refreshControl={<RefreshControl refreshing={isLoading} onRefresh={onRefresh} />}
      >
        <Text style={styles.pageTitle}>Seu Empréstimo</Text>
        {
          renderContent()
        }
      </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.light.background,
    },
    contentContainer: {
        flexGrow: 1,
        justifyContent: 'center',
    },
    pageTitle: {
        fontSize: 28,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        textAlign: 'center',
        marginVertical: 20,
    },
    errorText: {
        textAlign: 'center',
        color: 'red',
        marginTop: 50,
        fontSize: 16,
    },
    infoText: {
        textAlign: 'center',
        color: Colors.light.textSecondary,
        marginTop: 50,
        fontSize: 16,
    },
});

export default HomeScreen;