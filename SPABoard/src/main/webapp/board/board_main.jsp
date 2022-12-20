<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="pVo" class="board.PageVo"/>
<jsp:useBean id="bVo" class="board.BoardVo"/>
<jsp:setProperty property="*" name="pVo"/>    
<jsp:setProperty property="*" name="bVo"/>
<%
    request.setAttribute("pVo", pVo);
    request.setAttribute("bVo", bVo);
%>
<jsp:forward page="board.do"/>