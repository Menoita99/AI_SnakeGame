package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;



public class ExcelWritter {

	public static String root = "src/main/resources";
	
    public static void write(ArrayList<String> generations, ArrayList<String> scores, ArrayList<String> fitness, String filename){
    	String [] columns = {"Generation", "Score", "Fitness"};
    	File f = new File(filename+".csv");
        try{
        	Workbook workbook = new HSSFWorkbook();
        	Sheet sh = workbook.createSheet(filename);
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
        
			FileOutputStream fileOut = new FileOutputStream(f);
        	workbook.write(fileOut);
        	workbook.close();
        	fileOut.close();
        	
        	System.out.println("Excel file generated on path: "+f.getPath());
        }catch (Exception e) {
        	System.out.println("Error on writting excel on path: "+f.getPath());
		}
    }
}
