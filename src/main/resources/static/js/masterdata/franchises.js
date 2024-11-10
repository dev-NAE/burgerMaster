const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function() {
	loadFranchises();
	setupEventHandlers();
});

function setupEventHandlers() {
	$('#searchForm').on('submit', e => {
		e.preventDefault();
		currentPage = 0;
		loadFranchises();
	});

	$('#searchCodeBtn').on('click', updateFranchiseCode);
	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteFranchise);

	$('#franchiseForm input').on('input blur', function() {
		validateField($(this));
	});

	$('#franchiseForm select').on('change', function() {
		validateField($(this));
	});

	$('#saveBtn').click(function() {
		let isValid = true;

		$('#franchiseForm input, #franchiseForm select').each(function() {
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
		saveFranchise();
	});
}

function loadFranchises() {
	const searchParams = new URLSearchParams({
		page: currentPage,
		size: PAGE_SIZE
	});

	const formData = new FormData($('#searchForm')[0]);
	for (let [key, value] of formData.entries()) {
		if (value) {
			searchParams.append(key, value);
		}
	}

	searchParams.append('includeUnused', $('#includeUnused').is(':checked'));

	$.get('/masterdata/api/franchises?' + searchParams.toString())
		.done(response => {
			displayFranchises(response.content);
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

function displayFranchises(franchises) {
	const tbody = $('#franchiseList');
	tbody.empty();

	if (!franchises || franchises.length === 0) {
		tbody.html('<tr><td colspan="9" class="text-center">검색 결과가 없습니다.</td></tr>');
		return;
	}

	franchises.forEach(franchise => {
		tbody.append(`
            <tr onclick="openModal('detail', '${franchise.franchiseCode}')">
                <td>${franchise.franchiseCode}</td>
                <td>${franchise.franchiseName}</td>
                <td>${franchise.ownerName}</td>
                <td>${franchise.businessNumber}</td>
                <td>${franchise.email || ''}</td>
                <td>${franchise.address}</td>
                <td>${franchise.contractStartDate}</td>
                <td>${franchise.contractEndDate || ''}</td>
                <td class="text-center">
                    <span class="badge ${franchise.useYN === 'Y' ? 'badge-success' : 'badge-danger'}">
                        ${franchise.useYN === 'Y' ? '사용' : '미사용'}
                    </span>
                </td>
            </tr>
        `);
	});
}

let currentPage = 0;
const PAGE_SIZE = 10;
let totalPages = 0;

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
	loadFranchises();
}

function openModal(mode, franchiseCode = null) {
	const modal = $('#franchiseModal');
	const viewSection = $('#viewSection');
	const form = $('#franchiseForm');

	form[0].reset();
	$('#saveBtn, #editBtn, #deleteBtn').hide();
	viewSection.hide();
	form.hide();

	$('.is-invalid').removeClass('is-invalid');
	$('.invalid-feedback').text('');

	if (mode === 'register') {
		$('#modalTitle').text('가맹점 등록');
		$('#franchiseForm').data('mode', 'register');
		form.show();
		$('#saveBtn').show();
		$('#searchCodeBtnGroup').show();
	} else if (mode === 'detail' && franchiseCode) {
		$('#modalTitle').text('가맹점 상세');
		loadFranchiseDetail(franchiseCode);
	}
	modal.modal('show');
}

function loadFranchiseDetail(franchiseCode) {
	$.get('/masterdata/api/franchises/' + franchiseCode)
		.done(franchise => {
			$('#viewSection').html(getFranchiseDetailHtml(franchise)).show();
			fillForm(franchise);
			$('#editBtn, #deleteBtn').show();
		});
}

function fillForm(franchise) {
	$('#franchiseCode').val(franchise.franchiseCode);
	$('#franchiseName').val(franchise.franchiseName);
	$('#ownerName').val(franchise.ownerName);
	$('#businessNumber').val(franchise.businessNumber);
	$('#email').val(franchise.email);
	$('#address').val(franchise.address);
	$('#contractStartDate').val(franchise.contractStartDate);
	$('#contractEndDate').val(franchise.contractEndDate);
	$('#useYN').val(franchise.useYN);
}

function getFranchiseDetailHtml(franchise) {
	return `
        <table class="table">
            <tr><th>가맹점코드</th><td>${franchise.franchiseCode}</td></tr>
            <tr><th>가맹점명</th><td>${franchise.franchiseName}</td></tr>
            <tr><th>점주명</th><td>${franchise.ownerName}</td></tr>
            <tr><th>사업자번호</th><td>${franchise.businessNumber}</td></tr>
            <tr><th>이메일</th><td>${franchise.email || '-'}</td></tr>
            <tr><th>주소</th><td>${franchise.address}</td></tr>
            <tr><th>계약시작일</th><td>${franchise.contractStartDate}</td></tr>
            <tr><th>계약종료일</th><td>${franchise.contractEndDate || '-'}</td></tr>
            <tr><th>사용여부</th><td>${franchise.useYN === 'Y' ? '사용' : '미사용'}</td></tr>
        </table>
    `;
}

function saveFranchise() {
	const formData = $('#franchiseForm').serializeArray();
	const jsonData = {};
	formData.forEach(item => {
		jsonData[item.name] = item.value;
	});

	const isEdit = $('#franchiseForm').data('mode') === 'edit';
	const franchiseCode = $('#franchiseCode').val();

	$.ajax({
		url: isEdit ? `/masterdata/api/franchises/${franchiseCode}` : '/masterdata/api/franchises',
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
				$('#franchiseModal').modal('hide');
				loadFranchises();
			});
		},
		error: function(xhr) {
			switch (xhr.status) {
				case 409:
					const errorMessage = xhr.responseJSON?.message;
					if (errorMessage.includes('사업자번호')) {
						Swal.fire({
							icon: 'error',
							title: '저장 실패 (409 Conflict)',
							text: '이미 등록된 사업자번호입니다.'
						});
					} else if (errorMessage.includes('가맹점코드')) {
						Swal.fire({
							icon: 'error',
							title: '저장 실패 (409 Conflict)',
							text: '이미 존재하는 가맹점코드입니다.'
						});
					} else {
						Swal.fire({
							icon: 'error',
							title: '저장 실패 (409 Conflict)',
							text: errorMessage || '중복된 데이터가 존재합니다.'
						});
					}
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

function switchToEditMode() {
	$('#viewSection').hide();
	$('#franchiseForm').show();
	$('#editBtn').hide();
	$('#saveBtn').show();

	$('#franchiseForm').data('mode', 'edit');
	$('#franchiseCode').prop('readonly', true);
	$('#searchCodeBtnGroup').hide();
}

function deleteFranchise() {
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
				url: '/masterdata/api/franchises/' + $('#franchiseCode').val(),
				type: 'DELETE',
				beforeSend: function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				success: () => {
					$('#franchiseModal').modal('hide');
					loadFranchises();
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

function updateFranchiseCode() {
	$.get('/masterdata/api/franchises/nextCode')
		.done(code => {
			$('#franchiseCode').val(code);
			validateField($('#franchiseCode'));
		});
}

function validateField(field) {
	const id = field.attr('id');
	const value = field.val();

	switch (id) {
		case 'franchiseCode':
			if (!value) {
				showError(field, '가맹점코드를 검색하세요.');
				return false;
			}
			if (!/^FR\d{3}$/.test(value)) {
				showError(field, '가맹점코드 형식이 올바르지 않습니다.');
				return false;
			}
			break;

		case 'franchiseName':
			if (!value) {
				showError(field, '가맹점명을 입력하세요.');
				return false;
			}
			if (value.length < 2 || value.length > 100) {
				showError(field, '가맹점명은 2~100자 사이여야 합니다.');
				return false;
			}
			break;

		case 'ownerName':
			if (!value) {
				showError(field, '점주명을 입력하세요.');
				return false;
			}
			if (value.length > 50) {
				showError(field, '점주명은 50자 이하여야 합니다.');
				return false;
			}
			if (!/^[가-힣A-Za-z\s]+$/.test(value)) {
				showError(field, '점주명에 허용되지 않는 문자가 포함되어 있습니다.');
				return false;
			}
			break;

		case 'businessNumber':
			if (!value) {
				showError(field, '사업자번호를 입력하세요.');
				return false;
			}
			if (!/^\d{3}-\d{2}-\d{5}$/.test(value)) {
				showError(field, '사업자번호 형식이 올바르지 않습니다.');
				return false;
			}
			break;

		case 'email':
			if (value && !/^[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/.test(value)) {
				showError(field, '이메일 형식이 올바르지 않습니다.');
				return false;
			}
			if (value.length > 100) {
				showError(field, '이메일은 100자 이하여야 합니다.');
				return false;
			}
			break;

		case 'address':
			if (!value) {
				showError(field, '주소를 입력하세요.');
				return false;
			}
			if (value.length > 200) {
				showError(field, '주소는 200자 이하여야 합니다.');
				return false;
			}
			break;

		case 'contractStartDate':
			if (!value) {
				showError(field, '계약시작일을 입력하세요.');
				return false;
			}
			break;

		case 'contractEndDate':
			if (value && new Date(value) < new Date($('#contractStartDate').val())) {
				showError(field, '계약종료일은 계약시작일 이후여야 합니다.');
				return false;
			}
			break;

		case 'useYN':
			if (!['Y', 'N'].includes(value)) {
				showError(field, '올바른 사용 여부 값이 아닙니다.');
				return false;
			}
			break;
	}
	clearError(field);
	return true;
}

function showError(field, message) {
	field.addClass('is-invalid');
	$(`#validate${field.attr('id')}`).text(message);
}

function clearError(field) {
	field.removeClass('is-invalid');
	$(`#validate${field.attr('id')}`).text('');
}

// - 자동추가
$('input[name="businessNumber"]').on('input', function() {
	let value = this.value.replace(/[^0-9]/g, '');
	if (value.length > 10) {
		value = value.substr(0, 10);
	}

	let formattedValue = '';
	if (value.length > 5) {
		formattedValue = value.substr(0, 3) + '-' + value.substr(3, 2) + '-' + value.substr(5);
	} else if (value.length > 3) {
		formattedValue = value.substr(0, 3) + '-' + value.substr(3);
	} else {
		formattedValue = value;
	}

	this.value = formattedValue;
});