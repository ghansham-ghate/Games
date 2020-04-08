package com.gghate.tictactoe.Configurations;

import android.graphics.Color;

public class ConfigureOX {

    public static int x_backgroundColor= Color.BLACK;
    public static int o_backgroundColor=Color.BLACK;
    public static int x_textColor=Color.WHITE;
    public static int o_textColor=Color.WHITE;
    public static final String PLAYING="playing";
    public static final String WON="won";
    public static final String LOST="lost";
    public static int getX_backgroundColor() {
        return x_backgroundColor;
    }

    public static void setX_backgroundColor(int x_backgroundColor) {
        ConfigureOX.x_backgroundColor = x_backgroundColor;
    }

    public static int getO_backgroundColor() {
        return o_backgroundColor;
    }

    public static void setO_backgroundColor(int o_backgroundColor) {
        ConfigureOX.o_backgroundColor = o_backgroundColor;
    }

    public static int getX_textColor() {
        return x_textColor;
    }

    public static void setX_textColor(int x_textColor) {
        ConfigureOX.x_textColor = x_textColor;
    }

    public static int getO_textColor() {
        return o_textColor;
    }

    public static void setO_textColor(int o_textColor) {
        ConfigureOX.o_textColor = o_textColor;
    }
}
