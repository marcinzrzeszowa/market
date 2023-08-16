package com.mj.market.market.config;

import java.util.List;
import java.util.Random;

public class ColorConsole {
    private static Random random = new Random();

    //Color
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    //Bolt Color
    private static final String BLACK_BOLD = "\033[1;30m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String YELLOW_BOLD = "\033[1;33m";
    private static final String BLUE_BOLD = "\033[1;34m";
    private static final String PURPLE_BOLD = "\033[1;35m";
    private static final String CYAN_BOLD = "\033[1;36m";
    private static final String WHITE_BOLD = "\033[1;37m";

    // Underline
    private static final String BLACK_UNDERLINED = "\033[4;30m";
    private static final String RED_UNDERLINED = "\033[4;31m";
    private static final String GREEN_UNDERLINED = "\033[4;32m";
    private static final String YELLOW_UNDERLINED = "\033[4;33m";
    private static final String BLUE_UNDERLINED = "\033[4;34m";
    private static final String PURPLE_UNDERLINED = "\033[4;35m";
    private static final String CYAN_UNDERLINED = "\033[4;36m";
    private static final String WHITE_UNDERLINED = "\033[4;37m";

    // Background
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public static void printlnBlack(String text){System.out.println(ANSI_BLACK+text+ANSI_RESET);}
    public static void printlnRed(String text){
        System.out.println(ANSI_RED+text+ANSI_RESET);
    }
    public static void printlnGreen(String text){
        System.out.println(ANSI_GREEN+text+ANSI_RESET);
    }
    public static void printlnBlue(String text){
        System.out.println(ANSI_BLUE+text+ANSI_RESET);
    }
    public static void printlnPurple(String text){
        System.out.println(ANSI_PURPLE+text+ANSI_RESET);
    }
    public static void printlnCyan(String text){
        System.out.println(ANSI_CYAN+text+ANSI_RESET);
    }
    public static void printlnWhite(String text){
        System.out.println(ANSI_WHITE+text+ANSI_RESET);
    }
    public static void printlnYellow(String text){
        System.out.println(ANSI_YELLOW+text+ANSI_RESET);
    }
    public static void printlnRandom(String text){
        List<String> colors = List.of(ANSI_RED,ANSI_GREEN,ANSI_BLUE,ANSI_PURPLE,ANSI_CYAN,ANSI_WHITE,ANSI_YELLOW);
        int colorNumber = random.nextInt(colors.size());
        System.out.println(colors.get(colorNumber)+text+ANSI_RESET);
    }
}
