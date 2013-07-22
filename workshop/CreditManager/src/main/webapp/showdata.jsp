<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table width="100%" border="1">
		<tr>
			<td>
				RFC
			</td>
			<td>
				BANK CODE
			</td>
			<td>
				NAME
			</td>
			<td>
				ADDRESS
			</td>
			<td>
				LOAN AMOUNT
			</td>
			<td>
				DATE
			</td>
			<td>
				QUALIFICATION
			</td>
			<td>
				ACTIVE
			</td>
		</tr>
		<s:iterator value="Loans">
			<tr>
				<td>
					<s:property value="rfc"/>
				</td>
				<td>
					<s:property value="bankCode"/>
				</td>
				<td>
					<s:property value="name"/>
				</td>
				<td>
					<s:property value="address"/>
				</td>
				<td>
					<s:property value="loanAmount"/>
				</td>
				<td>
					<s:property value="date"/>
				</td>
				<td>
					<s:property value="qualification"/>
				</td>
				<td>
					<s:property value="active"/>
				</td>
			</tr>
		</s:iterator>
	</table>
</body>
</html>