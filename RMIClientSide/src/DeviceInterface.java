
import java.rmi.*;

public interface DeviceInterface extends Remote{
	public String addDevice(String name,double temp, long timestamp) throws RemoteException;
}
