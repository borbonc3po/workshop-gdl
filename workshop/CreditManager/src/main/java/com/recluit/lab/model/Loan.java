package com.recluit.lab.model;

public class Loan 
{
	private String rfc;
	private String date;
	private String bankCode;
	private String name;
	private String address;
	private String loanAmount;
	private String qualification;
	private String active;
	
	public Loan(String data, String separator)
	{
		split(data,separator);
	}
	
	private void defineVariables(String[] values)
	{
		setRfc(values[0]);
		setDate(values[5]);
		setBankCode(values[1]);
		setName(values[2]);
		setAddress(values[3]);
		setLoanAmount(values[4]);
		setQualification(values[6]);
		setActive(values[7]);
	}
	
	private void split(String data, String separator)
	{
		System.out.println("Data: "+data+"\nSeparator: "+separator);
		String[] values;
		values = data.split("\\"+separator);
		int i = 0;
		while(i < values.length)
		{
			System.out.println("Value["+i+"]: "+values[i]);
			i++;
		}
		defineVariables(values);
	}
	
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	
}
