package com.middleLayer;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.teamcenter.soa.exceptions.CanceledOperationException;
import com.teamcenter.soa.exceptions.NotLoadedException;
import com.teamcenter.soa.client.model.strong.User;
import com.communication.CommunicationMain;
import com.communication.CreateTeamcenterConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.optimisation.City;
import com.optimisation.OptimiseRootInsidePlant;
import com.optimisation.SelectMachinesWithYellowErrorState;
import com.properties.AcceptedJsonObject;
import com.properties.LoginProperties;
import com.properties.MachineProperties;
import com.properties.UpdateInstructionListProperties;

public class Main {
	
	public static void main(String[] args) throws IOException, NotLoadedException, CanceledOperationException, InterruptedException {
		// TODO Auto-generated method stub
		
		/*GetMachineDataFromTeamcenter getMachineDataFromTeamcenter = new GetMachineDataFromTeamcenter("C:/Teamcenter �nemli/Zuccarello.obj");
		int size = getMachineDataFromTeamcenter.getMachineData().size();
		for(int i = 0; i < size; i++) 
		{
			System.out.println("Machine CAD Data " + i);
			System.out.println(getMachineDataFromTeamcenter.getMachineData().get(i).getMachineCADFile());
		}*/
		
		new Main();	
		/*String factoryId = "007660";
		GetFactoryDataFromTeamcenter dataFromTeamcenter = new GetFactoryDataFromTeamcenter(factoryId);
		ArrayList<MachineProperties> factoryMachines = dataFromTeamcenter.getMachineIds();
		int size = factoryMachines.size();
		for(int i = 0; i < size; i++) 
		{
			System.out.println(factoryMachines.get(i).getId());
		}*/
		
		/*@SuppressWarnings("resource")
		ServerSocket ClientCommunicationSocket = new ServerSocket(35010);
		Socket socket = ClientCommunicationSocket.accept();
		CADConverter cadConverter = new CADConverter("C:\\Teamcenter �nemli\\jtfiles\\gokart_main_assy.jt");
		
		System.out.println(cadConverter.getCADFileFinal());
	    File file = new File(cadConverter.getCADFileFinal());
		PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

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
		ClientCommunicationSocket.close();*/
	}
	

	private boolean isSessionCreated;
	
	public Main() throws IOException, NotLoadedException, CanceledOperationException, InterruptedException
	{
		
			@SuppressWarnings("resource")
			ServerSocket ClientCommunicationSocket = new ServerSocket(35010);

			ArrayList<MachineProperties> machineList = new ArrayList<MachineProperties>();

			ArrayList<MachineProperties> machinesWithError = new ArrayList<MachineProperties>();

			ArrayList<MachineProperties> completedMachineList = new ArrayList<MachineProperties>();

			ArrayList<MachineProperties> yellowStateMachines = new ArrayList<MachineProperties>();
			ArrayList<MachineProperties> machinesWithRedError = new ArrayList<MachineProperties>();
			ArrayList<MachineProperties> machinesWithGreenError = new ArrayList<MachineProperties>();

			ArrayList<City> optimisedRootForGreenCities = new ArrayList<City>();
			ArrayList<City> optimisedRootForRedCities = new ArrayList<City>();

			ArrayList<MachineProperties> optimisedRootForGreenMachines = new ArrayList<MachineProperties>();
			ArrayList<MachineProperties> optimisedRootForRedMachines = new ArrayList<MachineProperties>();

			MachineProperties machineDetails = new MachineProperties();
			
			UpdateInstructionListProperties instructionListProperties = new UpdateInstructionListProperties();
			
			//this loop is infinite in order to keep the server always ready
			while(true) 
			{
				System.out.println("Waiting a client");
				Socket socket = ClientCommunicationSocket.accept();
				
				InputStream input = socket.getInputStream();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			    String message = reader.readLine();    // reads a line of text
			    
			    //Deserilaize Received Message
			    Gson gson = new GsonBuilder().create();
			    AcceptedJsonObject acceptedJsonObject =  gson.fromJson(message, AcceptedJsonObject.class);
			    
			    String jsonObjectFlag = acceptedJsonObject.getJsonFlag();
			    String jsonObjectMain = acceptedJsonObject.getJsonObject();
			    
				if(jsonObjectFlag.equals("LoginRequest")) 
				{
					CommunicationMain communicationMain = new CommunicationMain();
					LoginProperties loginProperties = new LoginProperties();
					loginProperties = communicationMain.receiveLoginArguments(jsonObjectMain);
					
					String userName = loginProperties.getUsername();
					String password = loginProperties.getPassword();
					CreateTeamcenterConnection createTeamcenterConnection = new CreateTeamcenterConnection(userName, password);
					User user = createTeamcenterConnection.getUser();
					//createTeamcenterSession("http://teamcenter1.hs.local:8080/tc", userName, password);
					if(user.get_userid().equals(userName) /*userName.equals("e1") && password.equals("123hm123")*/) 
					{
						isSessionCreated = true;
					}
					else 
					{
						isSessionCreated = false;
						System.out.println("There is no teamcenter session!");
					}
					Socket responseSocket = ClientCommunicationSocket.accept();
					
					//Send LoginResponse to Client
					communicationMain.SendLoginResponse(isSessionCreated, responseSocket);
				}
				else if(jsonObjectFlag.equals("FactoryQR")) 
				{
					//Get Machines from teamcenter based on the qrCode of the plant
					CommunicationMain communicationMain = new CommunicationMain();
					machineList = communicationMain.receiveFactoryQrCode(jsonObjectMain);
					
					//AssignRandomError
					AssignRandomError assignRandomError = new AssignRandomError(machineList);
					machinesWithError = assignRandomError.getMachineErrorList();
					
					//AssignOperations Based on Errors
					AssignOperation assignOperation = new AssignOperation(machinesWithError);
					completedMachineList = assignOperation.getCompletedMachineList();
					
					//SelectYellowStateMachines
					SelectMachinesWithYellowErrorState smwyes = new SelectMachinesWithYellowErrorState(completedMachineList);
					yellowStateMachines = smwyes.getYellowStateMachines();
					machinesWithGreenError = smwyes.getGreenStateMachines();
					machinesWithRedError = smwyes.getRedStateMachines();

					Socket responseSocket = ClientCommunicationSocket.accept();
					
					//Send Yellow State Machines to Client
					communicationMain.SendYellowStateMachines(yellowStateMachines, responseSocket);
				}
				else if(jsonObjectFlag.equals("OptimisationRequest")) 
				{
					//Get machines which has updated status
					CommunicationMain communicationMain = new CommunicationMain();
					ArrayList<MachineProperties> updatedMachineList = new ArrayList<MachineProperties>();
					updatedMachineList = communicationMain.receiveUpdatedMachineList(jsonObjectMain);
					int size = updatedMachineList.size();
					
					//Distribute the updated list
					for(int i = 0; i < size; i++) 
					{
						if(updatedMachineList.get(i).getErrorState().equals("2")) 
						{
							machinesWithRedError.add(updatedMachineList.get(i));
						}
						else if(updatedMachineList.get(i).getErrorState().equals("0")) 
						{
							machinesWithGreenError.add(updatedMachineList.get(i));
						}
					}

					//Send Updated List to Optimisation
					OptimiseRootInsidePlant optimiseRootInsidePlant = new OptimiseRootInsidePlant(machinesWithGreenError, 0);
					OptimiseRootInsidePlant optimiseRootInsidePlant2 = new OptimiseRootInsidePlant(machinesWithRedError, 2);
					optimisedRootForGreenCities = optimiseRootInsidePlant.getOptimisedRootMachineList();
					optimisedRootForRedCities = optimiseRootInsidePlant2.getOptimisedRootMachineList();
					int optimisedRootForGreenCitiesSize = optimisedRootForGreenCities.size();
					int optimisedRootForRedCitiesSize = optimisedRootForRedCities.size();
					
					//Convert type to MachineProperties from City
					for(int i = 0; i < optimisedRootForGreenCitiesSize; i++) 
					{
						MachineProperties machineProperties = new MachineProperties();
						machineProperties.setId(optimisedRootForGreenCities.get(i).getMachineId());
						machineProperties.setxAxis(Integer.toString(optimisedRootForGreenCities.get(i).getX()));
						machineProperties.setyAxis(Integer.toString(optimisedRootForGreenCities.get(i).getY()));
						optimisedRootForGreenMachines.add(machineProperties);
					}
					for(int i = 0; i < optimisedRootForRedCitiesSize; i++) 
					{
						MachineProperties machineProperties = new MachineProperties();
						machineProperties.setId(optimisedRootForRedCities.get(i).getMachineId());
						machineProperties.setxAxis(Integer.toString(optimisedRootForRedCities.get(i).getX()));
						machineProperties.setyAxis(Integer.toString(optimisedRootForRedCities.get(i).getY()));
						optimisedRootForRedMachines.add(machineProperties);
					}
					
					
					//Complete the optimisedList with all the informations
					int optimisedRootForGreenMachinesSize = optimisedRootForGreenMachines.size();
					int completedMachineListSize = completedMachineList.size();
					int optimisedRootForRedMachinesSize = optimisedRootForRedMachines.size();
					
					for(int i = 0; i < optimisedRootForGreenMachinesSize; i++) 
					{
						for(int j = 0; j < completedMachineListSize; j++) 
						{
							if(completedMachineList.get(j).getId().equals(optimisedRootForGreenMachines.get(i).getId())) 
							{
								optimisedRootForGreenMachines.get(i).setOperationToDo(completedMachineList.get(j).getOperationToDo());
							}
						}
					}
					
					for(int i = 0; i < optimisedRootForRedMachinesSize; i++) 
					{
						for(int j = 0; j < completedMachineListSize; j++) 
						{
							if(completedMachineList.get(j).getId().equals(optimisedRootForRedMachines.get(i).getId())) 
							{
								optimisedRootForRedMachines.get(i).setOperationToDo(completedMachineList.get(j).getOperationToDo());
							}
						}
					}
				
					Socket responseSocket = ClientCommunicationSocket.accept();
					
					//Send Fully Optimised List to Client
					CommunicationMain sendGreenOptimisedListToClient = new CommunicationMain();
					CommunicationMain sendRedOprimisedListTiClient = new CommunicationMain();
					sendGreenOptimisedListToClient.SendOptimisedList(optimisedRootForGreenMachines, responseSocket);
					
					Socket responseSocket2 = ClientCommunicationSocket.accept();
					
					sendRedOprimisedListTiClient.SendOptimisedList(optimisedRootForRedMachines, responseSocket2);
				}
				else if(jsonObjectFlag.equals("MachineQR")) 
				{
					//GetMachineQRCodeFromClient
					CommunicationMain communicationMain = new CommunicationMain();
					if(!completedMachineList.isEmpty()) 
					{
						machineDetails = communicationMain.receiveMachineQrCodeWithOptimisation(jsonObjectMain, completedMachineList);
					}
					else 
					{
						machineDetails = communicationMain.receiveMachineQrCodeWithoutOptimisation(jsonObjectMain);
					}
				
					Socket responseSocket = ClientCommunicationSocket.accept();
					
					//Send Machine Details to the client
					communicationMain.SendMachineDetailsToClient(machineDetails, responseSocket);
				
					Socket responseSocket2 = ClientCommunicationSocket.accept();
					
					communicationMain.SendCadModelToClient(machineDetails, responseSocket2);
				}
				else if(jsonObjectFlag.equals("UpdateInformationRequest")) 
				{
					//Receive List update request 
					CommunicationMain communicationMain = new CommunicationMain();
					instructionListProperties = communicationMain.UpdateRequestInstructionList(jsonObjectMain);
				
					Socket responseSocket = ClientCommunicationSocket.accept();
					
					//send the updated list back
					communicationMain.SendUpdatedInformationList(instructionListProperties, responseSocket);
				}
				else 
				{
					//error message
					continue;
				}
			}
	}
		
	

	
}
