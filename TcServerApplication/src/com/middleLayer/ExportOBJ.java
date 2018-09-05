package com.middleLayer;

import de.raida.cad.dex.CADExportContainer;
import de.raida.cad.dex.cadexport.obj.OBJExporterImplementation;
import de.raida.cad.dex.cadimport.jt.JTImporterImplementation;
import de.raida.cad.dex.interfaces.CADExportInterface;
import de.raida.cad.dex.interfaces.CADImportInterface;

/**
 * Example class for converting a CAD file into another format.
 * @author  <a href="mailto:j.raida@gmx.net">Johannes Raida</a>
 * @version 1.0
 */
public class ExportOBJ {
	/**
	 * Constructor, creating the scene.
	 * @param fileName File name
	 */
	
	private String finalName;
	public ExportOBJ(String fileName){ //filename is the full path of the .jt file
		try {
			// Load a JT file
			CADImportInterface cadImportInterface = new JTImporterImplementation();
			cadImportInterface.importFile(fileName, null);
			
			String newDirection = "C:\\CADConversion";
			String[] files = fileName.split("\\\\");
			int i = files.length;
			String newfilename = newDirection + "\\\\" + files[i-1];
			
			// Write the OBJ file
			CADExportInterface cadExportInterface = new OBJExporterImplementation();
			cadExportInterface.exportFile(newfilename.substring(0, newfilename.length()-3) + ".obj", new CADExportContainer(cadImportInterface, fileName), null);
			finalName = newfilename.substring(0, newfilename.length()-3) + ".obj";
			
		} catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public String getFinalFileName() 
	{
		return finalName;
	}
}
