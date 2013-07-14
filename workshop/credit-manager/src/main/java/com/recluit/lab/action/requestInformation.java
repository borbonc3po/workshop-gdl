package com.recluit.lab.action;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.restclient.makeConnection;

public class requestInformation extends ActionSupport
{
	private static final long serialVersionUID = -3561538112932319256L;
	private String message;
	private String response;
	
	public String execute() throws Exception
	{
		makeConnection connection = new makeConnection();
		System.out.println("Se crea el Hello client");
		response = connection.getMessage(message);
		System.out.println("The Server sent: "+response);
		if(!response.equals("ERROR"))
		{
			return SUCCESS;
		}
		return ERROR;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public String getBookResultTitle() {
		return response;
	}
}
