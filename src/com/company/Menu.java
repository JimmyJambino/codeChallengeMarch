package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
    private int width = 700;
    private int height = 700;

    private int xBounds = (screenWidth-width)/2; //TODO: Put all this into a method, and check for cases where screen < width etc.
    private int yBounds = (screenHeight-height)/2;
    //TODO: Remember this is a general constructor, we have to specify later as Client and Appointment have
    // different columnNames and columns.
    // I could potentially have Client and Appointment both implement an interface so that our parameter list would be
    // ArrayList<ClientAppointmentInterface> list, in which case I could call a common variable int = columnCount.
    public JTable tableConstructor(ArrayList<TableInformation> list) { // If I have to keep constructing this, then perhaps a LinkedList is faster?
        // Nvm iteration for loops is the same. We are not constructing new List here, only a new array which is fast.
        // In which case, searching through an ArrayList is faster than a LinkedList.
        int columnCount;
        String[] columnNames;

        if(list.size() == 0) { // The first bootup when there are no clients.
            columnCount = 0;
            String[] temp = {"No data available"}; // Can't initialize a String[] after it's been declared, so we must use a temporary String[] to assign value.
            columnNames = temp;

        } else {
            columnCount = list.get(0).getColumnCount();
            columnNames = list.get(0).getColumnNames(); // same as before, it will be the same object throughout the list
        }

        Object[][] data = new Object[list.size()][columnCount]; // [rowCount][columnCount]


        for(int i = 0; i < list.size(); i++) { // For each object
            for(int j = 0; j < columnCount; j++) { // For each column
                data[i][j] = list.get(i).getColumnInfo()[j];
            }
        }
        //JTable table = new JTable(data,columnNames);
        JTable table = new JTable(data,columnNames){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
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
                {"string name","int age","string note",true},
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

    // Shows all current clients and allows the user to add or delete clients, as well as search for specific parameters.
    public JPanel manageClientPanel() {
        // Window Setup
        ArrayList clientList = managementSystemReference.getClientList();
        for(int i = 0; i < 5; i++) {
            clientList.add(new Client("listTest",i,"notedddd"));
        }
        JTable clientTable = tableConstructor(clientList);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.setTitle("Client Management Menu");

        // GBC Setup
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 1;
        int paddingWidth = 30; // TODO: Should be based on box size
        gbc.insets = new Insets(paddingSize,paddingWidth,paddingSize,paddingWidth); // Padding
        gbc.weightx = 1; // Fills out the entire empty space on x-axis

        // Panel setup
        gbc.gridy = 0;
        panel.add(scrollPane,gbc);

        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 1; // y position, goes top to bottom
        JButton button1 = new JButton("Print index");
        button1.addActionListener(A -> ManagementSystem.printSelectedRowIndex(clientTable));
        // Enable button with checkRowSelection?
        panel.add(button1,gbc);

        gbc.gridy = 2;
        JButton button2 = new JButton("Add new Client");
        button2.addActionListener(A -> changePanel(addClientPanel()));
        panel.add(button2,gbc);


        return panel;

    }

    public JPanel addClientPanel() {
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

        JLabel usernameText = new JLabel("Name: ");
        JLabel passwordText = new JLabel("Age: ");
        JTextField usernameField = new JTextField("");
        JTextField passwordField = new JTextField("AGE");
        JButton saveClient = new JButton("Save Client");

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
        panel.add(saveClient, gbc);

        JButton clear = new JButton("Clear");
        gbc.gridx = 0;
        gbc.gridy = 3;
        //gbc.gridwidth = 2;
        panel.add(clear, gbc);

        saveClient.addActionListener(A -> checkInteger(usernameField.getText()));
        //TODO: call method that receives the info from this panel to construct new client
        return panel;
    }

    //TODO: Needs to check for integer age, clear all fields upon being called, and make button disabled.
    public void addChildFromMenu(String name, String age, String note) {
        checkInteger(age);
        if(checkInteger(age)) {
            int intAge = Integer.parseInt(age);
            managementSystemReference.addClientToList(new Client(name,intAge,note));
        }

    }

    public boolean checkInteger(String string) {
        try {
            int number = Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            // Goes here correctly. Continue work.
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public void initializeWindow() {
        JFrame.setDefaultLookAndFeelDecorated(true); // *chefs kiss*
        //JFrame frame = new JFrame("Layout");
        frame = new JFrame("Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        container.add(startMenuPanel());
        frame.pack();
        frame.setBounds((screenWidth-width)/2,(screenHeight-height)/2,width,height); // x and y are pixel location on startup
        frame.setVisible(true);
        //frame.setBounds(100,100,600,200); // figure out a standard window size
    }

    // Works for now.
    private void changePanel(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.getContentPane().doLayout();
        frame.getContentPane().validate(); //reValidate() instead?
        frame.update(frame.getGraphics());
    }

    public JPanel startMenuPanel() {
        System.out.println("New Panel");
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        // Put constraints on different buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 1;
        int paddingWidth = 30; // TODO: Should be based on box size
        gbc.insets = new Insets(paddingSize,paddingWidth,paddingSize,paddingWidth); // Padding
        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0; // y position, goes top to bottom
        gbc.weightx = 1; // Fills out the entire empty space on x-axis
        JButton button1 = new JButton("Manage Clients");
        button1.addActionListener(A -> changePanel(startMenuPanel()));

        panel.add(button1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton button2 = new JButton("Manage Appointments");
        button2.addActionListener(A -> changePanel(manageClientPanel()));
        panel.add(button2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        //gbc.gridwidth = 3;
        panel.add(new JButton("Show Client List"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;

        panel.add(new JButton("Check Calendar"), gbc);

        String title1 = " _____ _____ _____ _____ _____ _____ __ __      _____ _____ _____ _____ _____ _____ _____ ";
        String title2 = "|_   _|  |  |   __| __  |  _  |  _  |  |  |    |     |  _  |   | |  _  |   __|   __| __  |";
        String title3 = "  | | |     |   __|    -|     |   __|_   _|    | | | |     | | | |     |  |  |   __|    -|";
        String title4 = "  |_| |__|__|_____|__|__|__|__|__|    |_|      |_|_|_|__|__|_|___|__|__|_____|_____|__|__|";
        gbc.gridy = 6;
        JLabel label1 = new JLabel(title1);
        label1.setFont(new Font("Monospaced", Font.PLAIN,12));
        panel.add(label1, gbc);
        gbc.gridy = 7;
        JLabel label2 = new JLabel(title2);
        label2.setFont(new Font("Monospaced", Font.PLAIN,12));
        panel.add(label2, gbc);
        gbc.gridy = 8;
        JLabel label3 = new JLabel(title3);
        label3.setFont(new Font("Monospaced", Font.PLAIN,12));
        panel.add(label3, gbc);
        gbc.gridy = 9;
        JLabel label4 = new JLabel(title4);
        label4.setFont(new Font("Monospaced", Font.PLAIN,12));
        panel.add(label4, gbc);

        gbc.gridy = 10;
        try {
            BufferedImage image = ImageIO.read(new File("edit.png"));
            JLabel labelPic = new JLabel(new ImageIcon(image.getScaledInstance(200,200, Image.SCALE_FAST)));
            //labelPic.setBounds(10,10,10,10);
            panel.add(labelPic,gbc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return panel;
    }


    public void loginMenu(Container container) {
        frame = new JFrame("Login");

        //Container container = frame.getContentPane();
        //container.add(panel);
        frame.setContentPane(container);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(100,100,600,200); // figure out a standard window size
    }
    public void setLoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        System.out.println("Username: "+username + " Password: " + password);
        try {
            PrintStream output = new PrintStream(new File("login.txt"));
            output.println(username);
            output.println(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Doesn't work as intended. Use a container replacement instead.
    public void clearFrame(JFrame frame) { //TODO: Make this method nicer, consider making JFrame a class variable
        frame.getContentPane().removeAll(); // Removes all content
        frame.getContentPane().revalidate(); // What does this do?
        frame.getContentPane().repaint(); // Repaints the window
    }

    public Menu(ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    // First check if a txtfile named login exists, if not, ask for database login and if connection is good, save that info in a login.txt
    public void isCorrectLogin() {
        if(readLoginFile("login")) { // method that reads file and checks if it exists
            //TODO: Connect to DB, if gotConnection then...go to main menu
            while(!managementSystemReference.getHasConnection()) {

            }
            managementSystemReference.setupJDBC(username,password); // username and password may be wrong, how to deal?
            // Maybe just go to login menu in case there is an error.
        } else {
            System.out.println("loginMenu");
            loginMenu(loginMenuContainer());
        }

    }
    // Dialogues are added on top of a frame, given in the JDialog constructor.
    // That means we need another method to setup the "main" frame of menu choices.
    public void dialogTest() {
        // While there is no connection
        // Check file for credentials and if there is still no connection then we try to type in new credentials
        if(readLoginFile("login")) {
            managementSystemReference.setupJDBC(username,password);
        } else {
            loginDialogue();
        }
        if(!managementSystemReference.getHasConnection()) {
            loginDialogue();
        }
        /*
        while(!managementSystemReference.getHasConnection()) {
            readLoginFile("login");
            managementSystemReference.setupJDBC(username,password);
            if(!managementSystemReference.getHasConnection()) { //TODO: Right now it continues even when the window is closed.
                loginDialogue();
            }
        }
         */
    }
    public Container loginMenuContainer() {
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
        Container container = new Container();
        container.add(panel);
        return container;
    }

    public void loginDialogue() {
        JDialog dialog = new JDialog(frame,"Example",true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        //frame = new JFrame("Login"); // Don't need this one, it will only make another "empty" window
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
        //clear.addActionListener(A -> clearFrame(frame)); // Remove this function from this dialog
        //Container container = frame.getContentPane();
        //container.add(panel);
            /*
            frame.pack();
            frame.setVisible(true);
            frame.setBounds(100,100,600,200); // figure out a standard window size

             */
        dialog.setSize(300,300); //TODO: Change start position on screen.
        dialog.setBounds((screenWidth-300)/2,(screenHeight-300)/2,300,300);
        dialog.setVisible(true); //TODO: This one needs info, so the background (frame) is actually unnecessary.
    }

}
