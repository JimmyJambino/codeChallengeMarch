package com.company;

public class Client implements TableInformation{
    public String name;
    public int age;
    public String note;
    public boolean deleteOnUpdate = false; // If checked, this will remove the client from the  database when updating.
    // Potentially show list of to-be-deleted upon updating and confirm once more?
    private int columnCount = 3;
    private String[] columnNames = {"Name","Age","Note"};

    public Client(String name, int age, String note) {
        this.name = name;
        this.age = age;
        this.note = note;
    }

    public int getColumnCount() {
        return columnCount;
    }
    public String[] getColumnNames() {
        return columnNames;
    }
    public String[] getColumnInfo() {
        String[] info = {name,String.valueOf(age),note};
        return info;
    }

}
