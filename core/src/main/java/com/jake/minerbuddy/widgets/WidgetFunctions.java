package com.jake.minerbuddy.widgets;

public class WidgetFunctions {

    public static String cleanPrice(int price){
        int power = (int) Math.log10(price);
        if (power < 3){
            return Integer.toString(price);
        }else if(power < 6){
            return (float)price / 1000 + "K";
        }else{
            return (float)price / 1000000 + "M";
        }
    }
}
