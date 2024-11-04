const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

// 초기화
$(document).ready(function() {
	loadItems();
	setupEventHandlers();
});

// 이벤트 핸들러 설정
function setupEventHandlers() {
	$('#searchForm').on('submit', e => {
		e.preventDefault();
		currentPage = 0;
		loadItems();
	});

	$('#searchCodeBtn').on('click', updateItemCode);

	// 모달 버튼 이벤트
	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteItem);

	// 입력 필드: 실시간 검사 + 포커스 아웃
	$('#itemForm input').on('input blur', function() {
		validateField($(this));
	});

	// select 요소: 변경 시점
	$('#itemForm select').on('change', function() {
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
			Swal.fire({
				icon: 'error',
				title: '입력 오류',
				text: '입력값을 확인해주세요.'
			});
			return;
		}

		// 저장 로직 실행
		saveItem();
	});
}

// 아이템 목록 로드
function loadItems() {
	// 검색 조건 구성
	const searchParams = new URLSearchParams({
		page: currentPage,
		size: PAGE_SIZE
	});

	// 폼 데이터 처리
	const formData = new FormData($('#searchForm')[0]);
	for (let [key, value] of formData.entries()) {
		if (value) {
			searchParams.append(key, value);
		}
	}

	// 미사용 포함 여부
	searchParams.append('includeUnused', $('#includeUnused').is(':checked'));

	$.get('/masterdata/api/items?' + searchParams.toString())
		.done(response => {
			displayItems(response.content);
			displayPagination(response);
		})
		.fail(() => {
			Swal.fire({
				icon: 'error',
				title: '데이터 로드 실패',
				text: '데이터를 불러오는데 실패했습니다.'
			});
		});
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

// 페이지네이션
let currentPage = 0;
const PAGE_SIZE = 10;

function displayPagination(response) {
	const pagination = $('#pagination');
	pagination.empty();

	totalPages = response.totalPages;
	const maxPages = Math.min(totalPages, 10);

	pagination.append(`
	        <li class="page-item ${response.first ? 'disabled' : ''}">
	            <button class="page-link" onclick="changePage(${response.number - 1})" 
	                    ${response.first ? 'disabled' : ''}>이전</button>
	        </li>
	    `);

	for (let i = 0; i < maxPages; i++) {
		pagination.append(`
	            <li class="page-item ${response.number === i ? 'active' : ''}">
	                <button class="page-link" onclick="changePage(${i})">${i + 1}</button>
	            </li>
	        `);
	}

	pagination.append(`
	        <li class="page-item ${response.last ? 'disabled' : ''}">
	            <button class="page-link" onclick="changePage(${response.number + 1})"
	                    ${response.last ? 'disabled' : ''}>다음</button>
	        </li>
	    `);
}

function changePage(page) {
	if (page < 0 || page >= totalPages) {
		return;
	}
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
		$('#itemForm').data('mode', 'register');
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
	const formData = $('#itemForm').serializeArray();
	const jsonData = {};
	formData.forEach(item => {
		jsonData[item.name] = item.value;
	});

	const isEdit = $('#itemForm').data('mode') === 'edit';
	const itemCode = $('#itemCode').val();

	$.ajax({
		url: isEdit ? `/masterdata/api/items/${itemCode}` : '/masterdata/api/items',
		type: isEdit ? 'PUT' : 'POST',
		contentType: 'application/json',
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		data: JSON.stringify(jsonData),
		success: function() {
			Swal.fire({
				icon: 'success',
				title: isEdit ? '수정 완료' : '저장 완료',
				text: isEdit ? '성공적으로 수정되었습니다.' : '성공적으로 저장되었습니다.'
			}).then(() => {
				$('#itemModal').modal('hide');
				loadItems();
			});
		},
		error: function(xhr) {
			switch (xhr.status) {
				case 409:
					Swal.fire({
						icon: 'error',
						title: '저장 실패 (409 Conflict)',
						text: '이미 존재하는 품목코드입니다.'
					});
					break;
				case 400:
					if (xhr.responseJSON.errors) {
						const errors = xhr.responseJSON.errors;
						Object.keys(errors).forEach(field => {
							showError($(`#${field}`), errors[field]);
						});
						Swal.fire({
							icon: 'error',
							title: '입력값 오류 (400 Bad Request validated)',
							text: `검증 오류: ${Object.values(errors).join(', ')}`
						});
					} else {
						Swal.fire({
							icon: 'error',
							title: '비즈니스 규칙 위반 (400 Bad Request service)',
							text: xhr.responseJSON.message
						});
					}
					break;
				default:
					Swal.fire({
						icon: 'error',
						title: `서버 오류 (${xhr.status})`,
						text: xhr.responseJSON?.message || '오류가 발생했습니다.'
					});
			}
		}
	});
}

// 수정 모드로 전환 함수도 수정
function switchToEditMode() {
	$('#viewSection').hide();
	$('#itemForm').show();
	$('#editBtn').hide();
	$('#saveBtn').show();

	// 수정 모드 설정
	$('#itemForm').data('mode', 'edit');

	$('#itemCode').prop('readonly', true);
	$('#itemType').prop('disabled', true);
	$('#itemType').after(`<input type="hidden" name="itemType" value="${$('#itemType').val()}">`);
	$('#searchCodeBtnGroup').hide();
}

// 삭제
function deleteItem() {
	Swal.fire({
		title: '삭제 확인',
		text: '정말 삭제하시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#d33',
		cancelButtonColor: '#3085d6',
		confirmButtonText: '삭제',
		cancelButtonText: '취소'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: '/masterdata/api/items/' + $('#itemCode').val(),
				type: 'DELETE',
				beforeSend: function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				success: () => {
					$('#itemModal').modal('hide');
					loadItems();
					Swal.fire({
						icon: 'success',
						title: '삭제 완료',
						text: '성공적으로 삭제되었습니다.'
					});
				},
				error: (xhr) => {
					Swal.fire({
						icon: 'error',
						title: `삭제 실패 (${xhr.status})`,
						text: xhr.responseJSON?.message || '서버에서 오류가 발생했습니다.'
					});
				}
			});
		}
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
				validateField($('#itemType'));
			});
	}
}

// 필드별 유효성 검사
function validateField(field) {
	const id = field.attr('id');
	const value = field.val();

	switch (id) {
		case 'itemName':
			if (!value) {
				showError(field, '품목명을 입력하세요.');
				return false;
			}
			if (value.length < 2 || value.length > 100) {
				showError(field, '품목명은 2~100자 사이여야 합니다.');
				return false;
			}
			if (!/^[가-힣A-Za-z0-9\s\-\_]+$/.test(value)) {
				showError(field, '허용되지 않는 문자가 포함되어 있습니다.');
				return false;
			}
			break;

		case 'itemType':
			if (!value) {
				showError(field, '품목 유형을 선택하세요.');
				return false;
			}
			if (!['RM', 'PP', 'FP'].includes(value)) {
				showError(field, '올바른 품목 유형이 아닙니다.');
				return false;
			}
			const itemCode = $('#itemCode').val();
			if (itemCode && !itemCode.startsWith(value)) {
				showError(field, '품목코드와 품목유형이 일치하지 않습니다.');
				return false;
			}
			break;

		case 'itemCode':
			if (!value) {
				showError(field, '품목코드를 생성하세요.');
				return false;
			}
			if (!/^(RM|PP|FP)\d{3}$/.test(value)) {
				showError(field, '품목코드 형식이 올바르지 않습니다.');
				return false;
			}
			const itemType = $('#itemType').val();
			if (itemType && !value.startsWith(itemType)) {
				showError(field, '품목코드와 품목유형이 일치하지 않습니다.');
				return false;
			}
			break;

		case 'useYN':
			if (!value) {
				showError(field, '사용 여부를 선택하세요.');
				return false;
			}
			if (!['Y', 'N'].includes(value)) {
				showError(field, '올바른 사용 여부 값이 아닙니다.');
				return false;
			}
			break;
	}

	clearError(field);
	return true;
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
