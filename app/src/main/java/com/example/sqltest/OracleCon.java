package com.example.sqltest;
import android.util.Log;

import java.sql.*;
import java.util.ArrayList;

public class OracleCon {
    public static ArrayList<String> createQuery(String query){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Log.i("DRIVERS OK","DRIVERS OK");
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:ziibd12@//155.158.112.45:1521/oltpstud","ziibd12","haslo2015");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            StringBuilder columnsNames = new StringBuilder();
            for(int i=1;i<=columnsNumber;i++) {
                columnsNames.append(rsmd.getColumnName(i));
                columnsNames.append(" ");
            }
            arrayList.add(columnsNames.toString());
            while(rs.next()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i <= columnsNumber; i++) {
                    stringBuilder.append(rs.getString(i));
                    stringBuilder.append(" ");
                }
                arrayList.add(stringBuilder.toString());
            }
            con.close();
        }catch(Exception e){ e.printStackTrace();}

        return arrayList;
    }
}
