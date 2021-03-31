package com.company;

public class Main {

    public static void main(String[] args) {
        ManagementSystem managementSystem = new ManagementSystem();
        Menu menu = new Menu(managementSystem);
        menu.initializeWindow();
        menu.checkLogin(); //TODO: Refactor this.
    }
}
