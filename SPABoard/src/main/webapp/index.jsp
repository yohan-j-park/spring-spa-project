<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Starbucks Japan입니다.</title>
<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css'>
<link rel='icon' href='images/starbucks.png'/>
<link rel='stylesheet' href='css/index.css'/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.1.min.js"></script>
<script defer src='js/index.js'></script>
</head>
<body>
<div id='main'>
    <header>
		<nav>
			<button type="button" class="btn bg-primary text-white index" style='background-color:rgb(139, 188, 204)'>Home</button>
			<button type="button" class="btn bg-primary text-white board">게시판</button>
		</nav>
    </header>
    <div id='content'>
        <div class='info'>
            <h3>SPA(Single Page Application)를 제작함.</h3>
            <ul>
                <li>API & Library : servlet, jstl, ajax, jquery</li>
                <li>DB : mysql </li>
                <li>Framework : mybatis</li>
            </ul>               
        </div>
    </div>
    <footer>
        <font size='5' color='white'>S P A</font><br/>
        <font color='white'>servlet,jstl,jquery,mysql로 만들어진 Single Page Application</font>
    </footer>
</div>
</body>
</html>