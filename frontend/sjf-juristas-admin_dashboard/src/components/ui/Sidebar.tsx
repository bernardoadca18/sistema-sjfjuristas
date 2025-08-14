"use client"

import React from "react"
import styles from './Sidebar.module.css'
import { FaUsers, FaFileInvoiceDollar } from "react-icons/fa";
import { FaHandHoldingDollar, FaGears } from "react-icons/fa6";
import SidebarButton from "./SidebarButton";

const sidebarItems = [
    {
        name: 'Propostas',
        icon: <FaFileInvoiceDollar />,
        href: '/dashboard/propostas'
    },
    {
        name: 'Usuários',
        icon: <FaUsers />,
        href: '/dashboard/usuarios'
    },
    {
        name: 'Empréstimos',
        icon: <FaHandHoldingDollar />,
        href: '/dashboard/emprestimos'
    },
    {
        name: 'Configurações',
        icon: <FaGears />,
        href: '/dashboard/configuracoes'
    }
];

const Sidebar : React.FC = () => {

    const [activeButton, setActiveButton] = React.useState<string>("Propostas");

    return (
        <div className={`${styles.container}`}>
            <h1 className={`${styles.title}`}>SJF Juristas</h1>
            {
                sidebarItems.map((item) => (
                    <SidebarButton 
                        key={item.name}
                        href={item.href}
                        icon={item.icon}
                        iconPosition="left"
                        isActive={activeButton === item.name}
                        onClick={() => setActiveButton(item.name)}
                    >
                        {item.name}
                    </SidebarButton>
                ))
            }
        </div>
    )
}

export default Sidebar;