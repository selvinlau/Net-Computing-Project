package entity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Device {
	@Id
	private ObjectId id;
	private String name;
	private double temperature;
	private int timestamp;
	
	public Device(){
		super();
	}
	
	public Device(String name, double temperature, int timestamp){
		super();
		this.name = name;
		this.temperature = temperature;
		this.timestamp = timestamp;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

}
