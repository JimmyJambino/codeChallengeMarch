package com.company;

public class Client implements TableInformation{
    private int id;
    private String name;
    private int age;
    //new
    private String phonenumber;
    private String email;
    private String industry;
    private String note;
    private int columnCount = 6;
    private String[] columnNames = {"Name","Age", "Phone", "Email", "Industry", "Note"};

    public Client(String name, int age, String phonenumber, String email, String industry, String note) {
        this.name = name;
        this.age = age;
        this.phonenumber = phonenumber;
        this.email = email;
        this.industry = industry;
        this.note = note;
    }

    public int getColumnCount() {
        return columnCount;
    }
    public String[] getColumnNames() {
        return columnNames;
    }
    public String[] getColumnInfo() {
        String[] info = {name,String.valueOf(age),phonenumber,email, industry, note}; //TODO: Update columns in tables
        return info;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(String issue) {
        this.note = issue;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getIndustry() {
        return industry;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

}
