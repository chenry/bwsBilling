#!/bin/perl
$tableName = "Customer";
@fieldNames = ("first_name", "last_name", "company_name", "address1", "address2", "city", "state", "zipcode", "alt_address1", "alt_address2", "alt_city", "alt_state", "alt_zipcode", "comment", "rental_charge", "billing_cycle_id", "install_date", "creation_date", "last_update_stamp", "close_date");
@fieldTypes = ("string","string","string","string","string","string","string","string","string","string","string","string","string","string","integer","integer","date","date","date","date");


for ($i = 0; $i < 3; $i++){
    $insert = "INSERT INTO $tableName (";
    $total = @fieldNames;
    $counter = 0;
    foreach $column (@fieldNames){
        $insert = $insert ."$column";
        if ($counter != $total-1){
            $insert = $insert . ", ";
        } else{
            $insert = $insert . ") \n";
        }
        $counter++;
    }
    $insert = $insert ." Values (";
    $counter = 0;
    foreach $column (@fieldNames){
        if (@fieldTypes[$counter] eq "string"){
            $insert = $insert ."@fieldNames[$counter]$i";
        } elsif (@fieldTypes[$counter] eq "integer"){
            $insert = $insert ."$i$i$i$i";
        } elsif (@fieldTypes[$counter] eq "date"){
            $insert = $insert ."#$i/$i/2000#";
        }
        if ($counter != $total-1){
            $insert = $insert . ", ";
        } else{
            $insert = $insert . ") ";
        }
        $counter++;
    }
    $insert = $insert . "\n";
    print $insert;
}