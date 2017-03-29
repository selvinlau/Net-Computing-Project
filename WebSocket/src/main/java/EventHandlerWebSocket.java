

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import entity.Device;

@WebSocket
public class EventHandlerWebSocket {

    @OnWebSocketConnect
    public void connected(Session session) throws IOException {
    	Device d = new Device();
    	WebSocketServer.sessionMap.put(session, d);
    	
        System.out.println("Device Connected");
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String message)  throws IOException{
        System.out.println("Someone Disconnected");
        WebSocketServer.broadCastMessage(session,message,1);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
    	
        System.out.println("Got: " + message);
        if(message.equals("0")){
        	WebSocketServer.sendUserList(session);
        	System.out.println("HERE");
        }
        else{
        	WebSocketServer.broadCastMessage(session,message,0);
        }
    }
    
   
}