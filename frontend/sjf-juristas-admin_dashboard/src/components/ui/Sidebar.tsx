import React from "react"
import styles from './Sidebar.module.css'
import { FaUsers, FaFileInvoiceDollar } from "react-icons/fa";
import { FaHandHoldingDollar, FaGears } from "react-icons/fa6";
import SidebarButton from "./SidebarButton";

const Sidebar : React.FC = () => {

    return (
        <div className={`${styles.container}`}>
            <h1 className={`${styles.title}`}>SJF Juristas</h1>
            <SidebarButton href="" icon={<FaFileInvoiceDollar></FaFileInvoiceDollar>} iconPosition="left">
                <p>Propostas</p>
            </SidebarButton>
            <SidebarButton href="" icon={<FaUsers></FaUsers>} iconPosition="left">
                <p>Usuários</p>
            </SidebarButton>
            <SidebarButton href="" icon={<FaHandHoldingDollar></FaHandHoldingDollar>} iconPosition="left">
                <p>Empréstimos</p>
            </SidebarButton>
            <SidebarButton href="" icon={<FaGears></FaGears>} iconPosition="left">
                <p>Configurações</p>
            </SidebarButton>
        </div>
    )
}

export default Sidebar;