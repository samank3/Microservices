package service;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.net.InetAddress;

public class Stateful extends Thread {
	
	public static PrintStream log = System.out;
	private Socket client;
	
	public Stateful(Socket client) {
		this.client = client;
		
		
		
		
	}

	public static void main(String[] args) throws Exception {

		try {
			String path = "~/4413/ctrl/Geo.txt";
			path = path.replaceFirst("^~", System.getProperty("user.home"));
			
			String host = Files.readAllLines(Paths.get(path)).get(0);
			String port = Files.readAllLines(Paths.get(path)).get(1);
			String cookie = Files.readAllLines(Paths.get(path)).get(2);
			
			System.out.println(host + "\n" + port + "\n" + cookie + "\n");
			
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
	}
	
}
