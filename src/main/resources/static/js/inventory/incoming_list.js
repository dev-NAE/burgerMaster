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

	// "상세" 버튼 클릭 이벤트
	$('.detail-button').on('click', function() {
		var button = $(this); // 버튼 요소 참조
		var incomingId = button.data('incoming-id');
		var incomingStartDate = button.data('incoming-start-date');
		var incomingEndDate = button.data('incoming-end-date');
		var managerName = button.data('manager-name');
		var productionOrOrderId = button.data('production-or-order-id');
		var reasonOfIncoming = button.data('reason-of-incoming');
		var status = button.data('status');

		// 모달의 기본 정보 채우기
		$('#modalIncomingId').text(incomingId);
		$('#modalIncomingStartDate').text(incomingStartDate);
		$('#modalIncomingEndDate').text(incomingEndDate);
		$('#modalManagerName').text(managerName);
		$('#modalProductionOrOrderId').text(productionOrOrderId);
		$('#modalReasonOfIncoming').text(reasonOfIncoming);
		$('#modalStatus').text(status);

		// 입고 상세 정보 모달창에서 "입고 완료하기" 버튼 표시 여부 결정
		if (status === '입고 진행중' && (userRoles.includes('ROLE_ADMIN') || userRoles.includes('ROLE_INVENTORY'))) {
			$('#updateIncomingButton').show();
		} else {
			$('#updateIncomingButton').hide();
		}

		// 입고 품목 데이터 테이블 초기화 및 로딩 메시지 표시
		$('#modalIncomingItems').html('<tr><td colspan="4" class="text-center">로딩 중...</td></tr>');

		// AJAX 요청을 통해 입고 품목 정보 가져오기
		$.ajax({
			url: '/restInven/incomingDetail',
			type: 'GET',
			data: { incomingId: incomingId },
			dataType: 'json',
			success: function(data) {
				// 입고 품목 데이터 테이블 채우기
				if (data.length > 0) {
					var incomingItemsHtml = '';
					data.forEach(function(item) {
						incomingItemsHtml += '<tr>';
						incomingItemsHtml += '<td>' + item.itemCode + '</td>';
						incomingItemsHtml += '<td>' + item.itemName + '</td>';
						incomingItemsHtml += '<td>' + item.itemType + '</td>';
						incomingItemsHtml += '<td>' + item.quantity + '</td>';
						incomingItemsHtml += '</tr>';
					});
					$('#modalIncomingItems').html(incomingItemsHtml);
				} else {
					$('#modalIncomingItems').html('<tr><td colspan="4" class="text-center">입고 품목 정보가 없습니다.</td></tr>');
				}

				// 모달 표시
				$('#detailModal').modal('show');
			},
			error: function(xhr, status, error) {
				console.error('입고 품목 정보를 가져오는 중 오류 발생:', error);
				$('#modalIncomingItems').html('<tr><td colspan="4" class="text-center text-danger">입고 품목 정보를 가져오는 데 실패했습니다.</td></tr>');
			}
		});
	});

	// "입고 완료하기" 버튼 클릭 이벤트
	$('#updateIncomingButton').on('click', function() {
		var incomingId = $('#modalIncomingId').text();

		if (!incomingId) {
			alert('입고 ID가 없습니다.');
			return;
		}

		// Confirm dialog
		if (!confirm('정말 입고를 완료하시겠습니까?')) {
			return;
		}

		// AJAX 요청을 통해 상태 업데이트
		$.ajax({
			url: '/restInven/updateIncomingStatus', // 컨트롤러 매핑과 일치
			type: 'POST',
			data: { incomingId: incomingId },
			dataType: 'json',
			success: function(response) {
				if (response.success) {
					alert(response.message);

					// 모달 닫기 및 페이지 새로 고침
					$('#detailModal').modal('hide');
					location.reload();
				} else {
					alert('입고 상태 업데이트에 실패했습니다: ' + response.message);
				}
			},
			error: function(xhr, status, error) {
				console.error('입고 상태 업데이트 중 오류 발생:', error);
				alert('입고 상태 업데이트 중 오류가 발생했습니다.');
			}
		});
	});
});
