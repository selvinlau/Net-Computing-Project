/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static spark.Spark.*;
import static spark.Spark.init;
import static spark.Spark.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import entity.Device;

public class WebSocketServer {

	static Map<Session, Device> sessionMap = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		port(5678);
		webSocket("", EventHandlerWebSocket.class);
		init();
	}

	public static void broadCastMessage(Session senderSession, String message, int type)throws IOException{
		Gson g = new Gson();
		if(type == 0){
			Device d = g.fromJson(message, Device.class);
			sessionMap.get(senderSession).setName(d.getName());
			sessionMap.get(senderSession).setTemperature(d.getTemperature());
			sessionMap.get(senderSession).setTimestamp(d.getTimestamp());
			
			sessionMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
				try {
					
					session.getRemote().sendString(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		else if(type == 1){
			
			String jsonMessage = "{\"modeType\" : 1,\"name\":\""+sessionMap.get(senderSession).getName()+"\"}";
			sessionMap.remove(senderSession);
			sessionMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
				try {
					
					session.getRemote().sendString(jsonMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		
		
		
		
	}

	public static void sendUserList(Session senderSession)throws IOException{
		Gson g = new Gson();
		List<Device> dList = new ArrayList<Device>();
		try {
			System.out.println(sessionMap.size());
			for (Entry<Session, Device> entry : sessionMap.entrySet()) {
				if(entry.getValue().getTemperature()!=0){
					dList.add(entry.getValue());
				}
			
    		}
			String message = g.toJson(dList);
			System.out.println(dList);
			senderSession.getRemote().sendString(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
