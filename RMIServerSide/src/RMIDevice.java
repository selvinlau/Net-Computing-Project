
import java.rmi.*;
import java.rmi.server.*;

import dao.DeviceDAO;
import entity.Device;

public class RMIDevice extends UnicastRemoteObject implements DeviceInterface{

	DeviceDAO dDao = new DeviceDAO();
	
	public RMIDevice () throws RemoteException {  
		
	}


	public String addDevice(String name,double temp, long timestamp) throws RemoteException {
		Device device = new Device(name,temp,timestamp);
		return dDao.addDevice(device);
	}
	
	
}
