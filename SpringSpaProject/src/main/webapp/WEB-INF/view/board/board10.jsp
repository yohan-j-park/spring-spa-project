<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>board/board10.jsp</title>
<script defer src="js/board.js"></script>
</head>
<body>
<div style="text-align:center;">
<label class='lastboard'><font color="white" size='4'>최신 게시물</font></label><br/><br/>
<c:forEach var="vo" items="${list }">
<label style="margin-right:30px;">작성자: ${vo.id }</label>
<label>${vo.subject }</label>
<br/><hr/>
</c:forEach>
</div>

</body>
</html>