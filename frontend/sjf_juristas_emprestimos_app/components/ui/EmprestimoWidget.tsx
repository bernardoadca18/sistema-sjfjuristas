import React, { useEffect } from "react"
import { View, Text, StyleSheet, Platform } from "react-native";
import { Colors } from "@/constants/Colors";

const EmprestimoWidget : React.FC = () => {
    const titles = ['Valor do Empréstimo', 'Saldo Devedor', 'Próximo Pagamento', "Vencimento: "];

    

    return (
        <View style={styles.viewStyle}>
            <Text style={styles.titleText}>
            {
                titles[0]
            }
            </Text>
            <Text style={styles.valueText}>
            {
                "R$ 5.000,00"
            }
            </Text>
            <Text style={styles.titleText}>
            {
                titles[1]
            }
            </Text>
            <Text style={styles.valueText}>
            {
                "R$ 3.259,00"
            }
            </Text>
            <Text style={styles.titleText}>
            {
                titles[2]
            }
            </Text>
            <Text style={styles.valueText}>
            {
                "R$ 51,00"
            }
            </Text>
            <Text style={styles.secondaryText}>
            {
                titles[3]
            }
            </Text>
            
        </View>
    )
}

const styles = StyleSheet.create({
    titleText: { color: Colors.light.textSecondary, alignSelf: 'center', fontSize: 24, marginTop: 20},
    valueText : { color: Colors.light.primary, alignSelf: 'center', fontSize: 40, marginTop: 4, fontWeight: 'bold'},
    viewStyle : { width: '90%', alignSelf: 'center',},
    secondaryText : {color: '#111', alignSelf: 'center', fontSize: 18, marginTop: 20}
})

export default EmprestimoWidget;