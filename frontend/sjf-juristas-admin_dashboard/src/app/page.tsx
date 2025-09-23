import React from "react";
import TitleScreenButton from "@/components/ui/TitleScreenButton";
import styles from "./page.module.css";

const Home : React.FC = () => {
	return (
		<div className={`${styles.pageConteiner}`}>
			<div className={`${styles.buttonConteiner}`}>
				<TitleScreenButton href={"/login"}>
					<p>Fazer Login</p>
				</TitleScreenButton>
			</div>
		</div>
	);
}

export default Home;
