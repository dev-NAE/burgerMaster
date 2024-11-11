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
    $form.find('input[required]').each(function () {
        if (!this.value) {
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
    checkInputs();
}

// 출하등록건 검색 팝업
$('#find-to-ship').on('click', function(event) {
    event.preventDefault();
    openPopupAtMousePosition(event, '/tx/findToShip', 800, 600)
});

// 팝업에서 선택한 출하등록건 폼에 정보 입력
function setToShip(saleId, items, quality, franchiseCode, franchiseName, dueDate,
                   saleItems, totalPrice, quantity) {
    $('#sale-id').val(saleId);
    $('#shipment-view').val(saleId + ': ' + items);
    $('#franchise-code').val(franchiseCode);
    $('#franchise-name').val(franchiseName);
    $('#due_date').val(dueDate);
    $('#total-price-view').text(parseInt(totalPrice).toLocaleString());
    $('#total-quantity-view').text(quantity);

    const tbody = $('#item-list-tbody');
    tbody.empty();

    saleItems.forEach(row => {
        const newRow = $('<tr>', {class: 'item-row'});
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-code', text: row.itemCode})));
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-name', text: row.itemName})));
        newRow.append($('<td>', {class: 'text-right'})
            .append($('<span>', {class: 'item-price', text: (parseInt(row.price).toLocaleString())})).append(' 원'));
        newRow.append($('<td>', {class: 'text-center'})
            .append($('<span>', {class: 'item-quantity', text: row.quantity})));
        newRow.append($('<td>', {class: 'text-right'})
            .append($('<span>', {class: 'subtotal',
                text: (parseInt(row.subtotalPrice).toLocaleString() + ' 원') })));
        tbody.append(newRow);
    });
    checkInputs();
}

// 출하등록 버튼: 필수항목이 비어있으면 비활성화, 모두 채워지면 활성화
$(document).ready(function() {

    $form.on('input', 'input[required]', checkInputs);
    // 페이지 로드 시 적용
    checkInputs();

    $('#complete-yes').on('click', function () {

        // 중복클릭 방지용 로더(스피너) 작동
        $('#loadingSpinner').show();

        var shipment = {
            saleId: $('#sale-id').val(),
            shipDate: $('#ship_date').val() + ' 00:00:00',
            note: $('#note').val(),
            manager: $('#manager-code').val()
        };

        $.ajax({
            url: '/tx/completeShip',
            method: 'POST',
            data: shipment,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (response) {
                if (response === "success") {
                    $('#loadingSpinner').hide();
                    $('#complete-ship').modal('hide');
                    alert('출하 등록이 완료되었습니다.');
                    window.location.href = '/tx/shipList';
                } else {
                    $('#loadingSpinner').hide();
                    $('#complete-ship').modal('hide');
                    alert('출하 등록에 실패했습니다.')
                }
            },
            error: function (error) {
                $('#loadingSpinner').hide();
                $('#complete-ship').modal('hide');
                console.log('Error: :', error)
                alert('출하 등록처리 중 오류가 발생했습니다.')
            }
        });
    });
});
