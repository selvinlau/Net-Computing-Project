
import java.rmi.*;

public interface DeviceInterface extends Remote{
	public String addDevice(String name,double temp, int timestamp) throws RemoteException;
}
