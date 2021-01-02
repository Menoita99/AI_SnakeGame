package com.utils;

import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;



public class ExcelWritter {

	
	
    public static void write(ArrayList<String> generations, ArrayList<String> scores, ArrayList<String> fitness){
    	String [] columns = {"Generation", "Score", "Fitness"};
    	
        try{
        	Workbook workbook = new HSSFWorkbook();
        	Sheet sh = workbook.createSheet("Statistics");
        	Row headerRow = sh.createRow(0);
        	for (int i = 0; i < 3; i++) {
        		Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
			}
        	
        	for(int i = 0; i<generations.size(); i++) {
        		Row row = sh.createRow(i+1);
        		row.createCell(0).setCellValue(generations.get(i));
        		row.createCell(1).setCellValue(scores.get(i));
        		row.createCell(2).setCellValue(fitness.get(i));
        	}
        	
        	FileOutputStream fileOut = new FileOutputStream("statistics.csv");
        	workbook.write(fileOut);
        	workbook.close();
        	fileOut.close();
        	
        	System.out.println("Excel file generated");
        	
        }catch (Exception e) {
        	System.out.println("Error on writting excel");
		}
      
    }
}
