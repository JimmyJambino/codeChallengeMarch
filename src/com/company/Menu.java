package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Menu {
    ManagementSystem managementSystemReference;
    String username;
    String password;
    JFrame frame;
    private Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int)resolution.getWidth();
    private int screenHeight = (int)resolution.getHeight();
    private int width = 700;
    private int height = 700;

    /**
     * Returns a JTable with the information given from an ArrayList.
     * TableInformation is an Interface that is implemented by both Client and Appointment such that they may give the
     * necessary information to the JTable.
     * @param list
     * @return
     */
    public JTable tableConstructor(ArrayList<TableInformation> list) {
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

        JTable table = new JTable(data,columnNames){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        return table;
    }

    /**
     * Returns a JPanel that shows all current Appointment objects in a JTable and gives the user relevant menu options.
     * @return
     */
    public JPanel manageAppointmentPanel() {
        ArrayList appointmentList = managementSystemReference.getAppointmentList();
        ArrayList<Appointment> preSortedList = (ArrayList<Appointment>) appointmentList;
        AppointmentDateComparator comparator = new AppointmentDateComparator();
        Collections.sort(preSortedList, comparator);
        ArrayList sortedAppointmentList = (ArrayList) preSortedList;
        JTable appointmentTable = tableConstructor(appointmentList);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
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
        /*
        gbc.gridy = 1; // y position, goes top to bottom
        JButton button1 = new JButton("Print index");
        button1.addActionListener(A -> ManagementSystem.printSelectedRowIndex(appointmentTable));
        // Enable button with checkRowSelection?
        panel.add(button1, gbc);
         */

        gbc.gridy = 2;
        JButton button2 = new JButton("Add New Appointment");
        button2.addActionListener(A -> changePanel(addAppointmentPanel()));
        panel.add(button2, gbc);

        gbc.gridy = 3;
        JButton button3 = new JButton("Delete Selected Appointment");
        button3.addActionListener(A -> removeAppointment(sortedAppointmentList, appointmentTable));
        panel.add(button3, gbc);

        gbc.gridy = 4;
        JButton button4 = new JButton("Previous Menu");
        button4.addActionListener(A -> changePanel(startMenuPanel()));
        panel.add(button4, gbc);
        return panel;
    }

    /**
     * Returns a JPanel that shows all current clients and allows the user to select one to be added to an Appointment.
     * The Appointment arguments are typed into the JTextField objects at the bottom of the JPanel.
     * @return
     */
    public JPanel addAppointmentPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        ArrayList clientList = managementSystemReference.getClientList();
        JTable clientTable = tableConstructor(clientList);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        GridBagConstraints gbc = new GridBagConstraints();

        //########## GRIDBAGCONSTRAINTS SETTINGS ################
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 3;
        gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
        //#######################################################

        JLabel dayText = new JLabel("Day (DD): ");
        JLabel monthText = new JLabel("Month (MM): ");
        JLabel yearText = new JLabel("Year (YYYY): ");
        JLabel timeText = new JLabel("Hour (09-16): ");
        JTextField dayField = new JTextField("");
        JTextField monthField = new JTextField("");
        JTextField yearField = new JTextField("");
        JTextField timeField = new JTextField("");

        gbc.gridwidth = 2;
        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0;// y position, goes top to bottom
        panel.add(scrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(dayText, gbc);
        gbc.gridx = 1;
        panel.add(dayField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(monthText, gbc);
        gbc.gridx = 1;
        panel.add(monthField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(yearText, gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(timeText, gbc);
        gbc.gridx = 1;
        panel.add(timeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton saveAppointment = new JButton("Save Appointment");
        panel.add(saveAppointment, gbc);

        JButton button3 = new JButton("Previous Menu");
        button3.addActionListener(A -> changePanel(manageAppointmentPanel()));
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(button3, gbc);

        saveAppointment.addActionListener(A -> addAppointmentByFields(clientTable, dayField, monthField, yearField,timeField));
        return panel;
    }

    /**
     * Using getTableIndex(table) it gets the index for the selected Appointment in the table, then it can remove it using
     * the remove() method from ArrayList. It also deletes the Appointment from the database and refreshes the window.
     * @param list
     * @param table
     */
    public void removeAppointment(ArrayList<TableInformation> list, JTable table) {
        Appointment appointment = (Appointment)list.get(getTableIndex(table));
        list.remove(appointment);
        managementSystemReference.getWriter().deleteAppointmentFromDatabase(appointment);
        changePanel(manageAppointmentPanel());
    }

    /**
     * Given the arguments it creates a new Appointment and adds it to the appointmentList and to the database.
     * It also resets the JTextField objects.
     * @param table
     * @param dayField
     * @param monthField
     * @param yearField
     * @param timeField
     */
    public void addAppointmentByFields(
            JTable table, JTextField dayField, JTextField monthField, JTextField yearField, JTextField timeField) {
        Client client = managementSystemReference.getClientFromTable(table);
        int day = Integer.parseInt(dayField.getText());
        int month = Integer.parseInt(monthField.getText())-1; // Months are 0 indexed in java, but not in SQL
        int year = Integer.parseInt(yearField.getText());
        int time = Integer.parseInt(timeField.getText());

        Calendar calendar = new GregorianCalendar(year,month,day,time,0);
        Appointment appointment = new Appointment(client, calendar, time);
        managementSystemReference.addAppointmentToList(appointment);
        managementSystemReference.getWriter().saveAppointmentToDatabase(appointment);
        dayField.setText("");
        monthField.setText("");
        yearField.setText("");
        timeField.setText("");
    }

    // Shows all current clients and allows the user to add or delete clients, as well as search for specific parameters.
    public JPanel manageClientPanel() {
        // Window Setup
        ArrayList clientList = managementSystemReference.getClientList();

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
        JButton button1 = new JButton("Update Selected Client's Info");
        button1.addActionListener(A -> changePanel(editClientPanel(clientTable)));
        // Enable button with checkRowSelection?
        panel.add(button1, gbc);

        gbc.gridy = 2;
        JButton button2 = new JButton("Add new Client");
        button2.addActionListener(A -> changePanel(addClientPanel()));
        panel.add(button2, gbc);

        gbc.gridy = 3;
        JButton button3 = new JButton("Delete Selected Client");
        button3.addActionListener(B -> removeClient(clientList,clientTable));
        panel.add(button3, gbc);

        gbc.gridy = 4;
        JButton button5 = new JButton("Search By Note");
        button5.addActionListener(A -> searchDialog(clientList));
        panel.add(button5, gbc);

        gbc.gridy = 5;
        JButton button4 = new JButton("Previous Menu");
        button4.addActionListener(C -> changePanel(startMenuPanel()));
        panel.add(button4, gbc);

        return panel;
    }

    public JPanel manageClientSearchedPanel(ArrayList<TableInformation> updatedList) {
        // Window Setup
        ArrayList clientList = managementSystemReference.getClientList();

        JTable clientTable = tableConstructor(updatedList);
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
        JButton button1 = new JButton("Update Client Info");
        button1.addActionListener(A -> changePanel(editClientPanel(clientTable)));
        // Enable button with checkRowSelection?
        panel.add(button1, gbc);

        gbc.gridy = 2;
        JButton button2 = new JButton("Add new Client");
        button2.addActionListener(A -> changePanel(addClientPanel()));
        panel.add(button2, gbc);

        gbc.gridy = 3;
        JButton button3 = new JButton("Delete Selected Client");
        button3.addActionListener(B -> removeClient(clientList,clientTable));
        panel.add(button3, gbc);

        gbc.gridy = 4;
        JButton button5 = new JButton("Search By Note");
        button5.addActionListener(A -> searchDialog(clientList));
        panel.add(button5, gbc);

        gbc.gridy = 5;
        JButton button4 = new JButton("Previous Menu");
        button4.addActionListener(C -> changePanel(startMenuPanel()));
        panel.add(button4, gbc);

        return panel;
    }

    public JPanel editClientPanel(JTable table) {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        //########## GRIDBAGCONSTRAINTS SETTINGS ################
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 3;
        gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
        //#######################################################

        Client client = (Client)managementSystemReference.getClientList().get(getTableIndex(table));
        String clientName = client.getName();
        String ageString = String.format("%d", client.getAge());
        String phone = client.getPhonenumber();
        String email = client.getEmail();
        String industry = client.getIndustry();
        String clientNote = client.getNote();

        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0; // y position, goes top to bottom

        JLabel nameText = new JLabel("Name: ");
        panel.add(nameText, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(clientName, 50);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel ageText = new JLabel("Age: ");
        panel.add(ageText, gbc);

        gbc.gridx = 1;
        JTextField ageField = new JTextField(ageString);
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneText = new JLabel("Phone: ");
        panel.add(phoneText, gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(phone);
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel emailText = new JLabel("Email: ");
        panel.add(emailText, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(email);
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel industryText = new JLabel("Industry: ");
        panel.add(industryText, gbc);

        gbc.gridx = 1;
        JTextField industryField = new JTextField(industry);
        panel.add(industryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel noteText = new JLabel("Note: ");
        panel.add(noteText, gbc);

        gbc.gridx = 1;
        JTextField noteField = new JTextField(clientNote);
        panel.add(noteField, gbc);

        gbc.gridwidth = 2;

        gbc.gridx = 0;
        gbc.gridy = 7;
        JButton button1 = new JButton("Update Client");
        button1.addActionListener(A -> updateClient(client, nameField, ageField, phoneField, emailField, industryField, noteField));
        panel.add(button1, gbc);

        gbc.gridy = 8;
        JButton button2 = new JButton("Previous Menu");
        button2.addActionListener(A -> changePanel(manageClientPanel()));
        panel.add(button2, gbc);

        return panel;
    }

    /**
     * This method intentionally doesn't reset the fields inside JTextField objects in the window because it is working with a specific
     * Client and as such, if the user accidently clicks update twice, then all fields will be lost.
     * @param client
     * @param nameField
     * @param ageField
     * @param phoneField
     * @param emailField
     * @param industryField
     * @param noteField
     */
    public void updateClient(
            Client client, JTextField nameField, JTextField ageField, JTextField phoneField, JTextField emailField, JTextField industryField, JTextField noteField) {
        if(checkInteger(ageField.getText())) {
            client.setName(nameField.getText());
            client.setAge(Integer.parseInt(ageField.getText()));
            client.setPhonenumber(phoneField.getText());
            client.setEmail(emailField.getText());
            client.setIndustry(industryField.getText());
            client.setNote(noteField.getText());
            managementSystemReference.getWriter().updateClient(client);
        } else {
            System.out.println("Wrong input. Try again.");
        }

    }

    //TODO: Split this into 2 methods.
    public void removeClient(ArrayList<TableInformation> list, JTable table) {
        Client client = (Client)list.get(getTableIndex(table));
        list.remove(client);
        managementSystemReference.getWriter().deleteClientFromDatabase(client);
        ArrayList<TableInformation> aList = managementSystemReference.getAppointmentList();
        for(int i = 0; i < aList.size(); i++) {
            Appointment appointment = (Appointment)aList.get(i);
            if(appointment.getClient().getId() == client.getId()) {
                aList.remove(appointment);
            }
        }
        changePanel(manageClientPanel());
    }

    public void getClientsByNote(ArrayList<TableInformation> list, String search) {
        ArrayList<TableInformation> noteClientList = new ArrayList<>(); // Needs to be another TableInformation list as we need it for the table construction.
        String lowerCaseSearch = search.toLowerCase();
        for(int i = 0; i < list.size(); i++) {
            Client client = (Client)list.get(i);
            String clientLowerCaseNote = client.getNote().toLowerCase();
            if(clientLowerCaseNote.contains(lowerCaseSearch)) {
                noteClientList.add(client);
            }
        }
        changePanel(manageClientSearchedPanel(noteClientList));
    }

    public void searchDialog(ArrayList<TableInformation> list) {
        JDialog dialog = new JDialog(frame,"Search notes for Keywords",true);
        dialog.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        //########## GRIDBAGCONSTRAINTS SETTINGS ################
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 3;
        gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
        //#######################################################

        JLabel searchLabel = new JLabel("Search: ");
        JTextField searchField = new JTextField("");
        JButton search = new JButton("Start Search");

        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0; // y position, goes top to bottom
        dialog.add(searchLabel, gbc);
        gbc.gridx = 1;
        dialog.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(search, gbc);

        search.addActionListener(A -> getClientsByNote(list, searchField.getText()));

        dialog.setSize(300,300);
        dialog.setBounds((screenWidth-300)/2,(screenHeight-300)/2,300,300);
        dialog.setVisible(true);
    }

    public JPanel addClientPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        //########## GRIDBAGCONSTRAINTS SETTINGS ################
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 3;
        gbc.insets = new Insets(paddingSize,paddingSize,paddingSize,paddingSize); // Padding
        //#######################################################

        JLabel nameText = new JLabel("Name: ");
        JLabel ageText = new JLabel("Age: ");
        JLabel phoneText = new JLabel("Phonenumber: ");
        JLabel emailText = new JLabel("Email: ");
        JLabel industryText = new JLabel("Industry: ");
        JLabel noteText = new JLabel("Note: ");
        JTextField nameField = new JTextField("", 20);
        JTextField ageField = new JTextField("");
        JTextField phoneField = new JTextField("");
        JTextField emailField = new JTextField("");
        JTextField industryField = new JTextField("");
        JTextField noteField = new JTextField("");
        JButton saveClient = new JButton("Save Client");

        gbc.gridx = 0; // x position, goes left to right
        gbc.gridy = 0; // y position, goes top to bottom
        panel.add(nameText, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(ageText, gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(phoneText, gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailText, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(industryText, gbc);
        gbc.gridx = 1;
        panel.add(industryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(noteText, gbc);
        gbc.gridx = 1;
        panel.add(noteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(saveClient, gbc);

        JButton button3 = new JButton("Previous Menu");
        button3.addActionListener(A -> changePanel(manageClientPanel()));
        gbc.gridx = 0;
        gbc.gridy = 7;
        //gbc.gridwidth = 2;
        panel.add(button3, gbc);

        saveClient.addActionListener(A -> addClientByFields(
                nameField.getText(),ageField.getText(),noteField,nameField,ageField, phoneField, emailField, industryField));
        //TODO: call method that receives the info from this panel to construct new client
        return panel;
    }

    /**
     * Using the given arguments it will add a new Client to the clientList and to the database. It also resets the
     * JTextFields to indicate the Client has been saved.
     * @param name
     * @param age
     * @param noteField
     * @param nameField
     * @param ageField
     */
    public void addClientByFields(
            String name, String age, JTextField noteField, JTextField nameField, JTextField ageField, JTextField phoneField,
            JTextField emailField, JTextField industryField) {
        if(checkInteger(age) && checkStringValue(name)) {
            int intAge = Integer.parseInt(age);
            String phone = phoneField.getText();
            String email = emailField.getText();
            String industry = industryField.getText();
            String note = noteField.getText();
            Client client = new Client(name,intAge,phone,email,industry,note);
            managementSystemReference.addClientToList(client);
            managementSystemReference.getWriter().saveClientToDatabase(client);
            nameField.setText("");
            ageField.setText("");
            phoneField.setText("");
            emailField.setText("");
            industryField.setText("");
            noteField.setText("");
        } else {
            System.out.println("Wrong input. Try again.");
        }
    }

    public boolean checkInteger(String string) {
        try {
            int number = Integer.parseInt(string);
            if(number == 0) {
                return false;
            }
        } catch (NumberFormatException exception) {
            // Goes here correctly. Continue work.
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Checks the
     * @param name
     * @return
     */
    public boolean checkStringValue(String name) {
        if(name.length() == 0) {
            return false;
        }
        String nameCapital = name.toUpperCase();

        // Contains vowels
        boolean nameA = nameCapital.contains("A");
        boolean nameE = nameCapital.contains("E");
        boolean nameY = nameCapital.contains("Y");
        boolean nameU = nameCapital.contains("U");
        boolean nameI = nameCapital.contains("I");
        boolean nameO = nameCapital.contains("O");
        boolean nameÆ = nameCapital.contains("Æ");
        boolean nameØ = nameCapital.contains("Ø");
        boolean nameÅ = nameCapital.contains("Å");
        if(nameA || nameE || nameY || nameU || nameI || nameO || nameÆ || nameØ || nameÅ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initializes a Frame (Main window) with a predetermined size, adds a JPanel to it and positions the window
     * in the middle of the screen.
     */
    public void initializeWindow() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Main Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        container.add(startMenuPanel());
        frame.pack();
        frame.setBounds((screenWidth-width)/2,(screenHeight-height)/2,width,height); // x and y are pixel location on startup
        frame.setVisible(true);
    }

    /**
     * Clears all content in the frame (Main window), adds a new JPanel and refreshes the window.
     * @param panel
     */
    private void changePanel(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.getContentPane().doLayout();
        frame.getContentPane().validate(); //reValidate() instead?
        frame.update(frame.getGraphics());
    }

    public JPanel startMenuPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout(); //
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        // Put constraints on different buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills out the empty space of button text
        int paddingSize = 1;
        int paddingWidth = 30; // TODO: Should be based on box size
        gbc.insets = new Insets(paddingSize,paddingWidth,paddingSize,paddingWidth); // Padding

        gbc.gridx = 0; // x position, goes from left to right
        gbc.gridy = 0;
        try {
            BufferedImage image = ImageIO.read(new File("clientmanagerpicAlpha.png"));
            JLabel labelPic = new JLabel(new ImageIcon(image.getScaledInstance(630,300, Image.SCALE_FAST)));
            panel.add(labelPic,gbc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gbc.gridy = 1; //TODO: Use ++ in the future, this is ridiculous.
        JLabel filler1 = new JLabel("-----------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------");
        panel.add(filler1, gbc);
        gbc.gridy = 2;
        JLabel filler2 = new JLabel("-----------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------");
        panel.add(filler2, gbc);

        gbc.gridy = 3;
        JLabel quoteOfTheDay = new JLabel(managementSystemReference.randomQuote());
        panel.add(quoteOfTheDay, gbc);

        gbc.gridy = 4;
        JLabel filler3 = new JLabel("-----------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------");
        panel.add(filler3, gbc);

        gbc.gridy = 5;
        JLabel filler4 = new JLabel("-----------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------");
        panel.add(filler4, gbc);

        gbc.gridy = 6; // y position, goes top to bottom
        gbc.weightx = 1; // Fills out the entire empty space on x-axis
        JButton button1 = new JButton("Manage Clients");
        button1.addActionListener(A -> changePanel(manageClientPanel()));
        panel.add(button1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JButton button2 = new JButton("Manage Appointments");
        button2.addActionListener(A -> changePanel(manageAppointmentPanel()));
        panel.add(button2, gbc);

        return panel;
    }

    public void setLoginCredentials(String username, String password, JDialog dialog) {
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
        dialog.dispose();
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


    public Menu(ManagementSystem managementSystemReference) {
        this.managementSystemReference = managementSystemReference;
    }

    /**
     * Creates a pop-up Dialog in case the user hasn't logged in before or their username and password are incorrect.
     * It will continue to prompt the user for the correct credentials until given or program is exited.
     */
    public void loginDialog() {
        while(!managementSystemReference.getHasConnection()) {
            readLoginFile("login");
            managementSystemReference.setupJDBC(username,password);
            if(!managementSystemReference.getHasConnection()) { //TODO: Right now it continues even when the window is closed. This is fine, we can exit program with another button.
                loginDialogue();
            }
        }
    }


    public void loginDialogue() {
        JDialog dialog = new JDialog(frame,"Login",true);
        //dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
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

        JButton noSQL = new JButton("Exit Program");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        noSQL.addActionListener(A -> System.exit(0));
        dialog.add(noSQL, gbc);

        login.addActionListener(A -> setLoginCredentials(usernameField.getText(),passwordField.getText(), dialog));

        dialog.setSize(300,300); //TODO: Change start position on screen.
        dialog.setBounds((screenWidth-300)/2,(screenHeight-300)/2,300,300);
        dialog.setVisible(true);
    }

    public int getTableIndex(JTable table) {
        return table.getSelectedRow();
    }

}
