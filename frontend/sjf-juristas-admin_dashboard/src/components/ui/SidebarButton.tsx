import React from "react"
import styles from './SidebarButton.module.css'

type SidebarButtonProps = {
    href : string;
    children? : React.ReactNode;
    icon?: React.ReactElement<{ className? : string }>;
    iconPosition?: 'left' | 'right';
} & React.ButtonHTMLAttributes<HTMLButtonElement>;

const SidebarButton : React.FC<SidebarButtonProps> = ({ href, icon, children, iconPosition, className } : SidebarButtonProps) => {

    const iconWithClass = icon ? React.cloneElement(icon, { className: `${styles.icon} text-2xl` }) : null;

    return (
        <a href={href} className="">
            <div className={`${styles.buttonContainer} inline-flex items-center gap-4 p-4 rounded-xl`}>
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