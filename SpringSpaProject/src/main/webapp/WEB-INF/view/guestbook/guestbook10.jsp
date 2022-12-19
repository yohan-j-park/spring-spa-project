<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>guestbook/guestbook10.jsp</title>
<script defer src="js/guest.js"></script>
</head>
<body>
<div style="text-align:center;">
<label class='lastguestbook'><font color="white" size='4'>최신 방명록</font></label><br/><br/>
<c:forEach var="vo" items="${guestbook10 }">
<label style="margin-right:30px;">작성자: ${vo.id }</label>
<label>작성일: ${vo.nal }</label><br/><br/>
<li>${vo.doc }
<br/><hr/>
</c:forEach>
</div>

</body>
</html>