package com.middleLayer;

import com.properties.MachineProperties;

public class FactoryProperties {
	
	private String _factoryId;
	private MachineProperties _factoryMachines;
	
	public MachineProperties getFactoryMachines() {
		return _factoryMachines;
	}
	public void setFactoryMachines(MachineProperties factoryMachines) {
		this._factoryMachines = factoryMachines;
	}
	public String getFactoryId() {
		return _factoryId;
	}
	public void setFactoryId(String _factoryId) {
		this._factoryId = _factoryId;
	}
	

}
