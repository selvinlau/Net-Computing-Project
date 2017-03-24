
import java.rmi.*;
import java.rmi.server.*;

import dao.DeviceDAO;

public class RMIDeviceServer {
	public static void main (String[] argv) {
		
		try {
			System.setSecurityManager(new SecurityManager());

			RMIDevice d = new RMIDevice();			   		   
			Naming.rebind("rmi://localhost/RMIDevice", d);

			System.out.println("Device Server is ready.");
		}catch (Exception e) {
			System.out.println("Device Server failed: " + e);
		}
	}
}
