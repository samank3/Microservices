package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonParser;

public class FX extends Thread
{
	public static PrintStream log = System.out;
	private Socket client;
	
	public FX(Socket client)
	{
		this.client = client;
	}
	
	public void run()
	{
		log.printf("Connected to %s:%d\n", client.getInetAddress().getHostAddress(), client.getPort());
		// TODO use try with resources
		try
		{
		Scanner in = new Scanner(client.getInputStream());
		PrintStream out = new PrintStream(client.getOutputStream(), true);
		String request = in.nextLine();
		String response;
		
		if (request.matches("\\d+"))
		{
			
			URL url = new URL("https://api.exchangeratesapi.io/latest?base=CAD");
			Scanner http = new Scanner(url.openStream());
			String payload = "";
			while (http.hasNextLine()) payload += http.nextLine();
			// response = payload;
			JsonParser parser = new JsonParser();
			payload = parser.parse(payload).getAsJsonObject().get("rates").toString();
			payload = parser.parse(payload).getAsJsonObject().get("USD").toString();
			response = "" + Integer.parseInt(request)*Double.parseDouble(payload);
			
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
		log.printf("Dis-Connected to %s:%d\n", client.getInetAddress().getHostAddress(), client.getPort());
	}

	public static void main(String[] args) throws Exception
	{
		// TODO if statement based on args[0] and args[1]
		int port = 0;
		InetAddress host = InetAddress.getLocalHost(); //.getLoopbackAddress();
		ServerSocket server = new ServerSocket(port, 0, host);
		log.printf("Server listening on %s:%d\n", server.getInetAddress().getHostAddress(), server.getLocalPort());
		// TODO file output stream to ~/4413/ctrl/FX.txt
		// and write the host ip and port
		
		File file = null;
		FileOutputStream fos = null;
		String data = "Host: " + server.getInetAddress().getHostAddress() + "\nPort: " + server.getLocalPort() + "\n";
		try {
			file = new File("/cs/home/samank/4413/ctrl/FX.txt");
			fos = new FileOutputStream(file);
			
			System.out.println(data);
			
			if (!file.exists()) {
				System.out.println("file created\n");
				file.createNewFile();
				System.out.println("file created\n");
			}
			
			byte[] contentInBytes = data.getBytes();
			
			fos.write(contentInBytes);
			fos.flush();
			fos.close();
			
			System.out.println("Done");
			
			
		}catch(Exception e)
		{
			log.print(e);
		}
		
		//while(true) // TODO while (above file.exists())
		while (file.exists())
		{
			Socket client = server.accept();
			new FX(client).start();
//			server.close();
		}
		server.close();
		// TODO log server shutdown
		log.print("Server shutdown");
		
	}

}
