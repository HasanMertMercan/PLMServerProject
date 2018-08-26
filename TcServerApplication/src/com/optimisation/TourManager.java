package com.optimisation;

import java.util.ArrayList;

public class TourManager {
	// Holds our cities
    private static ArrayList<City> destinationCities = new ArrayList<City>();

    // Adds a destination city
    // This method erases the machines with error states 
    public static void addCity(City city) {
    		destinationCities.add(city);
    
    }

	// Get cities without error
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }
    
    
    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
    
    //Clear the destinationCities ArrayList whenever start to new optimisation
    public static void clearDestinationCities() 
    {
    	destinationCities.clear();
    }
    

}
