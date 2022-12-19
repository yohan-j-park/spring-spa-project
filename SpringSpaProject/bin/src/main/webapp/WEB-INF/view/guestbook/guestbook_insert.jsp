<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class='item1'>
	<form name='frm_guestbook' class='frm_guestbook_insert' method='post'>
		<label>작성자</label>
		<input type='text' name='id'>
		<label>작성일</label>
		<output name='nal'>
			<fmt:formatDate value="<%=new java.util.Date() %>" pattern="yyyy-MM-dd"/><br/>
		</output><br/>
		<textarea rows='7' cols='60' name='doc'></textarea>
		<br/>
		<label>암호</label>
		<input type='password' name='pwd'>
		<input type='button' value='작성' class='btnGuestbookSave'>
	</form>
</div>
<style>
.item1{
	margin:20px auto;
	padding: 20px 40px;
	background-color:#ccc;
	border: 5px solid #aaa;
	box-shadow: 4px 4px 8px #444;
}

</style>
</body>
</html>