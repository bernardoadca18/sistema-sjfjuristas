"use client"

import React, { useState } from "react"
import styles from './SidebarButton.module.css'

type SidebarButtonProps = {
    href : string;
    children? : React.ReactNode;
    icon?: React.ReactElement<{ className? : string }>;
    iconPosition?: 'left' | 'right';
    isActive?: boolean;
    onClick?: () => void;
} & React.ButtonHTMLAttributes<HTMLButtonElement>;

const SidebarButton : React.FC<SidebarButtonProps> = ({ href, icon, children, iconPosition, isActive, onClick, className } : SidebarButtonProps) => {

    const iconWithClass = icon ? React.cloneElement(icon, { className: `${styles.icon} text-2xl` }) : null;
    
    const buttonClasses = `
        ${styles.buttonContainer}
        inline-flex items-center gap-4 p-4 rounded-xl
        ${isActive ? styles.active : ''}
    `;

    return (
        <a href={href}
            onClick={(e) => {
                if (href === "") e.preventDefault();
                if (onClick) onClick();
            }}
        className="w-full">
            <div className={buttonClasses.trim()}>
                {
                    iconPosition === 'left' && iconWithClass
                }
                <span className={`${styles.text} text-lg`}>
                    {children}
                </span>
                {
                    iconPosition === 'right' && iconWithClass
                }
            </div>
        </a>
    )
};

export default SidebarButton;