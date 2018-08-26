package com.middleLayer;

import com.properties.UserProperties;

import java.util.ArrayList;
import java.util.List;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.exceptions.NotLoadedException;
import com.xmlreaders.XMLReaderUser;

public class UserDetails {
	
	private User user;
	private List<UserProperties> userList = new ArrayList<UserProperties>();
	private XMLReaderUser xmlReaderUser = new XMLReaderUser();
	private ArrayList<UserProperties> currentUser = new ArrayList<UserProperties>();

	public UserDetails() 
	{
		userList = xmlReaderUser.getUserPropertiesList();
		int size = xmlReaderUser.getUserPropertiesList().size();
		
		for(int i = 0; i < size; i++) 
		{
			try {
				if(userList.get(i).getId() == user.get_user_id()) 
				{
					currentUser.add(userList.get(i));
				}
			} catch (NotLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("User " + user + "could not load!");
			}
		}
		
	}
	
	//Test Constructor
	public UserDetails(String userID) 
	{
		userList = xmlReaderUser.getUserPropertiesList();
		int size = xmlReaderUser.getUserPropertiesList().size();
		
		for(int i = 0; i < size; i++) 
		{
			if(userList.get(i).getId().equals(userID)) 
			{
				currentUser.add(userList.get(i));
				break;
			}
		}
	}
	
	public ArrayList<UserProperties> getCurrentUser()
	{
		return currentUser;
	}
}
