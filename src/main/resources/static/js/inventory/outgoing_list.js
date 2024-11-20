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
		var outgoingId = button.data('outgoing-id');
		var outgoingStartDate = button.data('outgoing-start-date');
		var outgoingEndDate = button.data('outgoing-end-date');
		var managerName = button.data('manager-name');
		var productionOrSaleId = button.data('production-or-sale-id');
		var reasonOfOutgoing = button.data('reason-of-outgoing');
		var status = button.data('status');

		// 모달의 기본 정보 채우기
		$('#modalOutgoingId').text(outgoingId);
		$('#modalOutgoingStartDate').text(outgoingStartDate);
		$('#modalOutgoingEndDate').text(outgoingEndDate);
		$('#modalManagerName').text(managerName);
		$('#modalProductionOrSaleId').text(productionOrSaleId);
		$('#modalReasonOfOutgoing').text(reasonOfOutgoing);
		$('#modalStatus').text(status);

		// 출고 상세 정보 모달창에서 "출고 완료하기" 버튼 표시 여부 결정
		if (status === '출고 진행중' && (userRoles.includes('ROLE_ADMIN') || userRoles.includes('ROLE_INVENTORY'))) {
			$('#updateOutgoingButton').show();
		} else {
			$('#updateOutgoingButton').hide();
		}

		// 출고 품목 데이터 테이블 초기화 및 로딩 메시지 표시
		$('#modalOutgoingItems').html('<tr><td colspan="4" class="text-center">로딩 중...</td></tr>');

		// AJAX 요청을 통해 출고 품목 정보 가져오기
		$.ajax({
			url: '/restInven/outgoingDetail',
			type: 'GET',
			data: { outgoingId: outgoingId },
			dataType: 'json',
			success: function(data) {
				// 출고 품목 데이터 테이블 채우기
				if (data.length > 0) {
					var outgoingItemsHtml = '';
					data.forEach(function(item) {
						outgoingItemsHtml += '<tr>';
						outgoingItemsHtml += '<td>' + item.itemCode + '</td>';
						outgoingItemsHtml += '<td>' + item.itemName + '</td>';
						outgoingItemsHtml += '<td>' + item.itemType + '</td>';
						outgoingItemsHtml += '<td>' + item.quantity + '</td>';
						outgoingItemsHtml += '</tr>';
					});
					$('#modalOutgoingItems').html(outgoingItemsHtml);
				} else {
					$('#modalOutgoingItems').html('<tr><td colspan="4" class="text-center">출고 품목 정보가 없습니다.</td></tr>');
				}

				// 모달 표시
				$('#detailModal').modal('show');
			},
			error: function(xhr, status, error) {
				console.error('출고 품목 정보를 가져오는 중 오류 발생:', error);
				$('#modalOutgoingItems').html('<tr><td colspan="4" class="text-center text-danger">출고 품목 정보를 가져오는 데 실패했습니다.</td></tr>');
			}
		});
	});

	// "출고 완료하기" 버튼 클릭 이벤트
	$('#updateOutgoingButton').on('click', function() {
		var outgoingId = $('#modalOutgoingId').text();

		if (!outgoingId) {
			alert('출고 ID가 없습니다.');
			return;
		}

		// Confirm dialog
		if (!confirm('정말 출고를 완료하시겠습니까?')) {
			return;
		}

		// AJAX 요청을 통해 상태 업데이트
		$.ajax({
			url: '/restInven/updateOutgoingStatus', // 컨트롤러 매핑과 일치
			type: 'POST',
			data: { outgoingId: outgoingId },
			dataType: 'json',
			success: function(response) {
				if (response.success) {
					alert(response.message);

					// 모달 닫기 및 페이지 새로 고침
					$('#detailModal').modal('hide');
					location.reload();
				} else {
					alert('출고 상태 업데이트에 실패했습니다: ' + response.message);
				}
			},
			error: function(xhr, status, error) {
				console.error('출고 상태 업데이트 중 오류 발생:', error);
				alert('출고 상태 업데이트 중 오류가 발생했습니다.');
			}
		});
	});
});