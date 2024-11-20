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

	// 유효성 검사 함수
	function validateForm() {
		var incomingInsertCode = $('#incomingInsertCode_hidden').val().trim();
		var reasonOfIncoming = $('#reasonOfIncoming_hidden').val().trim();
		var managerId = $('#managerId').val().trim();

		var isValid = true;
		var errorMessage = '';

		if (incomingInsertCode === '') {
			isValid = false;
			errorMessage += '입고 대상이 선택되지 않았습니다.\n';
		}

		if (reasonOfIncoming === '') {
			isValid = false;
			errorMessage += '입고 사유가 선택되지 않았습니다.\n';
		}

		if (managerId === '') {
			isValid = false;
			errorMessage += '담당자가 선택되지 않았습니다.\n';
		}

		if (!isValid) {
			alert(errorMessage);
		}

		return isValid;
	}

	// 폼 제출 시 유효성 검사
	$('form').on('submit', function(e) {
		if (!validateForm()) {
			e.preventDefault(); // 제출 방지
		}
	});

	// 입고 대상 검색 클릭 시 입고 등록 목록 모달창 생성		
	$('#findIncomingInsertList').on('click', function() {
		// AJAX를 통해 입고 등록 대상 목록 가져오기
		$.ajax({
			url: '/restInven/incomingInsertList',
			type: 'GET',
			dataType: 'json',
			success: function(data) {
				if (data.length > 0) {
					var incomingInsertListHtml = '';
					data.forEach(function(list) { // forEach로 수정
						incomingInsertListHtml += '<tr class="selectable-row" data-prod-or-order-id="' +
							list.prodOrOrderId + '" data-reason-of-incoming="' +
							list.reasonOfIncoming + '">';
						incomingInsertListHtml += '<td>' + list.prodOrOrderId + '</td>';
						incomingInsertListHtml += '<td>' + list.reasonOfIncoming + '</td>';
						incomingInsertListHtml += '<td>' + list.prodOrOrderDate + '</td>';
						// 조건에 따라 다른 내용의 <td> 생성
						if (list.otherCount === 0) {
							incomingInsertListHtml += '<td>' + list.incomingItemDisplay + '</td>';
						} else {
							incomingInsertListHtml += '<td>' + list.incomingItemDisplay + ' 외 ' + list.otherCount + '건' + '</td>';
						}

						incomingInsertListHtml += '<td>' + list.totalAmount + '</td>';
						incomingInsertListHtml += '</tr>'; // 닫는 태그 추가
					});
					$('#modalIncomingInsertList').html(incomingInsertListHtml);
				} else {
					$('#modalIncomingInsertList').html('<tr><td colspan="5" class="text-center">입고 등록할 대상이 없습니다.</td></tr>');
				}

				// 모달 표시
				$('#InsertListModal').modal('show'); // 올바른 선택자 사용
				// 선택된 행의 스타일 초기화
				$('.selectable-row').removeClass('active-row');
			},
			error: function(xhr, status, error) {
				console.error('입고등록할 목록을 가져오는 중 오류 발생:', error);
				$('#modalIncomingInsertList').html('<tr><td colspan="5" class="text-center text-danger">입고 품목 정보를 가져오는 데 실패했습니다.</td></tr>');
			}
		});
	});

	// 담당자 찾기 버튼 클릭 시 담당자 목록 모달창 생성		
	$('#findManagerButton').on('click', function() {
		// AJAX를 통해 담당자 목록 가져오기
		$.ajax({
			url: '/restInven/findManagerList',
			type: 'GET',
			dataType: 'json',
			success: function(data) {
				if (data.length > 0) {
					var managerListHtml = '';
					data.forEach(function(manager) {
						managerListHtml += '<tr class="selectable-manager-row" data-manager-id="' + manager.managerId + '" data-manager-name="' + manager.name + '">';
						managerListHtml += '<td>' + manager.managerId + '</td>';
						managerListHtml += '<td>' + manager.name + '</td>';
						managerListHtml += '</tr>';
					});
					$('#modalManagerList').html(managerListHtml);
				} else {
					$('#modalManagerList').html('<tr><td colspan="2" class="text-center">담당자가 없습니다.</td></tr>');
				}

				// 모달 표시
				$('#ManagerListModal').modal('show');
				// 선택된 행의 스타일 초기화
				$('.selectable-manager-row').removeClass('active-row');
			},
			error: function(xhr, status, error) {
				console.error('담당자 목록을 가져오는 중 오류 발생:', error);
				$('#modalManagerList').html('<tr><td colspan="2" class="text-center text-danger">담당자 정보를 가져오는 데 실패했습니다.</td></tr>');
			}
		});
	});

	// '담당자 목록 모달'의 행 클릭 시 입력 필드에 값 설정 및 모달 닫기
	$(document).on('click', '.selectable-manager-row', function() {
		// 모든 행의 active-row 클래스 제거
		$('.selectable-manager-row').removeClass('active-row');

		// 현재 클릭된 행에 active-row 클래스 추가
		$(this).addClass('active-row');

		// data 속성에서 값 가져오기
		var managerId = $(this).data('manager-id');
		var managerName = $(this).data('manager-name');

		// 입력 필드에 값 설정 (표시용 readonly 입력 필드)
		$('#managerName_display').val(managerName);

		// 숨겨진 입력 필드에 값 설정 (제출용 hidden 입력 필드)
		$('#managerId').val(managerId);

		// 모달 닫기
		$('#ManagerListModal').modal('hide');
	});

	// 선택 가능한 입고대상 행 클릭 시 이벤트 핸들러
	$(document).on('click', '.selectable-row', function() {
		// 모든 행의 active-row 클래스 제거
		$('.selectable-row').removeClass('active-row');

		// 현재 클릭된 행에 active-row 클래스 추가
		$(this).addClass('active-row');

		// data 속성에서 값 가져오기
		var prodOrOrderId = $(this).data('prod-or-order-id');
		var reasonOfIncoming = $(this).data('reason-of-incoming');

		// 입력 필드에 값 설정 (표시용 readonly 입력 필드)
		$('#incomingInsertCode_display').val(prodOrOrderId);
		$('#reasonOfIncoming_display').val(reasonOfIncoming);

		// 숨겨진 입력 필드에 값 설정 (제출용 hidden 입력 필드)
		$('#incomingInsertCode_hidden').val(prodOrOrderId);
		$('#reasonOfIncoming_hidden').val(reasonOfIncoming);

		// 상세 정보 테이블 초기화 및 로딩 메시지 표시
		$('#example2 tbody').html('<tr><td colspan="4" class="text-center">로딩 중...</td></tr>');

		// AJAX 요청을 통해 상세 품목 정보 가져오기
		$.ajax({
			url: '/restInven/getIncomingInsertItems',
			type: 'GET',
			data: {
				prodOrOrderId: prodOrOrderId,
				reasonOfIncoming: reasonOfIncoming
			},
			dataType: 'json',
			success: function(data) {
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
					$('#example2 tbody').html(incomingItemsHtml);
				} else {
					$('#example2 tbody').html('<tr><td colspan="4" class="text-center">입고 품목 정보가 없습니다.</td></tr>');
				}
			},
			error: function(xhr, status, error) {
				console.error('입고 품목 정보를 가져오는 중 오류 발생:', error);
				$('#example2 tbody').html('<tr><td colspan="4" class="text-center text-danger">입고 품목 정보를 가져오는 데 실패했습니다.</td></tr>');
			}
		});

		// 모달 닫기
		$('#InsertListModal').modal('hide');
	});
});