package com.nptel.courses.online.utility;

import android.graphics.Color;

import java.util.Random;

public class ColorUtility {


    public static String CHOCOLATE = "#D2691E";
    public static String CORAL = "#FF7F50";
    public static String CRIMSON = "#DC143C";
    public static String FOREST_GREEN = "#228B22";
    public static String GOLD = "#FFD700";
    public static String HOT_PINK = "#FF69B4";
    public static String LIGHT_CORAL = "#F08080";
    public static String LIGHT_SEA_GREEN = "#20B2AA";
    public static String LIME_GREEN = "#32CD32";
    public static String MEDIUM_PURPLE = "#9370DB";
    public static String ORANGE_RED = "#FF4500";
    public static String SLATE_BLUE = "#6A5ACD";
    public static String STEEL_BLUE = "#4682B4";
    public static String TOMATO = "#FF6347";
    public static String VIOLATE = "#EE82EE";

    public static int getRandomColor(int redBound, int greenBound, int blueBound) {
        Random random = new Random();
        int r, g, b;
        r = random.nextInt(redBound);
        g = random.nextInt(greenBound);
        b = random.nextInt(blueBound);
        return Color.rgb(r, g, b);
    }

    public static int getPrimaryRandomColor() {
        Random random = new Random();
        int ran = random.nextInt(21);
        switch (ran) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.CYAN;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.MAGENTA;
            case 4:
                return Color.RED;
            case 5:
                return Color.YELLOW;
            case 6:
                return Color.parseColor(CHOCOLATE);
            case 7:
                return Color.parseColor(CORAL);
            case 8:
                return Color.parseColor(CRIMSON);
            case 9:
                return Color.parseColor(FOREST_GREEN);
            case 10:
                return Color.parseColor(GOLD);
            case 11:
                return Color.parseColor(HOT_PINK);
            case 12:
                return Color.parseColor(LIGHT_CORAL);
            case 13:
                return Color.parseColor(LIGHT_SEA_GREEN);
            case 14:
                return Color.parseColor(LIME_GREEN);
            case 15:
                return Color.parseColor(MEDIUM_PURPLE);
            case 16:
                return Color.parseColor(ORANGE_RED);
            case 17:
                return Color.parseColor(SLATE_BLUE);
            case 18:
                return Color.parseColor(STEEL_BLUE);
            case 19:
                return Color.parseColor(TOMATO);
            case 20:
                return Color.parseColor(VIOLATE);
            default:
                return 0;
        }
    }

    public static int[] getRandomColors(int red, int green, int blue, int size) {
        return getRandomColors(red, green, blue, size, null);
    }

    public static int[] getRandomColors(int red, int green, int blue, int size, int[] previousColors) {
        int[] colors = new int[size];
        if (previousColors != null) {
            System.arraycopy(previousColors, 0, colors, 0, previousColors.length);
        }
        int i = 0;
        while (i < size) {
            int newColor = getRandomColor(red, green, blue);
            if (checkIfArrayContainsValue(colors, newColor)) continue;
            colors[i] = newColor;
            i++;
        }
        return colors;
    }

    private static boolean checkIfArrayContainsValue(int[] array, int value) {
        for (int element : array)
            if (element == value)
                return true;
        return false;
    }

    public static int ARGB_To_RGB(int argb) {
        int alpha = Color.alpha(argb);
        int red = Color.red(argb);
        int green = Color.green(argb);
        int blue = Color.blue(argb);
        float fraction = (256 - (float) alpha) / 256;
        red = red + (int) ((256 - red) * fraction);
        green = green + (int) ((256 - green) * fraction);
        blue = blue + (int) ((256 - blue) * fraction);
        return Color.rgb(red, green, blue);
    }

    public static int getLighterVersion(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return Color.parseColor("#36" + hexColor.substring(hexColor.length() - 6));
    }

    public static int getLighterVersion(int color, String alpha) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return Color.parseColor(alpha + hexColor.substring(hexColor.length() - 6));
    }
}
