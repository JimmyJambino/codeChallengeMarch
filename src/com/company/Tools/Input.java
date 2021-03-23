package com.company.Tools;

import java.util.Scanner;

public class Input {



    public static String inputString(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public static String inputCalendarDate() {
        return ""  ;
    }

    //if we use a swing application then we can maybe make this easier in terms of verification?
    //SWING inputs
    public static String swingInputString() {
        return "";
    }
}
