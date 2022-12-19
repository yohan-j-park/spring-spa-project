/**
 * board.js
 */

(board = function(){
    var frm = $('.frm')[0];
    var param;
    
	$('.lastboard').on('click',function(){
		window.location.href='/';
	})

    $('.btnSearch').on('click', function(){
        frm = $('.frm_search')[0];
        frm.nowPage.value = 1;
        param = $(frm).serialize();
        $('#content').load("/board/board_select", param);
    });
    
    $('.btnDeleteR').on('click', function(){
        let yn = confirm('자료를 삭제하시겠습니까 ?');
        if( !yn ) return;
        param = $('.frm').serialize();
        $.post("/board/board_delete", param, function(data){
            $('#content').html(data);
        })
    })    

    $('.btnInsert').on('click', function(){
    	param = $('.frm_search').serialize();
    	$.post("/board/board_insert", param, function(data){
    		$('#content').html(data);
    	})
    })
       
    $('.btnInsertR').on('click', function(){
    	frm = $('.frm')[0];
    	param = new FormData(frm);
    	/*var param1 = $(frm).serialize();	 FormData를 사용하는 이유: serialize는 multipart를 지원하지 않는다.
    	var keys = param.keys();			 Iterator(반복자)를 사용하여 FormData의 ket혹은 value값이 제대로 들어가 있는지 확인할 수 있다.
    	var values = param.values();
    	console.log(keys.next(),values.next());
    	console.log(keys.next(),values.next());
    	console.log(keys.next(),values.next());
    	console.log(keys.next(),values.next());*/
    	$.ajax({
    		type : 'POST',
    		url : '/board/board_insertR',
    		contentType : false,
    		processData : false,
    		data : param,
    		dataType : 'html',
    		success : function(data){
				if(data != '') alert(data);
				frm.enctype='';
				param = $(frm).serialize();
    			$('#content').load('/board/board_select',param);
    		}
    	})
    })
    $('.btnSelect').on('click', function(){
    	param = $('.frm').serialize();
    	$.post("/board/board_select", param, function(data){
    			$('#content').html(data);
    	})
    })
    $('.btnUpdate').on('click', function(){
        param = $(frm).serialize();
        $.post("/board/board_update", param, function(data){
            $('#content').html(data);
        })
    })
    $('.btnUpdateR').on('click', function(){
        frm = $('.frm')[0];
        param = new FormData(frm);
       
        $.ajax({
            type : 'POST',
            url : '/board/board_updateR',
            contentType : false,
            processData : false,
            data : param,
            dataType : 'html',
            success : function(data){
				if(data != '') alert(data);
				frm.enctype='';
				param = $(frm).serialize();
    			$('#content').load('/board/board_select',param);
            }
        })
    })
    
    $('.btnRepl').on('click', function(){
        param = $(frm).serialize();
        $.post("/board/board_repl", param, function(data){
            $('#content').html(data);
        })
    })
    $('.btnReplR').on('click', function(){
        frm = $('.frm')[0];
        param = new FormData(frm);
       
        $.ajax({
            type : 'POST',
            url : "/board/board_replR",
            contentType : false,
            processData : false,
            data : param,
            dataType : 'html',
            success : function(data){
               if(data != '') alert(data);
				frm.enctype='';
				param = $(frm).serialize();
    			$('#content').load('/board/board_select',param);
            }
        })
    })
    
    board.move = function(nowPage){
        frm = $('.frm_search')[0];
        frm.nowPage.value = nowPage;
        param = $(frm).serialize();
        $.post("/board/board_select", param, function(data){
            $('#content').html(data);
        })
    }
    
    board.view = function(sno){
        frm = $('.frm_search')[0];
        frm.sno.value = sno;
        param = $(frm).serialize();
        $.post("/board/board_view", param, function(data){
            $('#content').html(data);
        })
    }    
})()