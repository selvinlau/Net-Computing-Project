package dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import entity.Device;

public class DeviceDAO {
	MongoClient client = new MongoClient("localhost", 27017);//Connecting to Client
	Datastore datastore = new Morphia().createDatastore(client, "Device"); //Selecting Collection
	
	public String addDevice(Device device){
		datastore.save(device);
		return "device added";
	}
	
	public List<Device> getAllDevices(){
		List<Device> list = datastore.find(Device.class).asList();
		if(list != null){
			return list;
		}
		
		return null;
	}
}
