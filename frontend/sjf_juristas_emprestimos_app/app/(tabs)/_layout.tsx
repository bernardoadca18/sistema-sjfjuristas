import { Tabs } from 'expo-router';
import React from 'react';

export default function TabLayout() {
    return (
        <Tabs>
            <Tabs.Screen name="index" options={{ title: 'Início' }} />
            <Tabs.Screen name="history" options={{ title: 'Histórico' }} />
            <Tabs.Screen name="profile" options={{ title: 'Perfil' }} />
        </Tabs>
    );
}