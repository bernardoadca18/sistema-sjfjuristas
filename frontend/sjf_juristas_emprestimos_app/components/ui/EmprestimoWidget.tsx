import React from "react";
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from "react-native";
import { Colors } from "@/constants/Colors";
import { Emprestimo, Parcela } from "@/types/Emprestimo";
import { Link } from 'expo-router';
import formatDate from "@/utils/formatDate";
import formatCurrency from "@/utils/formatCurrency";
import PagamentoWidget from "./PagamentoWidget";
import { ChavePix } from "@/types/Cliente";
import { ChavePixItem } from "../ChavePixItem";

interface EmprestimoWidgetProps {
    emprestimo: Emprestimo;
    proximaParcela?: Parcela;
    pagamentos?: Parcela[] | null;
    chavePixAtiva?: ChavePix | null;
    isSummary?: boolean;
    showPixMenu?: boolean;
}

const EmprestimoWidget : React.FC<EmprestimoWidgetProps> = ( { emprestimo, proximaParcela, pagamentos, chavePixAtiva, showPixMenu=true } ) => {
    return (
        <ScrollView style={styles.container}>
            <View style={styles.card}>
                <Text style={styles.titleText}>Valor do Empréstimo</Text>
                <Text style={styles.valueText}>{formatCurrency(emprestimo.valorLiberado)}</Text>
            </View>
            <View style={styles.card}>
                <Text style={styles.titleText}>Saldo Devedor</Text>
                <Text style={styles.valueText}>{formatCurrency(emprestimo.saldoDevedorAtual)}</Text>
            </View>
            <View style={styles.card}>
                <Text style={styles.titleText}>Próximo Pagamento</Text>
                <Text style={styles.valueText}>
                    {
                        proximaParcela ? formatCurrency(proximaParcela.valorTotalParcela) : '-'
                    }
                </Text>
                {
                    proximaParcela ? (
                        <Text style={styles.otherText}>Data de Vencimento:
                            <Text style={styles.dateText}>
                                {` ${formatDate(proximaParcela.dataVencimento)}`}
                            </Text>
                        </Text>
                    ) : (<></>)
                }
                
                
            </View>

            {
                proximaParcela && (
                    <Link href={`/payment/${proximaParcela?.id}`} asChild>
                        <TouchableOpacity style={styles.button}>
                            <Text style= {styles.buttonText}>Fazer Pagamento</Text>
                        </TouchableOpacity>
                    </Link>
                )
            }

            <Link href={{
                pathname: "/loan-details/[id]",
                params: { id : emprestimo.id }
            }as any} asChild>
                <TouchableOpacity style={styles.buttonSecondary}>
                    <Text style= {styles.linkText}>Ver Detalhes do Empréstimo</Text>
                </TouchableOpacity>
            </Link>

            <View style={styles.card}>
                <Text style={styles.pageTitle}>Últimos Pagamentos</Text>
                {
                    pagamentos ? (
                        pagamentos.map((pagamento, id) => {
                            return <PagamentoWidget pagamento={pagamento} key={id}></PagamentoWidget>
                        })
                    ) : (<></>)
                }
                

                <Link href={{
                    pathname: '/(tabs)/history',
                    params: { initialTab: 'parcelas_emprestimo', emprestimoId: emprestimo.id }
                }} asChild>
                    <TouchableOpacity style={styles.buttonSecondary}>
                        <Text style= {styles.linkText}>Ver Histórico Completo</Text>
                    </TouchableOpacity>
                </Link>


            </View>
            
            {
                showPixMenu ? (
                    <View>
                        {
                            chavePixAtiva ? (
                                <>
                                    <Text style={styles.pageTitle}>
                                        Chave PIX para Recebimento
                                    </Text>
                                    <ChavePixItem item={chavePixAtiva} hideActions={true} onDelete={() => {}} onSetAtiva={() => {}} />
                                </>
                            ) : (
                                <>
                                    <View style={styles.card}>
                                        <Text style={styles.pageTitle}>
                                            Chave PIX para Recebimento
                                        </Text>
                                        <Text style={styles.text}>
                                            {
                                                "Confirme ou atualize sua chave PIX para que possamos enviar seu dinheiro."
                                            }
                                        </Text>
                                    </View>
                                    <TouchableOpacity style={styles.button}>
                                        <Text style={styles.buttonText}>
                                            {
                                                "Gerenciar Chave PIX"
                                            }
                                        </Text>
                                    </TouchableOpacity>
                                </>
                            )
                        }
                    </View>
                ) : (<></>)
            }

        </ScrollView>
    )
}

const styles = StyleSheet.create({
    container : {
        width: '90%',
        alignSelf: 'center',
        paddingVertical: 20,
    },
    card: {
        backgroundColor: Colors.light.card,
        borderRadius: 16,
        padding: 20,
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
    text: {
        color: Colors.light.text,
        fontSize: 16
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        color: Colors.light.primaryDark,
        textAlign: 'center',
        marginVertical: 20,
    },

    titleText: {
        color: Colors.light.textSecondary, 
        fontSize: 18,
        marginBottom: 8
    },

    lastPaymentsText : {
        color: Colors.light.text, 
        fontSize: 16,
        marginBottom: 8
    },

    valueText: {
        color: Colors.light.primaryDark,
        fontSize: 36,
        fontWeight: 'bold'
    },

    secondaryText: {
        color: Colors.light.text, 
        fontSize: 16, 
        marginTop: 8
    },

    otherText: {
        color: Colors.light.textSecondary,
        fontSize: 14,
        marginTop: 8
    },

    dateText: {
        color: Colors.light.text,
        fontSize: 14,
        marginTop: 8
    },

    button: {
        backgroundColor: Colors.light.primary,
        paddingVertical: 15,
        borderRadius: 12,
        alignItems: 'center',
    },

    buttonSecondary : {
        backgroundColor: Colors.light.textOnPrimary,
        paddingVertical: 15,
        borderRadius: 12,
        alignItems: 'center',
        marginTop: 10,
    },

    buttonText: {
        color: Colors.light.textOnPrimary,
        fontSize: 16,
        fontWeight: 'bold',
    },
    
    linkText: {
        color: Colors.light.primary,
        textAlign: 'center',
        marginTop: 20,
        fontSize: 16,
        textDecorationLine: 'underline'
    }
})

export default EmprestimoWidget;