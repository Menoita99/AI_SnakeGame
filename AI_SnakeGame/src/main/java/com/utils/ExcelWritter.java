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

	public static String root = "Statistics/";
	
    public static void write(ArrayList<String> generations, ArrayList<String> scores, ArrayList<String> fitness,ArrayList<String> bestScorePerGen, ArrayList<String> bestFitnessPerGen, String filename){
    	String [] columns = {"Generation", "Score", "Fitness", "Best Score", "Best Fitness"};
    	File f = new File(root + filename+".csv");
        try{
        	Workbook workbook = new HSSFWorkbook();
        	Sheet sh = workbook.createSheet(filename);
        	Row headerRow = sh.createRow(0);
        	for (int i = 0; i < 5; i++) {
        		Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
			}
        	
        	for(int i = 0; i<generations.size(); i++) {
        		Row row = sh.createRow(i+1);
        		row.createCell(0).setCellValue(generations.get(i));
        		row.createCell(1).setCellValue(scores.get(i));
        		row.createCell(2).setCellValue(fitness.get(i));
        		row.createCell(3).setCellValue(bestScorePerGen.get(i));
        		row.createCell(4).setCellValue(bestFitnessPerGen.get(i));
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
