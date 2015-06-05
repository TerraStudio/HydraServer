package de.Marcel.HydraServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * author - marcelfranzen
 */
public class Config {
	private File file;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	private ArrayList<String> toSet;
	
	public Config (File file) {
		this.file = file;
		
		this.toSet = new ArrayList<String>();
		
		try {
			this.writer = new PrintWriter(file);
			this.reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//file exists
	public boolean exists () {
		return file.exists();
	}
	
	//create file
	public void create () {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//set value in file
	public void set (String path, String value) throws AlreadyExistsException {
		if(getString(path) != null) {
			String formattedPath = path + ":";
			
			toSet.add(formattedPath + " " + value);
		} else {
			throw new AlreadyExistsException(path + " already exists");
		}
	}
	
	//save
	public void save () throws IOException {
		for(String entry : toSet) {
			writer.println(entry);
			writer.flush();
		}
		
		toSet.clear();
	}
	
	//delete file
	public boolean delete () {
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file.delete();
	}
	
	public double getDouble (String path) {
		return Double.parseDouble(getString(path));
	}
	
	public float getFloat (String path) {
		return Float.parseFloat(getString(path));
	}
	
	public int getInt (String path) {
		return Integer.parseInt(getString(path));
	}
	
	public String getString (String path) {
		try {
			return (String) get(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	//get object in file
	private Object get (String path) throws IOException {
		String formattedPath = path + ": ";
		
		String line = "";
		while((line = reader.readLine()) != null) {
			if(line.startsWith(formattedPath)) {
				String data = line.replace(formattedPath, "");
				
				return data;
			}
		}
		
		return null;
	}
	
	public File getFile() {
		return file;
	}
	
	/*
	 * own exception (not necessary)
	 */
	public class AlreadyExistsException extends Exception {
		private static final long serialVersionUID = -4677658256602094957L;

		public AlreadyExistsException(String message) {
			super(message);
		}
	}
}
