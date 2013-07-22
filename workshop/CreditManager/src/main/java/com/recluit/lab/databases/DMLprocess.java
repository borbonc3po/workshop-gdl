package com.recluit.lab.databases;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.recluit.lab.model.Loan;

public class DMLprocess
{
	private DBconnect db;
	private Connection conn;
	private Statement stmt;
	
	public DMLprocess()
	{
		db = new DBconnect();
		conn = db.connectToOracle();
		try 
		{
			stmt = conn.createStatement();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void closeConnection()
	{
		try
		{
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void closeStatement()
	{
		try
		{
			if(stmt != null)
				stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private void executeQuery(String query)
	{
		try
		{
			//String query = "INSERT INTO system.employee VALUES (311,'Harish','Manager',SYSDATE,12345,55,7788,10)";
			stmt.executeUpdate(query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeStatement();
			closeConnection();
		}
	}	
	
	public void insertIntoTable(Loan values, String query)
	{
		System.out.println("My Query is: "+query);
		executeQuery(query);
		System.out.println("The new row has been inserted!!!");
	}
	
	public void modifyRow(String query)
	{
		executeQuery(query);
		System.out.println("The row has been changed!!!");
	}
	
	public ArrayList<String> readFromTable(String query)
	{
		ArrayList<String> result = new ArrayList<String>();
		System.out.println("QUERY: "+query);
		try
		{
			System.out.println("Getting information from the DB...");
			//String query = "SELECT count(*) FROM CUSTOMER WHERE RFC = '"+rfc+"'";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("I got something...");
			result = displayResult(rs);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeStatement();
			closeConnection();
		}
		return result;
	}
	
	public ArrayList<String> displayResult(ResultSet rs) throws SQLException
	{
		System.out.println("I'm gonna print the info...");
		ArrayList <String> columnsValues = new ArrayList <String>();
		ResultSetMetaData metaData = rs.getMetaData();
		int noOfCols = metaData.getColumnCount();
		while(rs.next())
		{
			for(int i = 1; i <= noOfCols; i++)
			{
				System.out.print(rs.getString(i) + "Proof\t\t");
				columnsValues.add(rs.getString(i));
			}
		}
		return columnsValues;
	}
}