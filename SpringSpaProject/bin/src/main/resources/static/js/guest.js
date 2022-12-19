/**
 * 
 */

/*move = function(nowPage){
	let frm = $('.frm_gb_search')[0];
	frm.nowPage.value = nowPage;
	let param = $(frm).serialize();
	$.post('/guestbook/guestbook_select',param,function(data){
		$('#content').html(data);
	})
}*/


$('.btnSearch').on('click',function(){
	let frm = $('.frm_gb_search')[0];
	frm.nowPage.value = 1;
	let param = $(frm).serialize();
	$('#content').load('/guestbook/guestbook_select',param);
});

move=function(nowPage){
	let frm = $('.frm_gb_search')[0];
	frm.nowPage.value = nowPage;
	let param = $(frm).serialize();
	$.ajax({
		url:'/guestbook/guestbook_select',
		method:'post',
		data:param,
		success: function(data, param){
			$('#content').html(data)
		}
	})
}

$('.btnGuestbookSave').on('click',function(){
	let frm = $('.frm_guestbook_insert')[0];
	let param = $(frm).serialize();
	$.post('/guestbook/guestbook_insert',param,function(msg){
		if(msg != '') alert(msg);
		frm = $('.frm_gb_search')[0];
		param = $(frm).serialize();
		$('#content').load('/guestbook/guestbook_select',param);
	});
	
});
var docValue;
modifyView = function(frm){
	docValue = frm.doc.value;
	let div = frm.querySelector('.updateZone')
	div.style.visibility='visible';
	$('.btnZone').css('visibility','hidden');
	let textarea = frm.doc;
	$(textarea).prop('readOnly', false); 
	$(frm.parentNode).css('background-color','#ccc');
	
}
modifyCancel = function(frm){
	frm.doc.value = docValue;
	let div = frm.querySelector('.updateZone')
	div.style.visibility='hidden';
	$('.btnZone').css('visibility','visible');
	let textarea = frm.doc;
	$(textarea).prop('readOnly', true); 
	$(frm.parentNode).css('background-color','#aaa');

}

/* model box----------------------- */
$('#btnClose').on('click',function(){
	$('#modal').css('display','none');	
});
var delForm;
modalView = function(frm){
	delForm = frm;
	$('#modal').css('display','block');	
}
/* 방명록 삭제------------------------- */
$('#btnCheck').on('click',function(){
	delForm.pwd.value = $('#pwd').val();
	
	let frm = delForm;
	let param = $(frm).serialize();
	$.post('/guestbook/guestbook_delete',param,function(msg){
		if(msg!='') alert(msg);
		frm = $('.frm_gb_search')[0];
		param = $(frm).serialize();
		$('#content').load('/guestbook/guestbook_select',param);
	});
});

/* 방명록 수정------------------------- */
update = function(frm){
	let param = $(frm).serialize();
	$.post('/guestbook/guestbook_update',param,function(msg){
		if(msg != '') alert(msg);
		frm = $('.frm_gb_search')[0];
		param = $(frm).serialize();
		$('#content').load('/guestbook/guestbook_select',param);
	});
}

$('.lastguestbook').on('click',function(ev){
	window.location.href='/';
})
