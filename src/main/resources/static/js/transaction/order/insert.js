// 마우스 포인터가 위치한 곳에 팝업창 띄우기
function openPopupAtMousePosition(event, url, width, height) {
    // 마우스 위치
    const mouseX = event.screenX;
    const mouseY = event.screenY;
    // 팝업창 크기
    const popupWidth = width;
    const popupHeight = height;
    // 팝업창 위치계산
    const popupLeft = mouseX - (popupWidth / 2);
    const popupTop = mouseY - (popupHeight / 2);
    // 팝업창 열기
    window.open(url, 'popupWindow', `width=${popupWidth}, height=${popupHeight}, top=${popupTop}, left=${popupLeft}`);
}

// 담당자 검색 팝업
$('#find-manager').on('click', function(event) {
    openPopupAtMousePosition(event, '/findManager', 600, 600)
});

// 팝업에서 선택한 담당자 폼에 입력
function setManager(managerCode, managerName) {
    $('#manager-code').val(managerCode);
    $('#manager-name').val(managerName);
}

// 거래처 검색 팝업
$('#find-supplier').on('click', function(event) {
    openPopupAtMousePosition(event, '/findSupplier', 600, 600)
});

// 팝업에서 선택한 거래처 폼에 입력
function setSupplier(supplierCode, supplierName) {
    $('#supplier-code').val(supplierCode);
    $('#supplier-name').val(supplierName);
}

// 물품 추가 팝업
$('#add-items').on('click', function(event) {
    openPopupAtMousePosition(event, '/addItems', 800, 600)
});

// 팝업에서 선택한 물품 목록에 입력
function setItemInfo(itemCode, itemName) {
    let $newItemRow = $($('#add-item-template').html());
    $newItemRow.find('.item-code').text(itemCode);
    $newItemRow.find('.item-name').text(itemName);

    $('#item-list-table').append($newItemRow);
    refreshQuantity();
}

// 단가, 수량 입력 시 소계, 총계 자동 산출
$(document).on('input', '.item-price, .item-quantity', function() {
    const $row = $(this).closest('.item-row');
    const price = parseInt($row.find('.item-price').val()) || 0;
    const quantity = parseInt($row.find('.item-quantity').val()) || 0;
    const subtotal = price * quantity;

    // 계산용 데이터 저장
    $row.find('.subtotal').data('subtotal', subtotal);

    // 데이터 표시
    $row.find('.subtotal').text(subtotal.toLocaleString());
    refreshTotalPrice();
});

// 등록된 물품 지우기
$(document).on('click', '.delete-this-row', function() {
    const $row = $(this).closest('.item-row');
    $row.remove();
    refreshQuantity();
    refreshTotalPrice();
});

// 등록에 따른 총 품목개수, 합계금액 보여주기
function refreshQuantity() {
    var rowCount = $('#item-list-table .item-row').length;
    $('#total-quantity').text(rowCount);
}

function refreshTotalPrice() {
    let totalPrice = 0;
    $('.subtotal').each(function() {
        let subtotalVal = $(this).data('subtotal') || 0;
        totalPrice += subtotalVal;
    })
    $('#total-price').data('totalPrice', totalPrice);
    $('#total-price').text(totalPrice.toLocaleString());
}


// 발주등록 버튼: 필수항목이 비어있으면 비활성화, 모두 채워지면 활성화
// + 등록버튼 연타에 따른 중복등록 방지 (로더 넣기)
