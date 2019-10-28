package service;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Gateway extends Thread{
	
	
	
	
	
	
	
	

	public static void main(String[] args) throws Exception {
		
		String path = "~/4413/ctrl/Geo.txt";
		path = path.replaceFirst("^~", System.getProperty("user.home"));
		
		String host = Files.readAllLines(Paths.get(path)).get(0);
		String port = Files.readAllLines(Paths.get(path)).get(1);
		
		System.out.println(host + "\n" + port + "\n");

	}

}
