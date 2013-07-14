package com.recluit.lab.restservices;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/Communication-Manager")
public class MessageService 
{
	/*Quitar el string como parametro 
	 * y regresar un solo libro
	 */
	@GET
	@Path("/{message}")
	@Produces(MediaType.TEXT_PLAIN)
	public String startCommunicator(@PathParam("message") String message)
	{
		System.out.println("Message received by the Credit Manager: -"+message+"-");
		return sendMessage(message);
	}
	
	private String sendMessage(String message)
	{
		socketConnection test = new socketConnection(message);
		return test.communicateWithTheServer();
	}
	
}
