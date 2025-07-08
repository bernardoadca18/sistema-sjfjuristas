import React from "react";
import { StyleSheet, Text, TextStyle, View, ViewStyle } from "react-native";
import { Colors } from "@/constants/Colors";

interface StatusWidgetProps {
    status : string;
};

interface StatusStyle {
    container: ViewStyle;
    text: TextStyle;
}

const statusConfig: { [key: string]: StatusStyle } = {
    "Pendente": {
        container: {backgroundColor: Colors.light.pendenteConteinerBackground},
        text: {color: Colors.light.pendenteText}
    },
    "Pago": {
        container: {backgroundColor: Colors.light.pagoConteinerBackground},
        text: {color: Colors.light.pagoText},
    },
    "Atrasado": {
        container: {backgroundColor: Colors.light.atrasadoConteinerBackground},
        text: {color: Colors.light.atrasadoText},
    },
    "Pago com Atraso": {
        container: {backgroundColor: Colors.light.pagoComAtrasoConteinerBackground},
        text: {color: Colors.light.pagoComAtrasoText}
    },
}

const StatusWidget : React.FC<StatusWidgetProps> = ({ status }) => {
    const config = statusConfig[status];

    if (!config)
    {
        return null;
    }

    return (
        <View style={[styles.container, config.container]}>
            <Text style={[styles.text, config.text]}>{status}</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        paddingVertical: 4,
        paddingHorizontal: 12,
        borderRadius: 8,
        marginVertical: 4,
    },
    text: {
        fontSize: 14,
        fontWeight: '500',
        textAlign: 'center',
    }
});

export default StatusWidget;