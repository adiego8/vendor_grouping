package com.company;

import com.opencsv.CSVWriter;
import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;


public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

//************* TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *****************************
//        Vendor test = new Vendor("ed","123","54445", 1);
//
//        String taxid = test.getTaxid();
//        System.out.println(taxid);


//        Group test_distance = new Group("testing");
//
//        String uno = "CENTER NORTH MIAMI THERAPY";
//        String dos = "MIAMI THERAPY";
//
//        int value = test_distance.distance(uno,dos);
//        int value2 = test_distance.distance_fuzzy(uno, dos);
//        System.out.println(value + "     " + value2);
//************* TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *****************************


        //declared the lists with the object VENDOR and GROUP

        LinkedList<Vendor> vendor_list = new LinkedList<Vendor>();
        BufferedReader reader = null;
        String line = "";
        String spliby = ",";
        String separated_line[];
        int blank_taxid_counter = 0;

        //import the csv files into the program based on a path

        String csvFile = "C:\\Users\\adiego8\\Desktop\\Vendor Fraud Detection System\\Clean\\to process Clean Vendor Table only by Medical Vendors.csv";

        try {
             reader = new BufferedReader(new FileReader(csvFile));

             while((line = reader.readLine()) != null){

                 separated_line = line.split(spliby);

                 int ID = Integer.parseInt(separated_line[0]);
                 String VendorName = separated_line[1];
                 String taxID = separated_line[2];
                 if(taxID.equals("")| taxID.contains("  ")){
                     taxID = String.valueOf(blank_taxid_counter);
                     blank_taxid_counter++;
                 }
                 String zipCode = separated_line[3];

                 Vendor entrance = new Vendor(VendorName, taxID, zipCode, ID); //create the vendor
                 vendor_list.addLast(entrance); // add vendor to the list
             }


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

/*
 AT THIS POINT ALL THE VENDOR LIST IS ALREADY CREATED FROM NOW ON START CREATING THE GROUPS
 START ITERATING THE VENDOR LIST IN ORDER TO CREATE THE GROUPS
*/
        LinkedList<Group> group_list = new LinkedList<Group>();
        int groupnumber = 0;
        // LISTS ITERATORS
        ListIterator<Vendor> vendor_list_iterator = vendor_list.listIterator();


        //****************** MAIN LOGIC ***************** MAIN LOGIC ********************* MAIN LOGIC ***********************

        while(vendor_list_iterator.hasNext()){                   //iterating over the vendors
            Vendor vendor_in_question = vendor_list_iterator.next();

            if(group_list.isEmpty()){                             //create the first group if there are no groups in the list else:
                group_list.addLast(create_group("headers"));
                group_list.getLast().addVendor(vendor_in_question);
            }
            else{
                ListIterator<Group> group_iterator = group_list.listIterator(); //declare iterator over the group_list

                while(group_iterator.hasNext()){                 //iterate over the groups

                    Group group_in_question = group_iterator.next();
                    boolean belong = group_in_question.belong_to(vendor_in_question);

                    if(belong){
                        vendor_in_question.setGroup_name(group_in_question.getName());
                        break;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! IF WE WANT TO BE IN ONE GROUP ONLY KEEP GOING IF NOT CONTINUE
                    }
                }

                if (vendor_in_question.getGroup_name() == null){
                    groupnumber ++;
                    String new_group_name = "group".concat(String.valueOf(groupnumber));
                    Group at_the_end = create_group(new_group_name);  //create a group
                    vendor_in_question.setGroup_name(at_the_end.getName());
                    at_the_end.addVendor(vendor_in_question);         //add vendor at the end of the group
                    group_list.addLast(at_the_end);                   //add group last at the end of the group list
                }

            }

        }


   // ------------------------------- AT THIS POINT ALL THE GROUPS SHOULD BE CREATED ALREADY ---------------------------------------
   // ------------------------------  FROM HERE WE START ALGORITHM TO CREATE THE GROUPED CSV ---------------------------------------

        File file = new File("C:\\Users\\adiego8\\Desktop\\Vendor Fraud Detection System\\Clean\\organized by groups test file.csv"); //create the .csv file to write in
        FileWriter filewriter = new FileWriter(file); //initialize the file writer
        CSVWriter writer = new CSVWriter(filewriter); //write the csv
        ListIterator<Group> group_iterator = group_list.listIterator(); // list iterator



        while(group_iterator.hasNext()){
            Group group_to_iterate = group_iterator.next();

            if(group_to_iterate.getName() != "headers"){
                Hashtable<Long,Vendor> table = group_to_iterate.getGroup();  //this is the hashtable of the group being iterated
                int table_size = table.size();                               //get the size of the table group to get the information from
                for(long i = 0 ; i < table_size ; i++){
                    Vendor vendor_to_csv = table.get(i);
                    String[] values = {String.valueOf(vendor_to_csv.getId()),vendor_to_csv.getName(),vendor_to_csv.getTaxid(),vendor_to_csv.getZip(),vendor_to_csv.getGroup_name()};
                    writer.writeNext(values);
                }

            }
            else{                 ///////////////////////////////////////////////////////////////////// CHECK THE PROBLEM THAT IS GIVING NULL
                Hashtable<Long,Vendor> table = group_to_iterate.getGroup();
                String[] header = {String.valueOf(table.get(Long.parseLong("0")).getId()),table.get(Long.parseLong("0")).getName(),table.get(Long.parseLong("0")).getTaxid(),table.get(Long.parseLong("0")).getZip(),"Group"};
                writer.writeNext(header);

            }

        }

        writer.close();
    //------------------------------------------------------- END OF THE PROGRAM -----------------------------------------------------------

    }


    // ********** THIS IS THE FUNCTION TO CREATE A NEW GROUP WHEN NEED ******************************
    private static Group create_group(String name){
        Group new_group = new Group(name);
        return new_group;
    }




}
