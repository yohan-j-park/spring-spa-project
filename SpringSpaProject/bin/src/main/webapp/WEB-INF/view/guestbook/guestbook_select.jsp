<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>guestbook_select.jsp</title>
<link rel="stylesheet" href="css/guestbook.css">
<script defer src="js/guest.js"></script>
</head>
<body>

<div id="guestbook_list">
	<%@include file="guestbook_insert.jsp" %>
	<form name='frm_gb_search' class='frm_gb_search' method='post'>
		<input type='hidden' name='nowPage' value='${gVo.nowPage }'/>
		<input type='hidden' name='serial' value="${gVo.sno }"/>
		<input type="text" class="findStr" name='findStr' value='${gVo.findStr }'>
		<input type='button' class='btnSearch' name='btnSearch' value='검색' style="margin-left:5px;">
	</form>
		
	<div class="guestbook_items">
		<c:forEach var="vo" items="${list }">
			<div class="item">
				<form name="frm_guestbook" class="frm_guestbook" method="post">
					<div class='btnZone'>
						<input type='button' class='btnUpdate' value='수정' name='btnZone'
								onclick='modifyView(this.form)'/>	
						<input type='button' class='btnDelete' value='X' name='btnZone'
								onclick='modalView(this.form)'/>
					</div>
					<label>작성자</label>
					<input type="text" name="id" value="${vo.id }" style='width:120px;' readonly>
					<label>작성일</label>
					<output>${vo.nal }</output><br/><br/>
					<textarea rows="7" cols="55" name="doc" readOnly>${vo.doc }</textarea>
					<br/>
					<div class='updateZone'>
						<label>암호</label>
						<input type="password" name="pwd">
						<input type="button" value="저장" class="btnGuestbookUpdate"
								onclick="update(this.form)">	
						<input type="button" value="취소" class="btnGuestbookCancel"
								onclick='modifyCancel(this.form)'>		
							
					</div>
					
					<input type='hidden' name='sno' value='${vo.sno }'/>
				</form>
			</div>
			
		</c:forEach>
	</div>
	<div class="guestbook_btn" style='text-align:center;'>
		<c:if test="${gVo.startPage>1 }">
			<input type="button" value="&lt;&lt;" class="btnFirst" onclick='move(1)'>
			<input type="button" value="&lt;" class="btnPrev" onclick = 'move(${gVo.startPage-1})'>
		</c:if>
		<c:forEach var='i' begin='${gVo.startPage }' end="${gVo.endPage }">
			<input type='button' value='${i }' class='btnMove' onclick='move(${i})'/>
		</c:forEach>
		<c:if test="${gVo.totPage>gVo.endPage }">
			<input type="button" value="&gt;" class="btnNext" onclick = 'move(${gVo.endPage+1})'>
			<input type='button' value="&gt;&gt;" class='btnLast' onclick='move(${gVo.totPage })'/>
		</c:if>
	</div>
	<div id='modal'>
		<div id='modalcontent'>
			<input type='button' value='X' id='btnClose'/>
			<span>암호를 입력하세요</span>
			<input type='password' id='pwd'/>
			<input type='button' value='확인' id='btnCheck'/>
		
		</div>
	</div>
</div>
</body>
</html>