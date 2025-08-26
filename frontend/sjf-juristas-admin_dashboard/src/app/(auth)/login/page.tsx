'use client'

import { LoginForm } from "@/components/ui/LoginForm";
import React from "react";

const LoginPage : React.FC = () => {
    return (
        <div className="min-h-screen flex items-center justify-center bg-background">
            <LoginForm />
        </div>
    );
}

export default LoginPage;