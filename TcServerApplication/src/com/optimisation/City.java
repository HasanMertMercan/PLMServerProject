package com.optimisation;

public class City {
	String _machineId;
	int x;
    int y;
    String _errorState;
    
    // Constructs a city at chosen x, y location
    public City(String machineId, int x, int y, String errorState){
    	this._machineId = machineId;
        this.x = x;
        this.y = y;
        this._errorState = errorState;
    }
    
    public String getMachineId() 
    {
    	return this._machineId;
    }
    
    // Gets city's x coordinate
    public int getX(){
        return this.x;
    }
    
    // Gets city's y coordinate
    public int getY(){
        return this.y;
    }
    
    //Get machine's error state
    public String getErrorState() 
    {
    	return this._errorState;
    }
    
    // Gets the distance to given city
    public double distanceTo(City city){
        int xDistance = Math.abs(getX() - city.getX());
        int yDistance = Math.abs(getY() - city.getY());
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );
        
        return distance;
    }
    
    @Override
    public String toString(){
        return getX()+", "+getY();
    }
	

}
