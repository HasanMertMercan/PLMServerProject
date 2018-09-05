package com.middleLayer;

import java.io.IOException;
import java.util.ArrayList;

import com.properties.MachineProperties;

public class GetMachineDataFromTeamcenter {
	
	//machineId will come from AR client
	
	private ArrayList<MachineProperties> machineData = new ArrayList<MachineProperties>();
	private MachineProperties machineProperties = new MachineProperties();
	
	public GetMachineDataFromTeamcenter(String machineId, String fileName, String revisionId) throws IOException, InterruptedException 
	{
		//TODO
		GetFileFromTeamcenter getFileFromTeamcenter = new GetFileFromTeamcenter(machineId, fileName, revisionId);
		String absoluteFileName = getFileFromTeamcenter.getFile().getAbsolutePath();
		CADConverter cadConverter = new CADConverter(absoluteFileName);
		machineData.get(0).setMachineCADFile(cadConverter.getCADFileFinal()); //This slot will be filled by String
	}
	
	//Test Constructor
	public GetMachineDataFromTeamcenter(String absoluteFileName) throws IOException, InterruptedException  
	{
		CADConverter cadConverter = new CADConverter(absoluteFileName);
		machineData.add(machineProperties);
		machineData.get(0).setMachineCADFile(cadConverter.getCADFileFinal());
	}
	
	public ArrayList<MachineProperties> getMachineData()
	{
		return machineData;
	}
	
	

}
