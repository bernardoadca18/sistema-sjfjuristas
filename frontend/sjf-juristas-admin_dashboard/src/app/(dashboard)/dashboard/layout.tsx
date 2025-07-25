import Sidebar from "@/components/ui/Sidebar";
import React, { ReactNode } from "react";

interface DashboardLayoutProps {
    children : ReactNode;
}

const DashboardLayout : React.FC<DashboardLayoutProps> = ({ children } : { children : ReactNode }) => {
    return (
        <div style={ { display : 'flex' } }>
            <Sidebar></Sidebar>
            <main style={{ marginLeft: '15vw'}}>
                {children}
            </main>
        </div>
    )
};

export default DashboardLayout;