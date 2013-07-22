<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create a Cutomer</title>
</head>
<body>
<div>
	<s:form action="message">
		<s:hidden name="opcion" value="5"/>	
		<s:textfield name="bankCode" label="Bank Code"/>
		<s:textfield name="loanAmount" label="Loan Amount"/>
		<s:textfield name="qualification" label="Qualification"/>
		<s:textfield name="expirationDate" label="Expiration Date"/>
		<s:textfield name="date" label="Date"/>
		<s:label>-----------------------------------------------------------------------------</s:label>
		<s:textfield name="rfc" label="RFC"/>
		<s:textfield name="firstName" label="First Name"/>
		<s:textfield name="lastName" label="Last Name"/>
		<s:textfield name="address" label="Address"/>
		<s:textfield name="salary" label="Salary"/>
		<s:textfield name="qualificationCustomer" label="Customer's General Qualification"/>
		<s:textfield name="qualificationCustomerDate" label="Qualification's Date"/>
		<s:submit value="Submit"/>
	</s:form>
</div>
</body>
</html>