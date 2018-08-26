package com.middleLayer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

//import org.apache.commons.io.FileUtils;

public class CADConverter {
	
	private String CADtxtFile;
	private String CADFileFinal;
	private File oldFile, actualFile;
	private String newDirection, newFileName;
	private String arr;
	
	//This method will be called whenever a jt file taken from the teamcenter (Machine data, tool data)
	//This method converts the .jt file to .obj file and saves it to given directory
	public CADConverter(String fileName) throws IOException 
	{
		//Burayi test ederken iyice incele
		oldFile = new File(fileName);
		newDirection = "C:/CADConversion";
		String[] files = fileName.split("/");
		int i = files.length;
		String filename = newDirection + "/" + files[i-1];
		actualFile = new File(filename);
		
		Files.copy(oldFile.toPath(), actualFile.toPath());
		new ExportOBJ(actualFile.getAbsolutePath()); //This method creates .obj file in the same path!
		//String filePath = actualFile.getAbsolutePath();
		
		arr = filename.substring(0, filename.length()-4);
		
		//arr = filename.split(".");
		
		newFileName = arr + ".obj"; 
		//CADtxtFile = changeFileExtension(newFileName); //The new file which comes from converter will be the parameter of this line
		//CADFileFinal = readCADtxtFile(CADtxtFile); //This is the final string for Objects CAD data
		CADFileFinal = newFileName;
		
		
	}
	
	//This method allows us to convert file to .txt
	private String changeFileExtension(String OBJFileName	/*This will be the filename and path comes from OBJ converter*/) 
	{
		String extension = OBJFileName.substring(0, OBJFileName.length()-4);
		String txtFile = extension + ".txt";
		File newTXTFile = new File(txtFile);
		actualFile.renameTo(newTXTFile);
		OBJFileName = txtFile;
		
		return OBJFileName;
	}
	
	//txt okumak uzun suruyor. File transferini dogrudan yap
	private String readCADtxtFile(String TxtFilePath) throws IOException 
	{
		String objText = "";
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader(TxtFilePath)); 
		while((line = br.readLine()) != null) 
		{
			objText += line;
			objText += "\n";
		}
		objText = objText.substring(0, objText.length() - 2);
		br.close();
		
		return objText;
	}
	
	public String getCADFileFinal() 
	{
		return CADFileFinal;
	}
}
