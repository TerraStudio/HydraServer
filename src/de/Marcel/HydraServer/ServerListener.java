package de.Marcel.HydraServer;

public interface ServerListener {
	public void onReceive (Connection con, String message);
	public void onLogin (Connection con);
	public void onLogout (Connection con);
}
