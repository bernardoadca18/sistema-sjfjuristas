import { theme } from "./Theme";

const tintColorLight = theme.colors.primary;
//const tintColorDark = '#fff';

export const Colors = {
    light: 
    {
        tint: tintColorLight,
        icon: '#687076',
        tabIconDefault: '#687076',
        tabIconSelected: tintColorLight,
        ...theme.colors
    }
};
