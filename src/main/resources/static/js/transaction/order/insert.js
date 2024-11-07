let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

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

let $form = $('#order-form');
let $submitButton = $('#order-submit-btn');

// 입력값 기본검증
function checkInputs() {
    let allChecked = true;
    let totalQuantity = $('#total-quantity').val();
    $form.find('input[required]').each(function () {
        if (!this.value || !this.checkValidity() || parseInt(totalQuantity) === 0) {
            allChecked = false;
            return false;
        }
    });
    $submitButton.prop('disabled', !allChecked);
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

// 발주일, 납기일 검증
$('#order_date, #due_date').on('change', function() {
    let orderDate = new Date($('#order_date').val());
    let dueDate = new Date($('#due_date').val());

    // 발주날짜가 납기날짜보다 이후일 경우 알림 및 초기화
    if (orderDate && dueDate && orderDate > dueDate) {
        alert('납기일이 발주일보다 빠를 수는 없습니다');
        $(this).val('');
    }
});

// 물품 추가 팝업
$('#add-items').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/addOrderItems', 800, 600)
});

// 팝업에서 선택한 물품 목록에 입력
function setItemInfo(itemCode, itemName, itemPrice, itemQuantity, subTotal) {
    let $newItemRow = $($('#add-item-template').html());
    $newItemRow.find('.item-code').text(itemCode);
    $newItemRow.find('.item-name').text(itemName);
    $newItemRow.find('.item-price').val(itemPrice == null ? 0 : itemPrice);
    $newItemRow.find('.item-quantity').val(itemQuantity == null ? 0 : itemQuantity);

    let subtotal = parseInt(subTotal == null ? 0 : subTotal);
    $newItemRow.find('.subtotal').val(subtotal);
    $newItemRow.find('.subtotal-view').text(subtotal.toLocaleString());

    $('#item-list-table tbody').append($newItemRow);
    refreshQuantity();
    refreshTotalPrice();
    checkInputs();
}

// 엔터제출 방지
$(document).on('keydown', 'input', function(event) {
    if (event.keyCode === 13) { // Enter key
        event.preventDefault();
    }
});

// 단가, 수량 입력 시 소계, 총계 자동 산출
$(document).on('input', '.item-price, .item-quantity', function() {

    const $row = $(this).closest('.item-row');
    const price = parseInt($row.find('.item-price').val()) || 0;
    const quantity = parseInt($row.find('.item-quantity').val()) || 0;
    const subtotal = price * quantity;

    // 계산용 데이터 저장
    $row.find('.subtotal').val(subtotal);

    // 데이터 표시
    $row.find('.subtotal-view').text(subtotal.toLocaleString());
    refreshTotalPrice();
});

// 등록된 물품 지우기
$(document).on('click', '.delete-this-row', function() {
    const $row = $(this).closest('.item-row');
    $row.remove();
    refreshQuantity();
    refreshTotalPrice();
    checkInputs();
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
        let subtotalVal = parseInt($(this).val()) || 0;
        totalPrice += subtotalVal;
    })
    $('#total-price-view').text(totalPrice.toLocaleString());
    $('#total-price').val(totalPrice);
}


// 발주등록 버튼: 필수항목이 비어있으면 비활성화, 모두 채워지면 활성화
$(document).ready(function() {


    // 폼에 이벤트 위임 (품목 추가시 작동하도록)
    $form.on('input', 'input[required]', checkInputs);

    // 페이지 로드 시 적용
    checkInputs();

    // 버튼을 누르면
    $submitButton.on('click', function(event) {
        event.preventDefault();

        // 중복클릭 방지용 로더(스피너) 작동
        $('#loadingSpinner').show();

        // 주문정보 수집 = OrderDTO 형태
        var order = {
            totalPrice: parseInt($('#total-price').val()),
            orderDate: $('#order_date').val(),
            dueDate: $('#due_date').val(),
            note: $('#note').val(),
            manager: $('#manager-code').val(),
            supplierCode: $('#supplier-code').val()
        };

        // 주문품목정보 수집 = OrderItemsDTO 형태
        var items = [];
        $('#item-list-table .item-row').each(function() {
            var $item = $(this);
            var item = {
                itemCode: $item.find('.item-code').text(),
                price: parseInt($item.find('.item-price').val()),
                subtotalPrice: parseInt($item.find('.subtotal').val()),
                quantity: parseInt($item.find('.item-quantity').val())
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
            url: '/tx/saveOrder',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'text',
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify(orderRequest),
            success: function(response) {
                if (response === "success") {
                    $('#loadingSpinner').hide();
                    alert('발주가 등록되었습니다.');
                    window.location.href = '/tx/orderList';
                } else if (response === "mismatch") {
                    $('#loadingSpinner').hide();
                    alert('입력한 내용이 데이터베이스와 일치하지 않습니다')
                }
            },
            error: function(error) {
                $('#loadingSpinner').hide();
                console.log('Error: :', error)
                alert('발주 등록처리 중 오류가 발생했습니다.')
            }
        });

    })

});
