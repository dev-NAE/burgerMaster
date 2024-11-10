const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function() {
	loadBOMs();
	setupEventHandlers();
});

function setupEventHandlers() {
	$('#searchForm').on('submit', e => {
		e.preventDefault();
		currentPage = 0;
		loadBOMs();
	});

	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteBOM);

	$('#bomForm input').on('input blur', function() {
		validateField($(this));
	});

	$('#bomForm select').on('change', function() {
		validateField($(this));
	});

	$('#saveBtn').click(function() {
		let isValid = true;

		$('#bomForm input, #bomForm select').each(function() {
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
		saveBOM();
	});
}

function loadBOMs() {
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

	$.get('/masterdata/api/boms?' + searchParams.toString())
		.done(response => {
			displayBOMs(response.content);
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

function displayBOMs(boms) {
	const tbody = $('#bomList');
	tbody.empty();

	if (!boms || boms.length === 0) {
		tbody.html('<tr><td colspan="6" class="text-center">검색 결과가 없습니다.</td></tr>');
		return;
	}

	boms.forEach(bom => {
		tbody.append(`
            <tr onclick="openModal('detail', '${bom.bomId}')">
                <td>${bom.ppCode}</td>
                <td>${bom.ppName}</td>
                <td>${bom.rmCode}</td>
                <td>${bom.rmName}</td>
                <td>${bom.quantity}</td>
                <td class="text-center">
                    <span class="badge ${bom.useYN === 'Y' ? 'badge-success' : 'badge-danger'}">
                        ${bom.useYN === 'Y' ? '사용' : '미사용'}
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
	loadBOMs();
}

function openModal(mode, bomId = null) {
	const modal = $('#bomModal');
	const viewSection = $('#viewSection');
	const form = $('#bomForm');

	form[0].reset();
	$('#saveBtn, #editBtn, #deleteBtn').hide();
	viewSection.hide();
	form.hide();
	
	$('#ppSearchBtn, #rmSearchBtn').show();

	$('.is-invalid').removeClass('is-invalid');
	$('.invalid-feedback').text('');

	if (mode === 'register') {
		$('#modalTitle').text('BOM 등록');
		$('#bomForm').data('mode', 'register');
		form.show();
		$('#saveBtn').show();
	} else if (mode === 'detail' && bomId) {
		$('#modalTitle').text('BOM 상세');
		loadBOMDetail(bomId);
	}
	modal.modal('show');
}

function selectItem(type, itemCode, itemName) {
    if (type === 'PP') {
        $('#ppCode').val(itemCode);
    } else {
        $('#rmCode').val(itemCode);
    }
    $('#itemSearchModal').modal('hide');
}

function selectItem(type, itemCode, itemName) {
    if (type === 'PP') {
        $('#ppCode').val(itemCode);
        $('#processedProduct\\.itemCode').val(itemCode);
    } else {
        $('#rmCode').val(itemCode);
        $('#rawMaterial\\.itemCode').val(itemCode);
    }
    $('#itemSearchModal').modal('hide');
}

function loadBOMDetail(bomId) {
	$.get('/masterdata/api/boms/' + bomId)
		.done(bom => {
			$('#viewSection').html(getBOMDetailHtml(bom)).show();
			fillForm(bom);
			$('#editBtn, #deleteBtn').show();
		});
}

function fillForm(bom) {
    $('#bomId').val(bom.bomId);
    $('#ppCode').val(bom.processedProduct.itemCode);
    $('#rmCode').val(bom.rawMaterial.itemCode);
    $('#quantity').val(bom.quantity);
    $('#useYN').val(bom.useYN);
}

function getBOMDetailHtml(bom) {
    return ` 
	<table class="table"> 
	    <tr><th>가공품코드</th><td>${bom.processedProduct.itemCode}</td></tr> 
	    <tr><th>가공품명</th><td>${bom.processedProduct.itemName}</td></tr> 
	    <tr><th>원재료코드</th><td>${bom.rawMaterial.itemCode}</td></tr> 
	    <tr><th>원재료명</th><td>${bom.rawMaterial.itemName}</td></tr> 
	    <tr><th>수량</th><td>${bom.quantity}</td></tr> 
	    <tr><th>사용여부</th><td>${bom.useYN === 'Y' ? '사용' : '미사용'}</td></tr> 
    </table> 
    `;
}

function saveBOM() {
    const jsonData = {
        ppCode: $('#ppCode').val(), 
        rmCode: $('#rmCode').val(), 
        quantity: $('#quantity').val(), 
        useYN: $('#useYN').val() 
    };

    const isEdit = $('#bomForm').data('mode') === 'edit';
    const bomId = $('#bomId').val();

    $.ajax({
        url: isEdit ? `/masterdata/api/boms/${bomId}` : '/masterdata/api/boms',
        type: isEdit ? 'PUT' : 'POST',
        contentType: 'application/json',
        data: JSON.stringify(jsonData),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            Swal.fire({
                icon: 'success', 
                title: isEdit ? '수정 완료' : '저장 완료', 
                text: isEdit ? '성공적으로 수정되었습니다.' : '성공적으로 저장되었습니다.'
            }).then(() => {
                $('#bomModal').modal('hide');
                loadBOMs();
            });
        },
        error: handleError
    });
}

function selectItem(type, itemCode, itemName) {
	if (type === 'PP') {
		$('#processedProduct\\.itemCode').val(itemCode);
		$('#ppName').val(itemName);
	} else {
		$('#rawMaterial\\.itemCode').val(itemCode);
		$('#rmName').val(itemName);
	}
	$('#itemSearchModal').modal('hide');
}

function fillForm(bom) {
	$('#bomId').val(bom.bomId);
	$('#ppCode').val(bom.ppCode);
	$('#rmCode').val(bom.rmCode);
	$('#quantity').val(bom.quantity);
	$('#useYN').val(bom.useYN);
}

function handleError(xhr) {
	switch (xhr.status) {
		case 409:
			Swal.fire({
				icon: 'error',
				title: '저장 실패 (409 Conflict)',
				text: '이미 존재하는 BOM입니다.'
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
					title: '입력값 오류',
					text: `검증 오류: ${Object.values(errors).join(', ')}`
				});
			} else {
				Swal.fire({
					icon: 'error',
					title: '비즈니스 규칙 위반',
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

function switchToEditMode() {
    $('#viewSection').hide();
    $('#bomForm').show();
    $('#editBtn').hide();
    $('#saveBtn').show();
    $('#bomForm').data('mode', 'edit');

    $('#ppSearchBtn, #rmSearchBtn').hide();
}

function deleteBOM() {
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
				url: '/masterdata/api/boms/' + $('#bomId').val(),
				type: 'DELETE',
				beforeSend: function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				success: () => {
					$('#bomModal').modal('hide');
					loadBOMs();
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

function openItemSearch(type) {
	const modal = $('#itemSearchModal');
	$('#itemSearchModalTitle').text(type === 'PP' ? '가공품 검색' : '원재료 검색');

	loadItems(type);

	$('#itemSearchKeyword').off('input').on('input', function() {
		loadItems(type, $(this).val());
	});

	modal.modal('show');
}

function loadItems(type, keyword = '') {
	$.get('/masterdata/api/items/search', {
		itemType: type,
		itemName: keyword,
		useYN: 'Y'
	}).done(items => {
		const tbody = $('#itemSearchList');
		tbody.empty();

		if (items.length === 0) {
			tbody.append('<tr><td colspan="2" class="text-center">검색 결과가 없습니다.</td></tr>');
			return;
		}

		items.forEach(item => {
			tbody.append(`
                <tr style="cursor: pointer" 
                    onclick="selectItem('${type}', '${item.itemCode}')">
                    <td>${item.itemCode}</td>
                    <td>${item.itemName}</td>
                </tr>
            `);
		});
	});
}

function selectItem(type, itemCode) {
	if (type === 'PP') {
		$('#ppCode').val(itemCode);
	} else {
		$('#rmCode').val(itemCode);
	}
	$('#itemSearchModal').modal('hide');
}

function validateField(field) {
	const id = field.attr('id');
	const value = field.val();

	switch (id) {
		case 'ppCode':
			if (!value) {
				showError(field, '가공품을 선택하세요.');
				return false;
			}
			break;

		case 'rmCode':
			if (!value) {
				showError(field, '원재료를 선택하세요.');
				return false;
			}
			break;

		case 'quantity':
			if (!value) {
				showError(field, '수량을 입력하세요.');
				return false;
			}
			if (value <= 0) {
				showError(field, '수량은 0보다 커야 합니다.');
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