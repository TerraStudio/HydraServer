package de.Marcel.HydraServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * author - marcelfranzen
 */
public class Connection implements Runnable {
    private Socket client;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    private ServerListener listener;
    private boolean running;
    
    private Thread thread;
   
    public Connection(Socket client, Server server, ServerListener listener) {
    	try {
    		this.client = client;
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream());
            this.server = server;
            this.listener = listener;
            this.running = true;
            
            //start a new thread
            this.thread = new Thread(this, "connection:" + client.getRemoteSocketAddress());
            this.thread.start();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    /*
     * wait for new incoming messages from client
     * check if client disconnected
     */
    @Override
    public void run() {
    	String message = "";
    	try {
    		while((message = reader.readLine()) != null && running) {
    			server.getListener().onReceive(this, message);
    			
    			if(reader.read() == -1) {
    				server.removeConnection(this);
    				listener.onLogout(this);
    			}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /*
     * cancel connection
     */
    @SuppressWarnings("deprecation")
	public boolean disconnect () {
    	try {
    		thread.stop();
    		
    		client.close();
    		
    		return true;
    	} catch (IOException e) {
    		e.printStackTrace();
    		
    		return false;
    	}
    }
    
    /*
     * write message to client
     */
    public void write (String message) {
    	writer.println(message);
    	writer.flush();
    }
    
    public Socket getClient() {
		return client;
	}
}
