package com.vimal.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/actions")
public class AppendWrapper {
	
	public static Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());
	
	@OnOpen
	public void onOpen(Session userSession,EndpointConfig config) {
		System.out.println("New Request recived. Id: "+userSession.getId());
		userSessions.add(userSession);
	}
	
	@OnClose
	public void onClose(Session userSession) {
		System.out.println("Connection closed. Id: "+userSession.getId());
		userSessions.remove(userSession);
	}
	
}
