let signal = 0;

$(document).ready(function(){
	$('.transmit').click(function(){
		$('#confirmationModalLabel').text('해당 작업 지시를 생산 라인에 전달하시겠습니까?');
		$('#confirmationModal').modal('show');
		$('#confirmSubmitBtn').attr('name', this.id);
	});
	
	$('.complete').click(function(){
		alert("완료");
		$('#confirmationModalLabel').text('해당 작업 지시를 완료 처리 하시겠습니까?');
		$('#confirmationModal').modal('show');
		
	});
});

$('#confirmSubmitBtn').click(function(){
	alert($('#confirmSubmitBtn').attr('name'));
});