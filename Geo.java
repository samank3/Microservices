package service;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Geo extends Thread
{
	public static PrintStream log = System.out;
	private Socket client;
	
	public Geo(Socket client)
	{
		this.client = client;
	}
	
	public void run()
	{
		log.printf("Connected to %s:%d\n", client.getInetAddress(), client.getPort());
		try
		{
		Scanner in = new Scanner(client.getInputStream());
		PrintStream out = new PrintStream(client.getOutputStream(), true);
		String request = in.nextLine();
		String response;

		if (true)
		//if (request.matches("[0-9]*{4}"))
		{
			String[] temp;
			temp = request.split(" ");   
			double t1 = Double.parseDouble(temp[0]);
			double n1 = Double.parseDouble(temp[1]);
			double t2 = Double.parseDouble(temp[2]);
			double n2 = Double.parseDouble(temp[3]);
			
			
			double theta = n1-n2;
			double dist = Math.sin(Math.toRadians(t1)) * Math.sin(Math.toRadians(t2)) + Math.cos(Math.toRadians(t1)) * Math.cos(Math.toRadians(t2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515 * 1.609344;
			response = "" + dist;
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
		int port = 0;
		InetAddress host = InetAddress.getLocalHost(); //.getLoopbackAddress();
		ServerSocket server = new ServerSocket(port, 0, host);
		log.printf("Server listening on %s:%d\n", server.getInetAddress(), server.getLocalPort());
		while(true)
		{
			Socket client = server.accept();
			new Geo(client).start();
		}
		//server.close();
	}

}
