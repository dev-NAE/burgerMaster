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
	$('#saveBtn').click(saveItem);
	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteItem);
}

// 아이템 목록 로드
function loadItems() {
    $.get('/masterdata/api/items?' + $('#searchForm').serialize())
        .done(response => {
            displayItems(response.content);
            displayPagination(response);
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
                <td>${item.useYn === 'Y' ? '사용' : '미사용'}</td>
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
//	$('.btn-action').hide();
	viewSection.hide();
	form.hide();
	$('#saveBtn, #editBtn, #deleteBtn').hide();

	if (mode === 'register') {
		$('#modalTitle').text('품목 등록');
		$('#itemForm').data('mode', 'register');
		form.show();
		$('#saveBtn').show();
	} else if (mode === 'detail') {
		$('#modalTitle').text('품목 상세');
		$('#itemForm').data('mode', 'edit');
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
	$('#useYn').val(item.useYn);
}

function getItemDetailHtml(item) {
	return `
        <table class="table">
            <tr><th>품목코드</th><td>${item.itemCode}</td></tr>
            <tr><th>품목명</th><td>${item.itemName}</td></tr>
            <tr><th>품목유형</th><td>${getItemTypeName(item.itemType)}</td></tr>
            <tr><th>사용여부</th><td>${item.useYn === 'Y' ? '사용' : '미사용'}</td></tr>
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
//        error: () => alert('삭제 실패')
    });
}

// 품목코드 검색 버튼 시 자동입력
function updateItemCode() {
    const itemType = $('#itemType').val();
    if (itemType) {
        $.get('/masterdata/api/items/nextCode?itemType=' + itemType)
            .done(code => $('#itemCode').val(code));
    }
}

