package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu(new ManagementSystem());
        //menu.mainMenu();
        menu.mainGui();
        //menu.loginMenu();
        //menu.dialogTest();
        JDBCWriter writer = new JDBCWriter();


    }
    public static void main2(String[] args) {
	// write your code here
        //Date date = new Date(); // .before(date) and .after(date)
        JButton pbConnect;
        JTextField txtUrl;
        JLabel labelCount; // Labels are put on the side?

        JFrame frame = new JFrame("Test frame");
        JPanel panel = new JPanel();
        pbConnect = new JButton("Button Text");
        frame.getContentPane().add(panel, BorderLayout.NORTH); // tilføj panel til frame
        panel.add(pbConnect); // tilføj button til panel

        JPanel panelBottom = new JPanel(); //TODO: Remember buttons have methods like .setEnabled(false)
        JButton pbExit = new JButton("Exit program");
        frame.getContentPane().add(panelBottom, BorderLayout.SOUTH); //TODO: Look up BorderLayout, how can we do similar things?
        panelBottom.add(pbExit);

        pbConnect.addActionListener(a -> testMethod()); // calls a method upon pressing button

        Vector<String> v1 = new Vector<>();
        v1.add("Line test");
        v1.add("Line 2 test");
        JLabel listLabel = new JLabel("List");
        JList list = new JList(v1);
        JPanel leftPanel = new JPanel();
        leftPanel.add(listLabel);
        leftPanel.add(list); // bottom left

        frame.getContentPane().add(leftPanel,BorderLayout.WEST);
        leftPanel.add(listLabel);
        //panelBottom.add(list); //Hmm why isn't it showing?

        // Exit program
        pbExit.addActionListener(b -> exitProgram()); //TODO: NICE!

        //Main window
        frame.pack(); // unnecessary with setBounds, as pack() makes the window minimal sized considering its content (such as buttons)
        frame.setBounds(100,100,600,200);
        frame.setVisible(true); // window pop up, otherwise it won't show
        //TODO: Put all panels down here instead for better visibility

    }

    public static void testMethod() {
        System.out.println("Button test test");
    }

    public static void exitProgram() {
        System.exit(0);
    }

    public static void printSelectedRowIndex(JTable table) {

        boolean ex = table.getSelectionModel().isSelectionEmpty();
        if(!ex){
            int index = table.getSelectedRows()[0]; // 0 because it is 0 indexed and we always return the top selected row.
            index = table.getSelectedRow(); // singular row, delete above?
            System.out.println(index);
        } else {
            System.out.println("nothing");
        }
    }
}
