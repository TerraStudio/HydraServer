package de.Marcel.HydraServer;

public interface ClientListener {
	public void onReceive (String message);
	public void onConnectionLost ();
}
