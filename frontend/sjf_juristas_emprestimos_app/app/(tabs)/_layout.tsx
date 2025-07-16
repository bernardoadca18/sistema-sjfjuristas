import { Tabs } from 'expo-router';
import React from 'react';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '@/constants/Colors';
import { PixIcon } from '@/components/PixIcon';

export default function TabLayout() {

    return (
        <Tabs
            screenOptions={{
                tabBarActiveTintColor: Colors.light.tint,
                headerShown: false,
            }}
        >
            <Tabs.Screen name="index" options={{  title: 'Início', tabBarIcon: ({ color, focused}) => (<Ionicons name={focused ? 'home' : 'home-outline'} size={28} color={color} />) }} />
            <Tabs.Screen name="history" options={{ title: 'Histórico', tabBarIcon: ({ color, focused}) => (<Ionicons name={focused ? 'time' : 'time-outline'} size={28} color={color} />)  }} />
            <Tabs.Screen name="pix" options={{ title: 'Chaves PIX', tabBarIcon: ({ color, focused}) => (<PixIcon color={color} focused={focused}></PixIcon>)  }} />
            <Tabs.Screen name="notifications" options={{ title: 'Notificações', tabBarIcon: ({ color, focused}) => (<Ionicons name={focused ? 'notifications' : 'notifications-outline'} size={28} color={color} />)  }} />
            <Tabs.Screen name="profile" options={{ title: 'Perfil', tabBarIcon: ({ color, focused}) => (<Ionicons name={focused ? 'person' : 'person-outline'} size={28} color={color} />) }} />
            <Tabs.Screen name="loan-details/[id]" options={{ href: null, }}/>
        </Tabs>
    );
}