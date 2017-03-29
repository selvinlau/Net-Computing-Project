
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.*;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import entity.Device;

public class DeviceClient {
	
	public static Device clientDevice;


	public static void main (String[] args)  throws Exception {
		clientDevice = new Device();
		clientDevice.setName("Machine-1");
		Thread thread1 = new Thread () {
			public void run () {
				establishRMIConnection();
			}
		};
		Thread thread2 = new Thread () {
			public void run () {
				establishWSConnection();
			}
		};
		
		thread1.start();
		thread2.start();

	}

	public static void establishRMIConnection(){
		DeviceInterface d;
		try {
			System.setSecurityManager(new SecurityManager());
			d = (DeviceInterface)Naming.lookup("rmi://localhost/RMIDevice");
			Random r = new Random();
			while(true){

				double randomValue = 20 + (40 - 20) * r.nextDouble();
				clientDevice.setTemperature(randomValue);
				clientDevice.setTimestamp(System.currentTimeMillis());
				String result=d.addDevice(clientDevice.getName(),clientDevice.getTemperature(),clientDevice.getTimestamp());
				System.out.println(result);
				TimeUnit.SECONDS.sleep(5);
			}

		}catch (Exception e) {
			System.out.println("HelloClient exception: " + e);
		}
	}

	public static void establishWSConnection(){
		try {
			// open websocket
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:5678"));

			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					System.out.println(message);
				}
			});


			// prevent socket from sleeping
			while(true){
				
				Gson g = new Gson();
				String json = g.toJson(clientDevice);
				clientEndPoint.sendMessage(json);
				TimeUnit.SECONDS.sleep(5);
			}
		} catch (InterruptedException ex) {
			System.err.println("InterruptedException exception: " + ex.getMessage());
		} catch (URISyntaxException ex) {
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}
	}


}