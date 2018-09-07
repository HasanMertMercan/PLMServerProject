package com.communication;

import java.io.IOException;

import com.middleLayer.Main;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.soa.client.model.strong.User;
import com.teamcenter.soa.common.ObjectPropertyPolicy;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class CreateTeamcenterConnection {
	
	private AppXSession session;
	private User user;
	
	public CreateTeamcenterConnection(String username, String password) throws InvalidCredentialsException 
	{
		createTeamcenterSession(username, password);
	}
	
	private void createTeamcenterSession(String username, String password) throws InvalidCredentialsException
	{
		String host = "";
		System.out.println("Starting a session on " + host); // prints on screen the host for the session
		this.session = new AppXSession (host);
		System.out.println("Logging in with username " + username); // prints on screen the session's user
		this.user = (User) this.session.login(username, password); // uses username and password to log into Teamcenter
		
		try
		{
			System.out.println("logged in as user " + this.user.get_user_name());
		}
		catch (NotLoadedException e)
		{
			throw new RuntimeException(e);
		}
		/** Get the session service and set the object property policy in Teamcenter */
		SessionService sessionService = SessionService.getService(AppXSession.getConnection());
		
		ObjectPropertyPolicy oppolicy = new ObjectPropertyPolicy();
		
		//TODO
		//Add necessary attirbutes to oppolicy.xml file. 
		String path = Main.class.getResource("/oppolicy.xml").toString();
		
		try
		{
			oppolicy.load(path);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		sessionService.setObjectPropertyPolicy(oppolicy);
		//this.fileManager = new FileManagementUtility(AppXSession.getConnection()); // Get and cache the file manager
	}
	
	public User getUser() 
	{
		return user;
	}

}
