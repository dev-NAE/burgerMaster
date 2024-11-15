let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

function changeSearch(type){
	document.getElementById('keyword').name = type;
}
function changeState(state){
	document.getElementById('state').value = state;
}

//전달, 완료에 따라 모달 수정
$(document).ready(function(){
	$('.transmit').click(function(){
		$('#confirmationModalLabel').text('해당 작업 지시를 생산 라인에 전달하시겠습니까?');
		$('#confirmationModal').modal('show');
		$('#confirmSubmitBtn').attr('name', 'transmit');
		$('#confirmSubmitBtn').attr('value', this.id);
	});
	
	$('.complete').click(function(){
		$('#confirmationModalLabel').text('해당 작업 지시를 완료 처리 하시겠습니까?');
		$('#confirmationModal').modal('show');
		$('#confirmSubmitBtn').attr('name', 'complete');
		$('#confirmSubmitBtn').attr('value', this.id);
	});
});

//작업 상태 변경 후, 테이블 변경
$('#confirmSubmitBtn').click(function(){
	let type = $('#confirmSubmitBtn').attr('name');
	let key = $('#confirmSubmitBtn').attr('value');
	
	console.log(type+" "+key);
	
	$.ajax({
		url: "/mf/orderUpdate",
		method: "POST",
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		dataType : "text",
		data: {
			type: type,
			key: key
		},
		beforeSend : function(xhr)
		            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
		                xhr.setRequestHeader(header, token);
		            },
		success: function(response){
			$('#confirmationModal').modal('hide');
			console.log(response);
			
			if(response=='success'){
				
			alert("처리되었습니다.");
			
			$('#'+key).attr('disabled', true);
			$('#'+key).removeClass('btn-warning');
			$('#'+key).removeClass('btn-primary');
			$('#'+key).addClass('btn-secondary')
			if(type=='transmit'){
				$('span[name='+key+']').text('작업 대기');
				$('span[name='+key+']').css("color", "brown");
			} else if(type=='complete'){
				$('span[name='+key+']').text('작업 종료');
				$('span[name='+key+']').css("color", "green");
			}
			
			} else {
				alert("잘못된 요청입니다. 다시 시도해주세요.");
			}
		},error: function(xhr, status, error) {
			$('#confirmationModal').modal('hide');
		    console.error("요청 오류 : ", error);
		    alert("요청 오류 발생.");
		}
	});
});

