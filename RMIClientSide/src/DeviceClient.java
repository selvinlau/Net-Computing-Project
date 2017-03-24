
import java.rmi.*;

import entity.Device;
 
public class DeviceClient {
	public static void main (String[] args) {
		DeviceInterface d;
		try {
  		        System.setSecurityManager(new SecurityManager());
			d = (DeviceInterface)Naming.lookup("rmi://localhost/RMIDevice");
			
			String result=d.addDevice("Server4",30.6,232323);
			System.out.println(result);
 
			}catch (Exception e) {
				System.out.println("HelloClient exception: " + e);
				}
		}
}