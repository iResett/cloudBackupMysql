package com.cloudcraft.mysqlbackup;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class Methods extends MySQL{

	public static void outfile(CommandSender p,String filename,String tablename) throws Exception{


            String query = "select * from "+tablename;
			
            printToCsv(filename, fetchDataFromDatabase(query));
            
			p.sendMessage("§6Tabela §f"+tablename+" §6salva com sucesso");
	}
	
	public static List<String> allTables(){
		ResultSet rs;
		List<String> ll = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '"+db+"'");
			rs = stm.executeQuery();


			while (rs.next()) {
			  String tableName = rs.getString("TABLE_NAME");

			  ll.add(tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ll;
	}
	
	public static List<String> fetchDataFromDatabase(String selectQuery) throws  Exception{
        List<String> resultSetArray = new ArrayList<>();
		try {


            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            int numCols = rs.getMetaData().getColumnCount();

            while(rs.next()) {
                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= numCols; i++) {
                    sb.append(String.format(String.valueOf(rs.getString(i))) + ",");
                }
                
                resultSetArray.add(sb.toString().substring(0,sb.toString().length() - 1));

            }

        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return resultSetArray;
    }
	
	public static void printToCsv(String file_name,List<String> resultArray) throws Exception{

        File csvOutputFile = new File(file_name);
        FileWriter fileWriter = new FileWriter(csvOutputFile, false);


        for(String mapping : resultArray) {
            fileWriter.write(mapping + "\n");
         }

        fileWriter.close();

    }
	
}
