package de.Marcel.HydraServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * author - marcelfranzen
 */
public class Client {
	private int port;
	private String ip;
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	private ClientListener listener;
	private boolean running;
	
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/*
	 * start the client
	 */
	public boolean start () {
		try {
			socket = new Socket(ip, port);
			running = true;
			
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			listenForMessages();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	/*
	 * wait for a new incoming message
	 */
	private synchronized void listenForMessages () {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				String line = "";
				try {
					while(((line = reader.readLine())) != null && running) {
						listener.onReceive(line);
						
						if(reader.read() == -1) {
							listener.onConnectionLost();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		t.start();
	}
	
	/*
	 * register new listener
	 */
	public void registerListener (ClientListener listener) {
		this.listener = listener;
	}
	
	/*
	 * writer message to server
	 */
	public void write (String message) {
		writer.println(message);
		writer.flush();
	}
	
	/*
	 * stop server
	 */
	public boolean stop () {
		try {
			running = false;
			reader.close();
			writer.close();
			
			System.exit(0);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	/*
	 * return true if server is running
	 */
	public boolean isRunning() {
		return running;
	}
}
