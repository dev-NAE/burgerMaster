$(document).ready(function() {


	// CSRF 토큰 설정 (Spring Security)
	var csrfToken = $('meta[name="_csrf"]').attr('content');
	var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

	// AJAX 요청 시 CSRF 토큰을 헤더에 추가
	$.ajaxSetup({
		beforeSend: function(xhr) {
			if (csrfToken && csrfHeader) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
			}
		}
	});

	// 수정하기 버튼 클릭 시 입력 필드 활성화 및 원래 값 저장
	$('#modifyAllButton').click(function() {
		// 입력 필드 활성화
		$('.quantity-input, .minReqQuantity-input').prop('disabled', false);

		// 버튼 토글
		$('#modifyAllButton').hide();
		$('#saveAllButton').show();
		$('#cancelAllButton').show();

		// 각 입력 필드의 원래 값 저장
		$('#inventoryTableBody tr').each(function() {
			var row = $(this);
			var quantityInput = row.find('.quantity-input');
			var minReqQuantityInput = row.find('.minReqQuantity-input');

			// 이미 원래 값이 저장되어 있지 않은 경우에만 저장
			if (!quantityInput.data('original')) {
				quantityInput.data('original', quantityInput.val());
			}
			if (!minReqQuantityInput.data('original')) {
				minReqQuantityInput.data('original', minReqQuantityInput.val());
			}
		});
	});

	// 취소 버튼 클릭 시 입력 필드 비활성화 및 원래 값으로 복원
	$('#cancelAllButton').click(function() {
		// 각 행의 입력 필드를 비활성화하고 원래 값으로 복원
		$('#inventoryTableBody tr').each(function() {
			var row = $(this);
			var quantityInput = row.find('.quantity-input');
			var minReqQuantityInput = row.find('.minReqQuantity-input');

			var originalQuantity = quantityInput.data('original');
			var originalMinReqQuantity = minReqQuantityInput.data('original');

			if (originalQuantity !== undefined) {
				quantityInput.val(originalQuantity);
			}
			if (originalMinReqQuantity !== undefined) {
				minReqQuantityInput.val(originalMinReqQuantity);
			}

			// 입력 필드 비활성화
			quantityInput.prop('disabled', true);
			minReqQuantityInput.prop('disabled', true);

			// 원래 값 데이터 속성 제거
			quantityInput.removeData('original');
			minReqQuantityInput.removeData('original');
		});

		// 버튼 토글
		$('#modifyAllButton').show();
		$('#saveAllButton').hide();
		$('#cancelAllButton').hide();
	});

	// 저장 버튼 클릭 시 AJAX 요청으로 데이터 전송
	$('#saveAllButton').click(function() {
		var inventoryData = [];

		// 각 행의 데이터 수집
		$('#inventoryTableBody tr').each(function() {
			var row = $(this);
			var itemCode = row.find('td:eq(0)').text().trim();
			var quantity = row.find('.quantity-input').val().trim();
			var minReqQuantity = row.find('.minReqQuantity-input').val().trim();

			if (itemCode && quantity !== '' && minReqQuantity !== '') {
				inventoryData.push({
					itemCode: itemCode,
					quantity: parseInt(quantity),
					minReqQuantity: parseInt(minReqQuantity)
				});
			}
		});

		// 데이터 유효성 검증
		if (inventoryData.length === 0) {
			alert('수정할 항목이 없습니다.');
			return;
		}

		// AJAX 요청 보내기
		$.ajax({
			url: '/restInven/updateInventoryItems', // RestInventoryController의 배치 업데이트 엔드포인트
			type: 'POST',
			contentType: 'application/json', // JSON 형식으로 전송
			dataType: 'json', // 응답을 JSON으로 예상
			data: JSON.stringify(inventoryData),
			success: function(response) {
				if (response.success) {
					alert(response.message);
					// 페이지 새로고침하여 변경 사항 반영
					location.reload();
				} else {
					alert('오류: ' + response.message);
				}
			},
			error: function(xhr, status, error) {
				console.error('AJAX Error:', status, error);
				console.error('Response:', xhr.responseText);
				alert('서버 오류: ' + error);
			}
		});
	});
});
