package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    ManagementSystem managementSystemReference;
    String username;
    String password;
    JFrame frame; // TODO: Keep a single frame, so make sure the size is correct.
    private Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int)resolution.getWidth();
    private int screenHeight = (int)resolution.getHeight();
    private int width = 800;
    private int height = 800;

    private int xBounds = (screenWidth-width)/2; //TODO: Put all this into a method, and check for cases where screen < width etc.
    private int yBounds = (screenHeight-height)/2;
    //TODO: Remember this is a general constructor, we have to specify later as Client and Appointment have
    // different columnNames and columns.
    // I could potentially have Client and Appointment both implement an interface so that our parameter list would be
    // ArrayList<ClientAppointmentInterface> list, in which case I could call a common variable int = columnCount.
    public JTable tableConstructor(ArrayList<TableInformation> list) { // If I have to keep constructing this, then perhaps a LinkedList is faster?
        // Nvm iteration for loops is the same. We are not constructing new List here, only a new array which is fast.
        // In which case, searching through an ArrayList is faster than a LinkedList.
        int columnCount = list.get(0).getColumnCount(); //  We are assuming only one type of class in the list
        // is in this list, so "just" look at the first objects information.
        Object[][] data = new Object[list.size()][columnCount]; // [rowCount][columnCount]
        String[] columnNames = list.get(0).getColumnNames(); // same as before, it will be the same object throughout the list

        for(int i = 0; i < list.size(); i++) { // For each object
            for(int j = 0; j < columnCount; j++) { // For each column
                data[i][j] = list.get(i).getColumnInfo()[j];
            }
        }
        JTable table = new JTable(data,columnNames);

        return table;
    }

    public void mainMenu() {
        Client client = new Client("Jimmy", 29, "This is a test note, but what happens when the line is super long? \nOr contains line breaks?");

        String[] columnNames = {
                "First Name",
                "Age",
                "Notes",
                "Delete-On-Update"
        };
        Object[][] data = {
                {client.name,client.age,client.note,true},
                {"Jungne",26,"Yohoho he be a pirate",true}
        };

        String[] columnNamesCalendar = {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
        }; //TODO: each day has 8 rows with information about the "hour" as each session is 1 hour long and a workday is 9 - 17
        JTable calendarTable = new JTable();

        JTable table = new JTable(data,columnNames){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        }; //TODO: WOOOOOO!
        table.setSelectionMode(0); // Single, perhaps just use .setRowSelection(true)?
        //table.getSelectedRows(); // returns an array of row indexes

        Menu menu = new Menu(new ManagementSystem()); //TODO: Check this again
        ArrayList<TableInformation> clientList = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            clientList.add(new Client("listTest",i,"notedddd"));
        }
        //clientList.add(new Client("listTest",1991,"notedddd"));
        JTable testTable = menu.tableConstructor(clientList);

        JScrollPane scrollPane = new JScrollPane(testTable);
        //table.setEnabled(false); // !!! This works! But doesn't allow the user to copy text to other programs (like an email)
        //Maybe this will be fine for this project tho, but this will only display information, how do I make it
        //easier for them to choose a client to update? Or to make an appointment?
        //table.setCellSelectionEnabled(true);

        //###########
        JPanel panel = new JPanel(new BorderLayout()); // Do I need more panels?
        //JFrame frame = new JFrame("Window Title");
        frame = new JFrame("Window Title");


        JButton button = new JButton("Print index selection");
        JButton button2 = new JButton("aaa");
        JButton button3 = new JButton("ooo");
        panel.add(button,BorderLayout.NORTH);
        panel.add(button2,BorderLayout.SOUTH);
        panel.add(button3,BorderLayout.CENTER);
        button.addActionListener(a -> Main.printSelectedRowIndex(testTable));

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout(10,10));
        container.add(panel);
        container.add(scrollPane, BorderLayout.WEST); // Doesn't work hmm
        //################

        frame.pack();

        frame.setBounds((screenWidth-width)/2,(screenHeight-height)/2,width,height); // x and y are pixel location on startup
        frame.setVisible(true);
        System.out.print(screenWidth + " " + screenHeight);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); yoo this might be  really good+

    }

    public void mainGui() {
        JFrame.setDefaultLookAndFeelDecorated(true); // *chefs kiss*
        //JFrame frame = new JFrame("Layout");
        frame = new JFrame("Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        // Put constraints on different buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 3;
        gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0; // y position, goes top to bottom
        JButton button1 = new JButton("Manage Clients");
        panel.add(button1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JButton("Button 2"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(new JButton("Button 3"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 10;
        gbc.gridwidth = 10;
        panel.add(new JButton("Button 5"), gbc);

        Container container = frame.getContentPane();
        container.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(100,100,600,200); // figure out a standard window size
    }

    // First check if a txtfile named login exists, if not, ask for database login and if connection is good, save that info in a login.txt
    public void loginMenu() {
        if(readLoginFile("login")) { // method that reads file and checks if it exists
            //TODO: Connect to DB, if gotConnection then...go to main menu
            managementSystemReference.setupJDBC(username,password); // username and password may be wrong, how to deal?
            // Maybe just go to login menu in case there is an error.

        } else { //TODO: Create file upon successfull connection
            frame = new JFrame("Login");
            JPanel panel = new JPanel();
            GridBagLayout layout = new GridBagLayout(); //
            panel.setLayout(layout);
            GridBagConstraints gbc = new GridBagConstraints();

            //########## GRIDBAGCONSTRAINTS SETTINGS ################
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
            int paddingSize = 3;
            gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
            //#######################################################

            JLabel usernameText = new JLabel("USERNAME: ");
            JLabel passwordText = new JLabel("PASSWORD: ");
            JTextField usernameField = new JTextField("USERNAME");
            JTextField passwordField = new JTextField("PASSWORD");
            JButton login = new JButton("Login");

            gbc.gridx = 0; // x position, goes left to right
            gbc.gridy = 0; // y position, goes top to bottom
            panel.add(usernameText, gbc);
            gbc.gridx = 1;
            panel.add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(passwordText, gbc);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            panel.add(login, gbc);

            JButton clear = new JButton("Clear");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            panel.add(clear, gbc);

            login.addActionListener(A -> setLoginCredentials(usernameField.getText(),passwordField.getText()));
            clear.addActionListener(A -> clearFrame(frame));
            Container container = frame.getContentPane();
            container.add(panel);
            frame.pack();
            frame.setVisible(true);
            frame.setBounds(100,100,600,200); // figure out a standard window size
        }

    }
    public void setLoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        System.out.println("Username: "+username + " Password: " + password);
    }

    /**
     * Tries to read a text file and if it exists, username and password will be auto-loaded.
     * @param fileName
     * @return
     */
    public boolean readLoginFile(String fileName) {
        try {
            Scanner load = new Scanner(new File(fileName+".txt"));
            username = load.nextLine(); // nextLine() because username and password may include spaces
            password = load.nextLine();
            return true;

        } catch (IOException io) {
            System.out.println("File not found");
            return false;
        }
    }

    public void clearFrame(JFrame frame) { //TODO: Make this method nicer, consider making JFrame a class variable
        frame.getContentPane().removeAll(); // Removes all content
        //frame.getContentPane().revalidate(); // What does this do?
        frame.getContentPane().repaint(); // Repaints the window
    }

    public Menu(ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    // Dialogues are added on top of a frame, given in the JDialog constructor.
    // That means we need another method to setup the "main" frame of menu choices.
    public void dialogTest() {
        if(readLoginFile("login")) { // method that reads file and checks if it exists
            //TODO: Connect to DB, if gotConnection then...go to main menu
            managementSystemReference.setupJDBC(username,password); // username and password may be wrong, how to deal?
            // Maybe just go to login menu in case there is an error.
        } else {
            JDialog dialog = new JDialog(frame,"Example",true);
            dialog.setLayout(new GridBagLayout());
            frame = new JFrame("Login");
            // top ignore
            JPanel panel = new JPanel();
            GridBagLayout layout = new GridBagLayout(); //
            panel.setLayout(layout);
            // bot ignore
            GridBagConstraints gbc = new GridBagConstraints();

            //########## GRIDBAGCONSTRAINTS SETTINGS ################
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
            int paddingSize = 3;
            gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
            //#######################################################

            JLabel usernameText = new JLabel("USERNAME: ");
            JLabel passwordText = new JLabel("PASSWORD: ");
            JTextField usernameField = new JTextField("USERNAME");
            JTextField passwordField = new JTextField("PASSWORD");
            JButton login = new JButton("Login");

            gbc.gridx = 0; // x position, goes left to right
            gbc.gridy = 0; // y position, goes top to bottom
            dialog.add(usernameText, gbc);
            gbc.gridx = 1;
            dialog.add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            dialog.add(passwordText, gbc);
            gbc.gridx = 1;
            dialog.add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            dialog.add(login, gbc);

            JButton clear = new JButton("Clear");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            dialog.add(clear, gbc);

            login.addActionListener(A -> setLoginCredentials(usernameField.getText(),passwordField.getText()));
            clear.addActionListener(A -> clearFrame(frame));
            Container container = frame.getContentPane();
            //container.add(panel);
            frame.pack();
            frame.setVisible(true);
            frame.setBounds(100,100,600,200); // figure out a standard window size
            dialog.setSize(300,300); //TODO: Change start position on screen.
            dialog.setVisible(true); //TODO: This one needs info, so the background (frame) is actually unnecessary.
        }
    }
    public void startFrame() {

    }


}
