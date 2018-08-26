package com.communication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.middleLayer.GetFactoryDataFromTeamcenter;
import com.middleLayer.MachineDetails;
import com.middleLayer.OperationDetails;
import com.properties.LoginProperties;
import com.properties.MachineProperties;
import com.properties.UpdateInstructionListProperties;
import com.teamcenter.soa.exceptions.NotLoadedException;


public class CommunicationMain {
	
	private ArrayList<MachineProperties> machineList = new ArrayList<MachineProperties>();

	public CommunicationMain() throws IOException 
	{
		
	}
	
	public ArrayList<MachineProperties> receiveUpdatedMachineList(String JsonObjectString) throws IOException
	{
		//Check this part!!!
		//Deserilaize Login Arguments
	     Gson gson = new GsonBuilder().create();
	     @SuppressWarnings("unchecked")
		ArrayList<MachineProperties> updatedMachineList = gson.fromJson(JsonObjectString, ArrayList.class);
		
		return updatedMachineList;
	}
	
	public ArrayList<MachineProperties> receiveFactoryQrCode(Socket socket) throws IOException
	{
		InputStream input = socket.getInputStream();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    String factoryId = reader.readLine();    // reads a line of text
		
		GetFactoryDataFromTeamcenter getFactoryDataFromTeamcenter = new GetFactoryDataFromTeamcenter(factoryId);
		machineList = getFactoryDataFromTeamcenter.getMachineIds();
		return machineList;		
	}
	
	public MachineProperties receiveMachineQrCodeWithOptimisation(Socket socket, ArrayList<MachineProperties> completedMachineList) throws IOException, NotLoadedException 
	{
		InputStream input = socket.getInputStream();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    String machineId = reader.readLine();    // reads a line of text
		//Bring associated machine with operation details and CAD data
		MachineDetails getMachineDetails = new MachineDetails(machineId, completedMachineList);
		MachineProperties machineDetails = getMachineDetails.getCurrentMachine().get(0);
		return machineDetails;
	}
	
	public MachineProperties receiveMachineQrCodeWithoutOptimisation(Socket socket) throws IOException
	{
		InputStream input = socket.getInputStream();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    String machineId = reader.readLine();    // reads a line of text
		
		MachineDetails getMachineDetails = new MachineDetails(machineId);
		MachineProperties machineDetails = getMachineDetails.getCurrentMachine().get(0);
		return machineDetails;
	}
	
	public void SendMachineDetailsToClient(MachineProperties machineDetails, Socket socket) throws IOException 
	{		
		OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);
	    
	    Gson gson = new GsonBuilder().create();
	    String input = gson.toJson(machineDetails);
	    writer.println(input);
	}
	
	public void SendCadModelToClient(MachineProperties machineDetails, Socket socket) throws IOException 
	{
		String fileName = machineDetails.getFileName();//use this to identify and transfer the file
		
	    PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

	    File file = new File(fileName);

	    //send file length
	    out.println(file.length());

	    //read file to buffer
	    byte[] buffer = new byte[(int)file.length()];
	    DataInputStream dis = new DataInputStream(new FileInputStream(file));
	    dis.read(buffer, 0, buffer.length);

	    //send file
	    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
	    bos.write(buffer);
	    bos.flush();
	    dis.close();

	}
	
	public UpdateInstructionListProperties UpdateRequestInstructionList(Socket socket) throws IOException, NotLoadedException 
	{
		InputStream input = socket.getInputStream();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    String ClientString = reader.readLine();    // reads a line of text
	    
	  //Deserilaize Login Arguments
	    Gson gson = new GsonBuilder().create();
	    UpdateInstructionListProperties updateInstructionListProperties = gson.fromJson(ClientString, UpdateInstructionListProperties.class);
		
		String operationId = updateInstructionListProperties.getOperationId();
		String operationInstructionId = updateInstructionListProperties.getOperationInstructionListId();
		
		if(operationInstructionId.equals("3")) 
		{
			updateInstructionListProperties.setChangeListType(false);
		}
		else if(operationInstructionId.equals("2") || operationInstructionId.equals("1")) 
		{
            OperationDetails operationDetails = new OperationDetails(operationId, operationInstructionId);
            updateInstructionListProperties.setOperationInstructionList(operationDetails.getCurrentOperation().get(0).get_instructions());
            updateInstructionListProperties.setChangeListType(true);
		}
		
		return updateInstructionListProperties;
		
	}
	
	public void SendUpdatedInformationList(UpdateInstructionListProperties updateInstructionListProperties, Socket socket) throws IOException 
	{
		OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);
	    
	    Gson gson = new GsonBuilder().create();
	    String input = gson.toJson(updateInstructionListProperties);
	    writer.println(input);
	}
	
	public void SendOptimisedList(ArrayList<MachineProperties> OptimisedRootForMachines, Socket socket) throws IOException 
	{
		
		OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);
	    
	    Gson gson = new GsonBuilder().create();
	    String input = gson.toJson(OptimisedRootForMachines);
	    writer.println(input);
	}
	
	public void SendYellowStateMachines(ArrayList<MachineProperties> YellowStateMachines, Socket Socket) throws IOException 
	{
		//Socket socket = FactoryQrSocket.accept();
		
		OutputStream output = Socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);
	    
	    Gson gson = new GsonBuilder().create();
	    String input = gson.toJson(YellowStateMachines);
	    writer.println(input);
	}
	
	public LoginProperties receiveLoginArguments(String JsonLoginArguments) throws IOException
	{
		//Deserilaize Login Arguments
	     Gson gson = new GsonBuilder().create();
	     LoginProperties loginProperties = gson.fromJson(JsonLoginArguments, LoginProperties.class);
	       
	     System.out.println(loginProperties.getUsername());
	     System.out.println(loginProperties.getPassword());
	     
	     return loginProperties;
	}
	
	public void SendLoginResponse(boolean isSessionCreated, Socket LoginSocket) throws IOException 
	{
		//Create connection to AR
		//Send isSessionCreated to AR
		//ServerSocket serverSocket = new ServerSocket(35010);
		//Socket socket = LoginServerSocket.accept();
		
		OutputStream output = LoginSocket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);
		
		if(isSessionCreated == true) 
		{
		    writer.println("1"); //1 stands for true
		}
		else 
		{
			writer.println("0"); //0 stands for false
		}
		
    }
		
		

}
