package de.Marcel.HydraServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * author - marcelfranzen test
 */
public class Server {
	private int port;
	private ServerSocket server;
	private boolean running;

	private ServerListener listener;
	
	private ArrayList<Connection> connections;
	
	public Server(int port) {
		this.port = port;
		this.connections = new ArrayList<Connection>();
	}
	
	/*
	 * wait for a new incoming connection and start a new thread
	 * to handle the client.
	 */
	private synchronized void listenForClients () throws Exception {
		final Server s = this;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					//get new connection / block if there is no connection
					Socket client = null;
					try {
						client = server.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//create new connection variable of client
					Connection con = new Connection(client, s, getListener());
					
					//save connection
					connections.add(con);
					
					if(listener != null) {
						listener.onLogin(con);
					}
				}
			}
		}).start();
	}
	
	/*
	 * stop server
	 */
	public void stop () {
		for(Connection c : connections) {
			c.disconnect();
		}
		
		running = false;
		System.exit(0);
	}
	
	/*
	 * disconnect a connection
	 */
	public void kickConnection (Connection c) {
		c.disconnect();
		removeConnection(c);
	}
	
	/*
	 * remove connection from connection list
	 */
	public void removeConnection (Connection c) {
		connections.remove(c);
	}
	
	/*
	 * send a message to every client
	 */
	public boolean sendToAll (String message) {
		for(Connection connection : connections) {
			connection.write(message);
		}
		
		return true;
	}
	
	/*
	 * start server, start to listen for clients
	 */
	public boolean start () {
		try {
			server = new ServerSocket(port);
			running = true;
			
			listenForClients();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	/*r
	 * register new listener
	 */
	public void registerListener (ServerListener listener) {
		this.listener = listener;
	}
	
	public ServerListener getListener() {
		return listener;
	}
	
	public ArrayList<Connection> getConnections() {
		return connections;
	}
	
	public int getPort() {
		return port;
	}
}
