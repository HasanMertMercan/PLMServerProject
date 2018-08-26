package com.properties;

import java.util.ArrayList;

public class OperationProperties {
	
	private String _id;
	private String _name;
	private String _profession;
	private String _fileName;
	private String _revisionId;
	private ArrayList<String> _instructions;
	private String _instructionsListId;
	private String _toolIds;
	private String _state;
	
	public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }
    public String getId() {
        return _id;
    }
    public void setId(String id) {
        this._id = id;
    }
    public String getProfession() {
        return _profession;
    }
    public void setProfession(String profession) {
        this._profession = profession;
    }
    
    public String getFileName() 
    {
    	return _fileName;
    }
    
    public void setFileName(String filename) 
    {
    	this._fileName = filename;
    }
    
    public String getRevisionId() 
    {
    	return _revisionId;
    }
    
    public void setRevisionId(String revisionId) 
    {
    	this._revisionId = revisionId;
    }
    
    public String getToolIds() 
    {
    	return _toolIds;
    }
    
    public void setToolIds(String toolIds) 
    {
    	this._toolIds = toolIds;
    }
	public String get_state() {
		return _state;
	}
	public void set_state(String state) {
		this._state = state;
	}
	public ArrayList<String> get_instructions() {
		return _instructions;
	}
	public void set_instructions(ArrayList<String> _instructions) {
		this._instructions = _instructions;
	}
	public String getInstructionsListId() {
		return _instructionsListId;
	}
	public void setInstructionsListId(String _instructionsListId) {
		this._instructionsListId = _instructionsListId;
	}

}
