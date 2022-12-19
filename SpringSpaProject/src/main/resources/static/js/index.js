/**
 * index.js
 */
$('.img').on('click',function(ev){
    window.location.href='/';
})

$('.btnBoard').on('click',function(){
	$('#content').load('/board/board_select');
});
 
$('.btnGuestBook').on('click',function(){
	$('#content').load('/guestbook/guestbook_select');
});

/* 방명록 최근 10개 */
/*$('.guestbook10').on('click',function(){
	$('#content>.info1').load('/guestbook/guestbook10');
})*/
$('#content>.info1').load('/guestbook/guestbook10');

/* 게시물 최근 10개 */
/*$('.board10').on('click',function(){
	$('#content>.info2').load('/board/board10');
})*/
$('#content>.info2').load('/board/board10');
