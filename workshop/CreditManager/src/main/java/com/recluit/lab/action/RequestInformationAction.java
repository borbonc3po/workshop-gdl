package com.recluit.lab.action;

import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.recluit.lab.databases.DMLprocess;
import com.recluit.lab.model.Loan;
import com.recluit.lab.restclient.makeConnection;

public class RequestInformationAction extends ActionSupport
{
	private static final long serialVersionUID = -3561538112932319256L;
	private String response;
	private String rfc;
	private int opcion;
	private String date;
	private String bankCode;
	private String address;
	private String loanAmount;
	private String qualification;
	private String firstName;
	private String lastName;
	private String salary;
	private String qualificationCustomerDate;
	private String qualificationCustomer;
	private String expirationDate;
	private ArrayList<Loan> Loans = new ArrayList<Loan>();

	private final static String SEPARATOR_OPERATION = ":";
	private final static String SEPARATOR_VALUES = "$";
	private final static String SEPARATOR_PACKAGE = "/";

/**************************************************************************************/	
	private void createLoanObject(String response)
	{
		System.out.println("Values response: -"+response+"-");
		String[] values = response.split(SEPARATOR_PACKAGE);
		int i = 0;
		while(i < values.length )
		{
			System.out.println("Values "+i+":s"+values[i]);
			i++;
		}
		int cant_packages = values.length;
		System.out.println("Values length :"+cant_packages);
		i = 0;
		while(i < cant_packages)
		{
			System.out.println("Values -packages- :"+values[i]);
			Loans.add(new Loan(values[i], SEPARATOR_VALUES));
			i++;
		}
	}
/**************************************************************************************/
	private String createInsertLoanQuery(Loan values)
	{
		String query = "INSERT INTO LOANS VALUES ("+
														"'"+values.getRfc()+values.getDate()+"',"+
														"'"+values.getRfc()+"',"+
														"'"+values.getLoanAmount()+"',"+
														"'"+values.getQualification()+"',"+
														"'"+values.getDate()+"',"+
														"'Y'"
														+")";
		return query;
	}
	
	
/**************************************************************************************/
	private void getValues()
	{
		String query = "Select FNAME, LNAME, QUALIFICATION, ADDRESS FROM CUSTOMER WHERE RFC = '"+rfc+"'";
		DMLprocess DBconsultRow = new DMLprocess();
		ArrayList<String> cant = DBconsultRow.readFromTable(query);
		firstName = cant.get(0);
		lastName = cant.get(1);
		qualification = cant.get(2);
		address = cant.get(3);
	}
/**************************************************************************************/
	private String hardWorker()
	{
		makeConnection connection = new makeConnection();
		System.out.println("Starting the Execution ...");
		String query;
		System.out.println("Opcion "+opcion);		
		switch(opcion)
		{
			case 1:
				/*
				 * The information is send to the communicator 
				 */
				response = connection.getMessage(opcion+SEPARATOR_OPERATION+rfc);
				System.out.println("Response: -"+response+"-");
				/*
				 * We create a Loan Object for each row found by the server
				 * and put it into a ArrayList to save the information of 
				 * the server (the line(s) of the file read by C)
				 */
				createLoanObject(response);
				return "showdata";
			case 2:
				/*
				 * We check if there's any RFC on the costumer table
				 * if not, we are going to show the form to create a 
				 * new customer, if the customer is already in the DB
				 * we continue normally. 
				 */
				DMLprocess DBconsultRow = new DMLprocess();
				query = "SELECT count(*) FROM CUSTOMER WHERE RFC = '"+rfc+"'";
				ArrayList<String> cant = DBconsultRow.readFromTable(query);
				if(Integer.parseInt(cant.get(0)) == 0 )
				{
					return "createCostumer";
				}
				/*
				 * We create the row to insert in the TXT file in the Unix server
				 */
				getValues();
				String row = rfc+SEPARATOR_VALUES+bankCode+SEPARATOR_VALUES+firstName+SEPARATOR_VALUES+address+SEPARATOR_VALUES+loanAmount+SEPARATOR_VALUES+date+SEPARATOR_VALUES+qualification+SEPARATOR_VALUES+"Y";
				response = connection.getMessage(opcion+SEPARATOR_OPERATION+row);
				/*
				 * With the response we create an element "Load" that would be inside
				 * a ArrayList<Loan> to keep the information of the new Loan's form
				 * save.
				 * WARNING: Is only one element, but we put it inside of an ArrayList 
				 * to avoid the work of create code only for this part, so we can re-use
				 * the actual method for this task.
				 */
				createLoanObject(row);
				/*
				 * We insert the information of the Loan in the Database.
				 * WARNING: Even when we know that there's only one element
				 * in the ArrayList we have to do a "for" for all the elements
				 * in order to avoid every kind of warning or error.
				 */
				DMLprocess DBinsertRow = new DMLprocess();
				for(Loan loan:Loans)
				{
					DBinsertRow.insertIntoTable(loan,createInsertLoanQuery(loan));
				}
				break;
			case 3:
				/*
				 * This option is still on development so it could not work properly 
				 * be careful.
				 */
				response = connection.getMessage(opcion+SEPARATOR_OPERATION+rfc+SEPARATOR_VALUES+date);
				query = "UPDATE LOANS SET STATUS = 'N' WHERE ID = '"+rfc+date+"'";
				System.out.println("QUERY OF UPDATE: "+query);
				DMLprocess DBchangeRow = new DMLprocess();
				DBchangeRow.modifyRow(query);
				break;
			case 4:
				/*
				 * This option is disabled 'cause i didn't have the enough time to work
				 * on it :(
				 */
				//response = connection.getMessage(opcion+SEPARATOR_OPERATION+rfc+SEPARATOR_VALUES+date);
				break;
			case 5:
				//insertar el registro
				//reiniciar el proceso con la opcion 2
				System.out.println("\n\n\nfname: "+firstName+"\nlName: "+lastName+"\nquali customer:"+qualificationCustomer+"\n\n\n");
				query = "INSERT INTO CUSTOMER VALUES ('"
														+rfc+"','"
														+firstName+"','"
														+lastName+"','"
														+qualificationCustomer+"','"
														+qualificationCustomerDate+"','"
														+salary+"','"
														+address+"'"
														+")";
				DMLprocess DBinsertRowCustomer = new DMLprocess();
				DBinsertRowCustomer.modifyRow(query);
				opcion = 2;
				return hardWorker();
			default:
				System.out.println("The -"+opcion+"- is an invalid option, check it out");
				return ERROR;
		}
		if(!response.equals("ERROR"))
		{
			return SUCCESS;
		}
		return ERROR;
	}
	
/**************************************************************************************/
	public String execute() throws Exception
	{
		/*
		 * This method has all the heavy part
		 * so inside execute() we only return the answer 
		 */
		System.out.println("\n\n\nfname: "+firstName+"\nlName: "+lastName+"\nquali customer:"+qualificationCustomer+"\n\n\n");
		return hardWorker();
	}
	
/************************ g e t t e r s   a n d   s e t t e r s ***********************/
	
	public String getResponse() {
		return response;
	}
	public String getRfc() {
		return rfc;
	}
	public int getOpcion() {
		return opcion;
	}
	public String getDate() {
		return date;
	}
	public String getBankCode() {
		return bankCode;
	}
	public String getAddress() {
		return address;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public String getQualification() {
		return qualification;
	}
	public String getSalary() {
		return salary;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getQualificationCustomer() {
		return qualificationCustomer;
	}
	public String getQualificationCustomerDate() {
		return qualificationCustomerDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public ArrayList<Loan> getLoans() {
		return Loans;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public void setOpcion(int opcion) {
		this.opcion = opcion;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public void setQualificationCustomer(String qualificationCustomer) {
		this.qualificationCustomer = qualificationCustomer;
	}
	public void setQualificationCustomerDate(String qualificationCustomerDate) {
		this.qualificationCustomerDate = qualificationCustomerDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public void setLoans(ArrayList<Loan> loans) {
		Loans = loans;
	}
}