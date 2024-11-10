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

let $form = $('#ship-form');
let $submitButton = $('#ship-submit-btn');

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

// 출하등록건 검색 팝업
$('#find-to-ship').on('click', function(event) {
    event.preventDefault();
    openPopupAtMousePosition(event, '/tx/findToShip', 800, 600)
});

// 팝업에서 선택한 출하등록건 폼에 정보 입력
function setToShip(saleId, items, quality, franchiseCode, franchiseName, orderDate, dueDate,
                   saleItems, totalPrice, quantity) {
    $('#shipment-view').val(saleId + ': ' + items);
    $('#order_date').val(orderDate);
    $('#franchise-code').val(franchiseCode);
    $('#franchise-name').val(franchiseName);
    $('#due_date').val(dueDate);
    $('#total-price-view').text(totalPrice);
    $('#total-quantity-view').text(quantity);

    const tbody = $('#item-list-tbody');
    tbody.empty();
    console.log(saleItems);

    saleItems.forEach(row => {
        const newRow = $('<tr>', {class: 'item-row'});
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-code', text: row.itemCode})));
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-name', text: row.itemName})));
        newRow.append($('<td>', {class: 'text-right'})
            .append($('<span>', {class: 'item-price', text: row.price})).append(' 원'));
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-quantity', text: row.quantity})).append(' 단위'));
        newRow.append($('<td>', {class: 'text-right'})
            .append($('<span>', {class: 'subtotal',
                text: (parseInt(row.subtotalPrice).toLocaleString() + ' 원') })));


        tbody.append(newRow);
    });

}


/*
// 수주등록 버튼: 필수항목이 비어있으면 비활성화, 모두 채워지면 활성화
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
        var sale = {
            totalPrice: parseInt($('#total-price').val()),
            orderDate: $('#order_date').val(),
            dueDate: $('#due_date').val(),
            note: $('#note').val(),
            manager: $('#manager-code').val(),
            franchiseCode: $('#franchise-code').val()
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
        var saleRequest = {
            sale: sale,
            items: items
        }

        // 등록처리를 위한 전송
        $.ajax({
            url: '/tx/saveSale',
            method: 'POST',
            contentType: 'application/json',
            dataType: 'text',
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify(saleRequest),
            success: function(response) {
                if (response === "success") {
                    $('#loadingSpinner').hide();
                    alert('수주가 등록되었습니다.');
                    window.location.href = '/tx/saleList';
                } else if (response === "mismatch") {
                    $('#loadingSpinner').hide();
                    alert('입력한 내용이 데이터베이스와 일치하지 않습니다')
                }
            },
            error: function(error) {
                $('#loadingSpinner').hide();
                console.log('Error: :', error)
                alert('수주 등록처리 중 오류가 발생했습니다.')
            }
        });

    })



});
 */