package com.recluit.lab.databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectProcess
{
	public void readFromTable()
	{
		DBconnect db = new DBconnect();
		Connection conn = db.connectToOracle();
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();
			String query = "Select * from system.employee";
			ResultSet rs = stmt.executeQuery(query);
			displayResult(rs);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				stmt.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void displayResult(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();
		int noOfCols = metaData.getColumnCount();
		
		for(int i = 1; i <= noOfCols; i++)
		{
			System.out.print(metaData.getColumnName(i) + "\t\t");
		}
		System.out.println("");
		
		while(rs.next())
		{
			for(int i = 1; i <= noOfCols; i++)
			{
				System.out.print(rs.getString(i) + "\t\t");
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args)
	{
		SelectProcess  selectDemo = new SelectProcess ();
		selectDemo.readFromTable();
	}	
}
