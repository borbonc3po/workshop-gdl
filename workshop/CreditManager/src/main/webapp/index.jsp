<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix = "s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Checking for Credits</title>
</head>
<body>
<script type="text/javascript">
function hideall()
{
	document.getElementById('pay').style.display = 'none';
	document.getElementById('consult').style.display = 'none';
	document.getElementById('insert').style.display = 'none';
	document.getElementById('close').style.display = 'none';
}
function showdiv()
{
	hideall();
    select = document.getElementById("operations");
    if(select.options[select.options.selectedIndex].value=='1')
    {
        document.getElementById('consult').style.display = '';        
    }
    if(select.options[select.options.selectedIndex].value=='2')
    {
        document.getElementById('insert').style.display = '';        
    }
    if(select.options[select.options.selectedIndex].value=='3')
    {
        document.getElementById('close').style.display = '';        
    }
    if(select.options[select.options.selectedIndex].value=='4')
    {
        document.getElementById('pay').style.display = '';        
    }
}
</script>
Welcome
<br/>
Select the task to do:

<s:select 
		name="operations" 
		id="operations" 
		label="Operations" 
		headerKey="-1" 
		headerValue="Select Operation"
		onchange="showdiv()"
		list="#{
				'1':'1. Display loans',
				'2':'2. Insert new loan',
				'3':'3. Close loan',
				'4':'4. Payment'
				}"
		value="-1"/>
<div id="insert" style="display: none;">
	<s:form action="message">
		<s:hidden name="opcion" value="2"/>	
		<s:textfield name="rfc" label="Write the RFC"/>
		<s:textfield name="bankCode" label="Bank Code"/>
		<s:textfield name="loanAmount" label="Loan Amount"/>
		<s:textfield name="date" label="Date"/>
		<s:submit value="Submit"/>
	</s:form>
</div>
<div id="close" style="display: none;">
	<s:form action="message">	
		<s:hidden name="opcion" value="3"/>
		<s:textfield name="rfc" label="Write the RFC"/>
		<s:textfield name="date" label="Date"/>
		<s:submit value="Submit"/>
	</s:form>
</div>
<div id="consult" style="display: none;">
	<s:form action="message">	
		<s:hidden name="opcion" value="1"/>
		<s:textfield name="rfc" label="Write the RFC"/>
		<s:submit value="Submit"/>
	</s:form>
</div>
<div id="pay" style="display: none;">
	<s:form action="message">	
		<s:hidden name="opcion" value="4"/>
		<s:textfield name="rfc" label="Write the RFC"/>
		<s:textfield name="pay" label="Pay"/>
		<s:textfield name="date" label="Date"/>
		<s:submit value="Submit"/>
	</s:form>
</div> 
</body>
</html>

