package com.middleLayer;

import java.io.IOException;
import java.util.ArrayList;

import com.properties.ToolProperties;

public class GetToolDataFromTeamcenter {
	
	//TODO
	//Bring tool CAD file for specified toolId.
	//toolId comes from ToolFinder
	//This method will be called in ToolFinder
	
	private ArrayList<ToolProperties> toolData = new ArrayList<ToolProperties>();
	
	public GetToolDataFromTeamcenter(String toolId, String fileName, String revisionId) throws IOException 
	{
		GetFileFromTeamcenter getFileFromTeamcenter = new GetFileFromTeamcenter(toolId, fileName, revisionId);
		String absoluteFileName = getFileFromTeamcenter.getFile().getAbsolutePath();
		CADConverter cadConverter = new CADConverter(absoluteFileName);
		toolData.get(0).setToolCADFile(cadConverter.getCADFileFinal()); //This slot will be filled by String
	}

	public ArrayList<ToolProperties> getToolData()
	{
		return toolData;
	}
}
