package com.company;


import org.apache.commons.text.similarity.FuzzyScore;
import java.util.Hashtable;
import java.util.Locale;

public class Group {


    //group identifier
    private String name;
    
    //groups variables
    private Hashtable<Long,Vendor> group = new Hashtable<Long, Vendor>();
    private long id_counter = 0;



    //this is the constructor of the group*********************************************
    public Group(String name) {
        this.name = name;
        this.setGroup();
    }

    //create a hashtable in the group***************************************************
    private void setGroup() {
       this.group = new Hashtable<Long, Vendor>();
    }


    //gives the group hashtable ****************************************************
    public Hashtable<Long, Vendor> getGroup() {
        return this.group;

    }

    // gives the name of the group ***************************************************
    public String getName() {
        return name;
    }

    //method to add the vendor to the group*************************************************
    public void addVendor(Vendor vendor){
        this.group.put(this.id_counter,vendor);
        this.id_counter ++;        
    }

    
    //method to get all the vendors in the group*********************************************
    public void printGroup(){
        int length = this.group.size();
        for (int i = 0 ; i < length ; i++){
            Vendor vendor =  this.group.get(i); //gets the vendor in the hashtable as per its key value
        }
    }

    //method to get vendor by position*****************************************************
    public Vendor get_Vendor_by_Position(long pos){
        if(this.group.containsKey(pos)){
            Vendor result = this.group.get(pos);
            return result;
        }
        else{
            final Vendor result = null;
            return result;
        }
    }  



    //belong to this group************************************************** ALGORITHM WITH THE INTELLIGENCE TO TAKE THE DECISION IF BELONGS OR NOT ***************************


    public boolean belong_to ( Vendor vendor ){

        //get all the data from analyzed outside group vendor in order to associate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String name_outside = vendor.getName();
        String tax_id_outside = vendor.getTaxid();
        String zip_outside = vendor.getZip();
        //char[] analyzed_vendor_outside = vendor_name.toCharArray(); //break up the array in pieces
        int size = this.group.size(); //size of the group

        if(size == 0){
            this.addVendor(vendor);
            return true;
        }

        for(int i = 0 ; i < size ; i++){
            Vendor analyzed_vendor_ingroup = this.get_Vendor_by_Position(i); // gets the vendor in this position

            //gets the distance od the main variables related to associate the grouping !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String name_ingroup = analyzed_vendor_ingroup.getName();
            int name_distance = this.distance(name_outside,name_ingroup); //this is my algorithm @adiego8
            int name_distance_fuzzy = this.distance_fuzzy(name_outside,name_ingroup); //this is the fuzzy that comes with apache maven

            String tax_ingroup = analyzed_vendor_ingroup.getTaxid();
            int taxid_distance = this.distance(tax_id_outside,tax_ingroup);                     //same as comment above
            int taxid_distance_fuzzy = this.distance_fuzzy(tax_id_outside,tax_ingroup);

            String zip_ingroup = analyzed_vendor_ingroup.getZip();                             //same as comment above
            int zip_distance = this.distance(zip_outside,zip_ingroup);
            int zip_distance_fuzzy = this.distance_fuzzy(zip_outside,zip_ingroup);


            // condition meaning that if the names are equals or match in the taxid then the vendor is added to thi group
            if(name_distance == 0 || taxid_distance == 0){
                this.addVendor(vendor);
                return true;
            }
            //if the distance is under 16 means the result is pretty close so it should be grouped here
            if(name_distance < 14 && name_distance_fuzzy > 46){      //I set it up in 12 in order to do it as close as possible when doing tests with strings noted that from arount 12
                this.addVendor(vendor);  //was when the strings started to be really different
                return true;
            }
        }
        return false;
    }


 //************************************************************  THIS ARE THE FUZZY ALGORITHMS ***********************************************************************
 //************************************************************  THIS ARE THE FUZZY ALGORITHMS  ************************************************************************

    //algorithm to calculate how close are the strings being compared    ******* @adiego8 ****************


    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }


    // *****************  imported from apache.maven **************************************


    public int distance_fuzzy (String a , String b){  ///has to be over 45 to enter into the group
       a = a.toLowerCase();
       b = b.toLowerCase();

       double fuzzyscore = new FuzzyScore(Locale.getDefault()).fuzzyScore(a,b);
       int result = (int) Math.round(fuzzyscore);
       return result;
    }



}
