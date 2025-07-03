import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import EmprestimoWidget from '@/components/ui/EmprestimoWidget';
import { Colors } from '@/constants/Colors';


interface HomeScreenProps {}

const HomeScreen: React.FC<HomeScreenProps> = () => {
  return (
    <View>
      <Text>Seu Empréstimo Atual</Text>
      <EmprestimoWidget/>
      
    </View>
  );
}

const styles = StyleSheet.create({
    pageTitleText : {color: Colors.light.primary, alignSelf: 'center', fontSize: 60, marginTop: 4, fontWeight: 'bold'},
});


export default HomeScreen;