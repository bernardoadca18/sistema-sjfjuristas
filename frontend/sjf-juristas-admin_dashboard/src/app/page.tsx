import React from "react";
import Sidebar from "@/components/ui/Sidebar";
import TitleScreenButton from "@/components/ui/TitleScreenButton";
import styles from "./page.module.css";

const Home : React.FC = () => {
	return (
		<div className={`${styles.pageConteiner}`}>
			<div className={`${styles.buttonConteiner}`}>
				<TitleScreenButton href={"/dashboard"}>
					<p>Dashboard</p>
				</TitleScreenButton>
			</div>
		</div>
	);
}

export default Home;
