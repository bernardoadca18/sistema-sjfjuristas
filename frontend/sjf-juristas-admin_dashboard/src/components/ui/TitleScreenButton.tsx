import React from "react"
import styles from './TitleScreenButton.module.css'
import Link from "next/link";
import { Url } from "next/dist/shared/lib/router/router";

interface TitleScreenButtonProps
{
    children : React.ReactNode;
    href : Url;
}

const TitleScreenButton : React.FC<TitleScreenButtonProps> = ({ children, href }) => {
    return (
        <Link href={href}>
            <div className={`${styles.buttonContainer} inline-flex items-center gap-4 p-4 pr-16 pl-16 rounded-xl shadow-xl`}>
                <span className={`${styles.text} text-lg`}>
                  {
                        children
                    }
                </span>
            </div>
        </Link>
    );
};

export default TitleScreenButton;