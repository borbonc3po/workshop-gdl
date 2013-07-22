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
	@Path("/{package}")
	@Produces(MediaType.TEXT_PLAIN)
	public String startCommunicator(@PathParam("package") String my_package)
	{
		System.out.println("Message received by the Credit Manager: -"+my_package+"-");
		return sendMessage(my_package);
	}
	
	private String sendMessage(String message)
	{
		socketConnection test = new socketConnection(message);
		return test.communicateWithTheServer();
	}
	
	
}
