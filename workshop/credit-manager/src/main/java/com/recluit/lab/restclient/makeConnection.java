package com.recluit.lab.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class makeConnection 
{
	public String getMessage(String message)
	{
		String url = "http://localhost:8080/RESTServer/rest/Communication-Manager/"+ message.replace(" ", "%20");
		System.out.println("Connecting to " + url + "...");
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
		/*if(response.getStatus() != 200)
		{
			System.out.print("I can't established the connection");
			throw new RuntimeException("ERROR: " + response.getStatus());
		}*/
		System.out.print("The Server is sending its response...");
		return response.getEntity(String.class);
	}
}

