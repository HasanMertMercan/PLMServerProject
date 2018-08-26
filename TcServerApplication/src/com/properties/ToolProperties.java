package com.properties;


public class ToolProperties {
	
	private String _toolId;
	private String _toolName;
	private String _toolFileName;
	private String _revisionId;
	private String _toolCADFile;
	
	public String getToolId() 
	{
		return _toolId;
	}
	
	public void setToolId(String toolId) 
	{
		this._toolId = toolId;
	}
	
	public String getToolName() 
	{
		return _toolName;
	}
	
	public void setToolName(String toolName) 
	{
		this._toolName = toolName;
	}
	
	public String getToolCADFile() 
	{
		return _toolCADFile;
	}
	
	public void setToolCADFile(String toolCADFile) 
	{
		this._toolCADFile = toolCADFile;
	}

	public String get_toolFileName() {
		return _toolFileName;
	}

	public void set_toolFileName(String toolFileName) {
		this._toolFileName = toolFileName;
	}

	public String get_revisionId() {
		return _revisionId;
	}

	public void set_revisionId(String revisionId) {
		this._revisionId = revisionId;
	}
	

}
