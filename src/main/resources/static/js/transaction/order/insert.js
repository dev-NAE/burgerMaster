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
    openPopupAtMousePosition(event, '/tx/findManager', 600, 600)
});

// 팝업에서 선택한 담당자 폼에 입력
function setManager(managerCode, managerName) {
    $('#manager-code').val(managerCode);
    $('#manager-name').val(managerName);
}

// 거래처 검색 팝업
$('#find-supplier').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/findSupplier', 600, 600)
});

// 팝업에서 선택한 거래처 폼에 입력
function setSupplier(supplierCode, supplierName) {
    $('#supplier-code').val(supplierCode);
    $('#supplier-name').val(supplierName);
}

// 물품 추가 팝업
$('#add-items').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/addItems', 800, 600)
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
    $('#total-quantity-view').text(rowCount);
    $('#total-quantity').val(rowCount);
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
$(document).ready(function() {
    var $form = $('#order-form');
    var $submitButton = $('#order-submit-btn');

    function checkInputs() {
        var allChecked = true;
        var totalQuantity = $('#total-quantity').val();
        $form.find('input[required]').each(function () {
            if (!this.value || !this.checkValidity() || parseInt(totalQuantity) === 0) {
                allChecked = false;
                return false;
            }
        });
        $submitButton.prop('disabled', !allChecked);
    }

    // 폼에 이벤트 위임 (품목 추가시 작동하도록)
    $form.on('input', 'input[required]', checkInputs);

    // 페이지 로드 시 적용
    checkInputs();

    // 버튼을 누르면
    $submitButton.on('click', function(event) {
        event.preventDefault();

        // 유효성 검사 + 메시지

        // 주문정보 수집 = OrderDTO 형태
        var totalPrice = $('total-price').text();
        var order = {
            totalPrice: parseInt(totalPrice),
            orderDate: $('#order_date').val(),
            dueDate: $('#due_date').val(),
            note: $('#note').val(),
            manager: $('#manager-code').val(),
            supplier_code: $('#supplier-code').val()
        };

        // 주문품목정보 수집 = OrderItemsDTO 형태
        var items = [];
        $('#item-list-table .item-row').each(function() {
            var $item = $(this);
            var item = {
                itemCode: $item.find('.item-code').text(),
                subtotalPrice: $item.find('.subtotal').text(),
                quantity: $item.find('.item-quantity').val()
            }
            items.push(item);
        });

        // 데이터 통합 = OrderRequestDTO 형태
        var orderRequest = {
            order: order,
            items: items
        }

        // 등록처리를 위한 전송
        $.ajax({
            url: '/saveOrder',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(orderRequest),
            success: function(response) {
                alert('발주가 등록되었습니다.');
            },
            error: function(error) {
                console.log('Error: :', error)
                alert('발주 등록처리 중 오류가 발생했습니다.')
            }
        });



    })


});

// + 등록버튼 연타에 따른 중복등록 방지 (로더 넣기)
