// 초기화
$(document).ready(function() {
	loadItems();
	setupEventHandlers();
});

// 이벤트 핸들러 설정
function setupEventHandlers() {
	$('#searchForm').on('submit', e => {
		e.preventDefault();
		loadItems();
	});

	$('#searchCodeBtn').on('click', updateItemCode);

	// 모달 버튼 이벤트
	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteItem);

	// change 유효성검사
	$('#itemForm input, #itemForm select').on('change', function() {
		validateField($(this));
	});

	// 저장 버튼 클릭
	$('#saveBtn').click(function() {
		let isValid = true;

		$('#itemForm input, #itemForm select').each(function() {
			if (!validateField($(this))) {
				isValid = false;
			}
		});

		if (!isValid) {
			//			Swal.fire({
			//				icon: 'error',
			//				title: '입력 오류',
			//				text: '입력값을 확인해주세요.'
			//			});
			return;
		}

		// 저장 로직 실행
		saveItem();
	});
}

// 아이템 목록 로드
function loadItems() {
	const formData = new FormData($('#searchForm')[0]);
	const searchParams = new URLSearchParams();

	for (let pair of formData.entries()) {
		if (pair[1]) {
			searchParams.append(pair[0], pair[1]);
		}
	}

	searchParams.append('includeUnused', $('#includeUnused').is(':checked'));

	$.get('/masterdata/api/items?' + searchParams.toString())
		.done(response => {
			displayItems(response.content);
			displayPagination(response);
		})
		.fail(() => alert('데이터 로드 실패'));
}

// 아이템 목록 표시
function displayItems(items) {
	const tbody = $('#itemList');
	tbody.empty();

	if (!items || items.length === 0) {
		tbody.html('<tr><td colspan="4" class="text-center">검색 결과가 없습니다.</td></tr>');
		return;
	}

	items.forEach(item => {
		tbody.append(`
            <tr onclick="openModal('detail', '${item.itemCode}')">
                <td>${item.itemCode}</td>
                <td>${item.itemName}</td>
                <td>${getItemTypeName(item.itemType)}</td>
                <td>${item.useYN === 'Y' ? '사용' : '미사용'}</td>
            </tr>
        `);
	});
}

function getItemTypeName(type) {
	return {
		'RM': '원재료',
		'PP': '가공품',
		'FP': '완제품'
	}[type] || type;
}

// 페이지네이션 함수
function displayPagination(response) {
	const pagination = $('#pagination');
	pagination.empty();

	const totalPages = Math.min(response.totalPages, 10);

	pagination.append(`
        <li class="page-item ${response.first ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="changePage(${response.number - 1}); return false;">이전</a>
        </li>
    `);

	for (let i = 0; i < totalPages; i++) {
		pagination.append(`
            <li class="page-item ${response.number === i ? 'active' : ''}">
                <a class="page-link" href="#" onclick="changePage(${i}); return false;">${i + 1}</a>
            </li>
        `);
	}

	pagination.append(`
        <li class="page-item ${response.last ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="changePage(${response.number + 1}); return false;">다음</a>
        </li>
    `);
}

function changePage(page) {
	if (page < 0 || page >= totalPages) return;

	currentPage = page;
	loadItems();
}

// 모달 열기
function openModal(mode, itemCode = null) {
	const modal = $('#itemModal');
	const viewSection = $('#viewSection');
	const form = $('#itemForm');

	// 초기화
	form[0].reset();
	$('#saveBtn, #editBtn, #deleteBtn').hide();
	viewSection.hide();
	form.hide();

	$('.is-invalid').removeClass('is-invalid');
	$('.invalid-feedback').text('');

	if (mode === 'register') {
		$('#modalTitle').text('품목 등록');
		form.show();
		$('#saveBtn').show();
		$('#itemType').prop('disabled', false);
		$('#searchCodeBtnGroup').show();
	} else if (mode === 'detail' && itemCode) {
		$('#modalTitle').text('품목 상세');
		loadItemDetail(itemCode);
	}

	modal.modal('show');
}

// 상세 정보 로드
function loadItemDetail(itemCode) {
	$.get('/masterdata/api/items/' + itemCode)
		.done(item => {
			$('#viewSection').html(getItemDetailHtml(item)).show();
			fillForm(item);
			$('#editBtn, #deleteBtn').show();
		});
}

function fillForm(item) {
	$('#itemCode').val(item.itemCode);
	$('#itemName').val(item.itemName);
	$('#itemType').val(item.itemType);
	$('#useYN').val(item.useYN);
}

function getItemDetailHtml(item) {
	return `
        <table class="table">
            <tr><th>품목코드</th><td>${item.itemCode}</td></tr>
            <tr><th>품목명</th><td>${item.itemName}</td></tr>
            <tr><th>품목유형</th><td>${getItemTypeName(item.itemType)}</td></tr>
            <tr><th>사용여부</th><td>${item.useYN === 'Y' ? '사용' : '미사용'}</td></tr>
        </table>
    `;
}

// 아이템 저장
function saveItem() {
	const mode = $('#itemForm').data('mode');
	const itemCode = $('#itemCode').val();

	const formData = $('#itemForm').serializeArray();
	const jsonData = {};
	formData.forEach(item => {
		jsonData[item.name] = item.value;
	});

	$.ajax({
		url: mode === 'edit' ?
			`/masterdata/api/items/${itemCode}` :
			'/masterdata/api/items',
		type: mode === 'edit' ? 'PUT' : 'POST',
		contentType: 'application/json',
		data: JSON.stringify(jsonData),
		success: function() {
			$('#itemModal').modal('hide');
			loadItems();
		},
		error: xhr => alert(xhr.responseJSON?.message || '저장 실패')
	});
}

// 수정 모드로 전환
function switchToEditMode() {
	$('#viewSection').hide();
	$('#itemForm').show();
	$('#editBtn').hide();
	$('#saveBtn').show();

	$('#itemCode').prop('readonly', true);
	$('#itemType').prop('disabled', true);
	$('#itemType').after(`<input type="hidden" name="itemType" value="${$('#itemType').val()}">`);
	$('#searchCodeBtnGroup').hide();
}

// 삭제
function deleteItem() {
	if (!confirm('정말 삭제하시겠습니까?')) return;

	$.ajax({
		url: '/masterdata/api/items/' + $('#itemCode').val(),
		type: 'DELETE',
		success: () => {
			$('#itemModal').modal('hide');
			loadItems();
		},
		error: () => alert('삭제 실패')
	});
}

// 품목코드 검색 버튼 시 자동입력
function updateItemCode() {
	const itemType = $('#itemType').val();
	if (itemType) {
		$.get('/masterdata/api/items/nextCode?itemType=' + itemType)
			.done(code => {
				$('#itemCode').val(code);
				validateField($('#itemCode'));
			});
	}
}

// 필드별 유효성 검사
function validateField(field) {
	const id = field.attr('id');
	const value = field.val();
	let isValid = true;

	switch (id) {
		case 'itemName':
			if (!value) {
				showError(field, '품목명을 입력하세요.');
				isValid = false;
			}
			break;

		case 'itemCode':
			const itemType = $('#itemType').val();
			if (!value) {
				if (itemType) {
					showError(field, '검색 버튼을 눌러 품목코드를 생성하세요.');
				} else {
					showError(field, '품목 유형을 먼저 선택하세요.');
				}
				isValid = false;
				break;
			}
			const codePattern = /^(RM|PP|FP)\d{3}$/;
			if (!codePattern.test(value)) {
				showError(field, '품목코드 형식이 올바르지 않습니다.');
				isValid = false;
			} else {
				const itemType = $('#itemType').val();
				if (itemType && value.substring(0, 2) !== itemType) {
					showError(field, '품목코드와 품목유형이 일치하지 않습니다.');
					isValid = false;
				}
			}
			break;

		case 'itemType':
			if (!value) {
				showError(field, '품목 유형을 선택하세요.');
				isValid = false;
			} else {
				const itemCode = $('#itemCode').val();
				if (itemCode)
				validateField($('#itemCode'));
			}
			break;
	}

	if (isValid) {
		clearError(field);
	}
	return isValid;
}

// 에러 표시
function showError(field, message) {
    field.addClass('is-invalid');
    $(`#validate${field.attr('id')}`).text(message);
}

// 에러 제거
function clearError(field) {
    field.removeClass('is-invalid');
    $(`#validate${field.attr('id')}`).text('');
}
