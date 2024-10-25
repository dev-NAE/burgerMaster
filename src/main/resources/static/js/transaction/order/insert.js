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


// 거래처 검색 팝업
$('#find-supplier').on('click', function(event) {
    openPopupAtMousePosition(event, '/findSupplier', 600, 600)
});

// 팝업에서 선택한 거래처 폼에 입력



// 물품 추가 팝업
$('#add-items').on('click', function(event) {
    openPopupAtMousePosition(event, '/addItems', 800, 600)
});


// 팝업에서 선택한 물품 목록(+히든 인풋)에 입력



// 단가 * 수량 = 소계 자동 산출
$(document).ready(function() {
    $('.item-price, .item-quantity').on('input', function() {
        const $row = $(this).closest('.item-row');
        const price = parseInt($row.find('.item-price').val()) || 0;
        const quantity = parseInt($row.find('.item-quantity').val()) || 0;

        const subtotal = price * quantity;
        $row.find('.subtotal').text(subtotal);
    });
});

// 등록된 물품 지우기
$('#delete-this-row').on('click', function(event) {

});

// 등록에 따른 총 품목개수, 합계금액 보여주기


// 발주등록 버튼: 필수항목이 비어있으면 비활성화, 모두 채워지면 활성화
// + 등록버튼 연타에 따른 중복등록 방지 (로더 넣기)
