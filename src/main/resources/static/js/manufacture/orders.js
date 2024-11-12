
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
	const type = $('#confirmSubmitBtn').attr('value');
	const key = $('#confirmSubmitBtn').attr('name');
	
	$.ajax({
		url: "/mf/orderUpdate",
		method: "POST",
		dataType: "json",
		data: {
			type: type,
			key: key
		},
		success: function(response){
			alert("처리되었습니다.");
			$('#'+key).attr('disabled', true);
			if(type=='transmit'){
				$('td[name=key]').text('작업 중');
			} else if(type=='complete'){
				$('td[name=key]').text('작업 완료');
			}
		},error: function(xhr, status, error) {
		    console.error("요청 오류:", error);
		    alert("요청 오류 발생.");
		}
	});
});

function color(state, row){
	alert(state+' '+row);
	if(state=='작업 전달 전'){
		$(row).css("color", "red");
	} else if(state=='작업 중'){
		this.style.color="orange";
	} else if(state=='작업 완료'){
		this.style.color="blue";
	} else if(state=='작업 종료'){
		this.style.color="green";
	} 
}

