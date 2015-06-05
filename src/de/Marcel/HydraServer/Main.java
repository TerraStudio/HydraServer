package de.Marcel.HydraServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("91.96.104.92", 80);
		
		socket.getOutputStream().write(1);
	}
}
