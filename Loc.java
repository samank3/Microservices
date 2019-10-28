package service;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonParser;

public class Loc extends Thread
{
	public static PrintStream log = System.out;
	private Socket client;
	
	public Loc(Socket client)
	{
		this.client = client;
	}
	
	public void run()
	{
		log.printf("Connected to %s:%d\n", client.getInetAddress(), client.getPort());
		// TODO use try with resources
		try
		{
		Scanner in = new Scanner(client.getInputStream());
		PrintStream out = new PrintStream(client.getOutputStream(), true);
		String request = in.nextLine();
		String response;
//		if (request.matches("\\d+"))
		if (true)
		{
//			URL url = new URL("https://api.exchangeratesapi.io/latest?base=CAD");
			
			//BUILD PARAMETERS
			
			String[] parameters = request.split(" ");
			
			String finalP="";
			
			for (String w:parameters) {
				finalP = finalP + "+" + w;
			}
			
			finalP = finalP + "&key=AIzaSyDhhD3hng-go7f92MnJLxVD3NA-BBAFzV8";
			
			
			// BUILD URL
			String urlBuild = "https://maps.googleapis.com/maps/api/geocode/json?address=" + finalP;
			
			System.out.println(urlBuild);
			
			
			URL url = new URL(urlBuild);
			Scanner https = new Scanner(url.openStream());
			String payload = "";
			while (https.hasNextLine()) payload += https.nextLine();
			
			response = payload;
			
			//JsonParser parser = new JsonParser();
			//payload = parser.parse(payload).getAsJsonObject().get("results").getAsJsonObject().get("0").
			//getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").toString();
			//response = "" + payload;
			
		}
		else
		{
			response = "Don't understand: " + request;
		}
		out.println(response);
		} catch (Exception e)
		{
			log.println("Error: " + e);
		}
		try {client.close();} catch (Exception e) {log.print(e);}
		log.printf("Dis-Connected to %s:%d\n", client.getInetAddress(), client.getPort());
	}

	public static void main(String[] args) throws Exception
	{
		// TODO if statement based on args[0] and args[1]
		int port = 0;
		InetAddress host = InetAddress.getLoopbackAddress();
		ServerSocket server = new ServerSocket(port, 0, host);
		log.printf("Server listening on %s:%d\n", server.getInetAddress(), server.getLocalPort());
		// TODO file output stream to ~/4413/ctrl/FX.txt
		// and write the host ip and port
		while(true) // TODO while (above file.exists())
		{
			Socket client = server.accept();
			new Loc(client).start();
		}
		//server.close();
		// TODO log server shutdown
	}

}
