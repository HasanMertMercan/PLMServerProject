package com.middleLayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Arguments {
	
	private Map<String, String> options = new LinkedHashMap<>();
	
	public String host(){ return options.get("host"); }
	public String username(){ return options.get("username"); }
	public String password(){ return options.get("password"); }
	
	public static Arguments Parse(String[] args)
	{
		Arguments arguments = new Arguments();
		int current = 0;
		while( current < args.length )
		{
			String argument = args[current++];
			switch(argument)
			{
				case "-h": case "--host":
				if( current == args.length ) throw new RuntimeException("host was supplied without a value");
					arguments.storeOption("host", args[current++]);
					break;
				case "-u": case "--username":
					if( current == args.length ) throw new RuntimeException("username was supplied without a value");
					arguments.storeOption("username", args[current++]);
				break;
				case "-p": case "--password":
					if( current == args.length ) throw new RuntimeException("password was supplied without a value");
					arguments.storeOption("password", args[current++]);
				break;
				default:
				throw new RuntimeException("unknown argument "+argument);
			}
		}
		
		List<String> mandatoryOptions = new ArrayList<>();
		if ( !arguments.options.containsKey("host") )
		{
			mandatoryOptions.add("host");
		}
		if ( !arguments.options.containsKey("username") )
		{
			mandatoryOptions.add("username");
		}
		if ( !arguments.options.containsKey("password") )
		{
			mandatoryOptions.add("password");
		}
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		for( String key : mandatoryOptions )
		{
			String value = System.getProperty(key, "");
			if( !value.isEmpty() )
			{
				arguments.storeOption(key,value);
				System.out.format("%s: using system property (%s)\n", key, value);
				continue;
			}
			while(!arguments.options.containsKey(key))
			{
				System.out.format("%s: ", key);
				value = scanner.nextLine();
				if( value.isEmpty() )
				{
					System.out.println("Invalid argument entered. Try again.");
				}
				else
				{
					arguments.storeOption(key,value);
					break;
				}
			}
		}
		return arguments;
	}
		
	public void storeOption(final String key, final String value)
	{
		if( value == null || value.isEmpty() )
		{
			throw new RuntimeException("The option "+key+" cannot be empty");
		}
		this.options.put(key, value);
	}
	

}
