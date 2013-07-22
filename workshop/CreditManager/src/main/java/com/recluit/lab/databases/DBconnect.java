package com.recluit.lab.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect
{
	private Connection conn;

	public Connection connectToOracle()
	{
		try
		{
			System.out.println("Trying to connect");
			Class.forName("org.mariadb.jdbc.Driver"); //Load driver into memory
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CREDIT_MANAGER", "root", "");
			System.out.println("Connection established");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Class not found exception");
			e.printStackTrace();
		}
		catch(SQLException e)
		{
			System.out.println("SQL Exception");
			e.printStackTrace();
		}
		return conn;
	}
}