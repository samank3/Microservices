package service;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Auth extends Thread
{
	public static PrintStream log = System.out;
	private Socket client;
	
	public Auth(Socket client)
	{
		this.client=client;
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
			{
				String[] token = request.split("\\s+");
				String username = token[0];
				String password = token[1];
				String dbURL = "jdbc:sqlite:/cs/home/samank/4413/pkg/sqlite/Models_R_US.db";
				Connection connection = DriverManager.getConnection(dbURL);
				
				//System.out.println(username + " " + password);
				
				String query = "select name, salt, count, hash from Client where name = '" + username + "'";
				//System.out.println(query);
				
				PreparedStatement statement = connection.prepareStatement(query);
				/* statement.setString(0, username); */
				ResultSet rs = statement.executeQuery();
				
				AuthBean bean = new AuthBean();
				while(rs.next())
				{
					bean.setSalt(rs.getString("salt"));
					bean.setCount(rs.getInt("count"));
					bean.setHash(rs.getString("hash"));
				}
				
//				System.out.println(bean.getString("salt"));
//				System.out.println("Salt: " + bean.getSalt());
//				System.out.println("Count: " + bean.getCount());
//				System.out.println("Hash: " + bean.getHash());
				
				
				String testPassword = g.Util.hash(password, bean.getSalt(), bean.getCount());
				
				String ogPassword = bean.getHash();
				
//				System.out.println("gUtil output: " + testPassword);
				
				if (testPassword.equals(ogPassword)) {
					response = "OK";
				}
				else {
					response = "FAILURE";
				}
				
				System.out.println("Result: " + response);
				
			}
			else {
				response = "Don't understand: " + request;
			}
			out.println(response);
		}catch (Exception e)
		{
			log.println("Error: " + e);
		}
		try {client.close();} catch (Exception e) {log.print(e);}
		log.printf("Dis-Connected to %s:%d\n", client.getInetAddress(), client.getPort());
	}

	public static void main(String[] args) throws Exception{
		int port = 0;
		InetAddress host = InetAddress.getLocalHost(); //.getLoopbackAddress();
		ServerSocket server = new ServerSocket(port, 0, host);
		log.printf("Server listening on %s:%d\n", server.getInetAddress(), server.getLocalPort());
		while(true)
		{
			Socket client = server.accept();
			new Auth(client).start();
		}
	}
}
