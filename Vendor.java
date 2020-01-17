package com.company;




public class Vendor {

    //properties of vendor object

    private String name;
    private String taxid;
    private String zip;
    private int id;
    private String group_name;

    //this is the constructor

    public Vendor(String name, String TaxID, String zip , int id ){
        this.name = name;
        this.taxid = TaxID;
        this.zip =zip;
        this.id = id;

    }

    //methods to insert the values into the vendor objects
    public void setName(String name) {
        this.name = name;
    }
    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    //methods to get the methods from the vendor object
    public String getName() {
        return name;
    }

    public String getTaxid() {
        return taxid;
    }

    public String getZip() {
        return zip;
    }

    public int getId() { return id;  }

    public String getGroup_name() { return group_name; }


}
