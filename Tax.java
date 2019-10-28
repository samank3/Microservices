package service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import com.google.gson.Gson;
		

public class Tax extends Thread
{
	public static PrintStream log = System.out;
	private Socket client;
	
	public Tax(Socket client)
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
		if (request.matches("[A-Z]{2}\\s+(json|xml)"))
		{
			String[] token = request.split("\\s+");
			String province = token[0];
			String format = token[1];
			String dbURL = "jdbc:sqlite:/cs/home/samank/4413/pkg/sqlite/Models_R_US.db";
			Connection connection = DriverManager.getConnection(dbURL);
			
			String query = "select * from Tax where code = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1,  province);
			ResultSet rs = statement.executeQuery();
			
			/* this is not secure
			String query = "select * from Tax where code='" + province + "'";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			*/
			
			TaxBean bean = new TaxBean();
			while (rs.next())
			{
				bean.setName(rs.getString("province"));
				bean.setCode(rs.getString("code"));
				bean.setType(rs.getString("type"));
				bean.setGst(rs.getDouble("gst"));
				bean.setPst(rs.getDouble("pst"));
			}
			if (format.equals("xml"))
			{
				JAXBContext context = JAXBContext.newInstance(TaxBean.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				m.marshal(bean, baos);
				response = baos.toString();
			} else
			{
				Gson gson = new Gson();
				response = gson.toJson(bean);
			}
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
			new Tax(client).start();
		}
		//server.close();
	}

}
