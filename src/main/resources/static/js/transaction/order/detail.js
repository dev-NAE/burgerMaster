let editToken = $("meta[name='_csrf']").attr("content");
let editHeader = $("meta[name='_csrf_header']").attr("content");
let orderId = $('#order-id').text();

$(document).ready(function() {

    // [발주서 출력] 버튼 누르면 생기는 일
    $('#print-form').on('click', function() {

        // 출력에 필요한 데이터 저장
        const printData = {
            orderId: orderId,
            managerName: $('#manager-name').val(),
            supplierName: $('#supplier-name').val(),
            orderDate: $('#order_date').val(),
            dueDate: $('#due_date').val(),
            totalPrice: $('#total-price').val()
        }
        printData.tableRows = $('#item-list-table .item-row').map(function() {
            return {
                itemName: $(this).find('.item-name').text(),
                itemPrice: $(this).find('.item-price').val().replace(/,/g, ''),
                itemQuantity: $(this).find('.item-quantity').val().replace(/,/g, ''),
                subtotal: $(this).find('.subtotal').text().replace(' 원', '').replace(/,/g, '')
            };
        }).get();

        // 출력페이지 팝업
        const printPopup =
            window.open('/tx/orderForm', 'popupWindow',
            `width=700, height=900, top=100, left=500, resizable=no`);

        // 출력페이지로 데이터 전송
        $(printPopup).on('load', function() {
            printPopup.postMessage(printData, "*");
        });
    });

    // [목록으로 이동] 버튼 누르면 -> 목록 페이지로 이동
    $('#back-to-list').on('click', function () {
        window.location.href = '/tx/orderList';
    });

    // [수정] 버튼 누르면 생기는 일
    $('#edit-btn').on('click', function() {

        // 현재 값 저장 ('수정취소'시 복구용)
        const originalData = {
            managerCode: $('#manager-code').val(),
            managerName: $('#manager-name').val(),
            supplierCode: $('#supplier-code').val(),
            supplierName: $('#supplier-name').val(),
            orderDate: $('#order_date').val(),
            dueDate: $('#due_date').val(),
            note: $('#note').val(),
        };

        originalData.tableRows = $('#item-list-table .item-row').map(function() {
            return {
                itemCode: $(this).find('.item-code').text(),
                itemName: $(this).find('.item-name').text(),
                itemPrice: $(this).find('.item-price').val().replace(/,/g, ''),
                itemQuantity: $(this).find('.item-quantity').val().replace(/,/g, ''),
                subtotal: $(this).find('.subtotal').text().replace(' 원', '').replace(/,/g, '')
            };
        }).get();

        // 우하단 버튼 그룹 바뀜
        $('#edit-btn').hide();
        $('#cancel-btn').hide();
        $('#complete-btn').hide();

        const cancelEditButton = $('<button>', {
            id: 'cancel-edit-btn',
            class: 'btn btn-lg btn-danger my-3 mx-2',
            text: '수정취소'
        });

        const saveChangesButton = $('<button>', {
            id: 'save-changes-btn',
            class: 'btn btn-lg btn-primary my-3 mx-2',
            text: '수정사항 저장'
        });

        $('#btn-group').append(cancelEditButton, saveChangesButton);

        // 발주서 출력 버튼 없애기
        $('#print-form').hide();

        // 물품 추가버튼 보이기
        $('#add-items').show();

        // 주문기본정보(input) 수정 입력 가능
        $('.form-control').prop('readonly', false);
        $('.item-price, .item-quantity').prop('readonly', false);

        // insert.js 스크립트 동적 적용
        if (!$('script[src="/path/to/insert.js"]').length) {
            const scriptTag = $('<script>', {src: '/js/transaction/order/insert.js'});
            $('body').append(scriptTag);
            scriptTag.on('load', function () {
                console.log("insert.js 로드 성공");
            });
        }
        setEventHandlers();

        // 기존 물품항목들 수정가능한 버전으로 바꾸기
        const tableBody = $('#item-list-table tbody');
        tableBody.empty();
        originalData.tableRows.forEach(row =>
            setItemInfo(row.itemCode, row.itemName, row.itemPrice, row.itemQuantity, row.subtotal));

        // [수정취소] 버튼 누르면 변경없이 해당 발주상세페이지 로드
        $('#cancel-edit-btn').on('click', function () {

            // 원래 값 복구
            $('#manager-code').val(originalData.managerCode);
            $('#manager-name').val(originalData.managerName);
            $('#supplier-code').val(originalData.supplierCode);
            $('#supplier-name').val(originalData.supplierName);
            $('#order_date').val(originalData.orderDate);
            $('#due_date').val(originalData.dueDate);
            $('#note').val(originalData.note);

            tableBody.empty();
            originalData.tableRows.forEach(row => {
                const newRow = $('<tr>', {class: 'item-row'});
                newRow.append($('<td>', {class: 'text-center'}));
                newRow.append($('<td>', {class: 'text-center'})
                    .append($('<span>', {class: 'item-code', text: row.itemCode})));
                newRow.append($('<td>', {class: 'text-center'})
                    .append($('<span>', {class: 'item-name', text: row.itemName})));
                newRow.append($('<td>', {class: 'text-right'}).append($('<input>', {
                    class: 'col-sm-8 item-price',
                    type: 'text',
                    value: row.itemPrice,
                    style: 'text-align:right;',
                    readonly: true
                })).append(' 원'));
                newRow.append($('<td>', {class: 'text-center'}).append($('<input>', {
                    class: 'col-sm-6 item-quantity',
                    type: 'text',
                    value: row.itemQuantity,
                    style: 'text-align:right;',
                    readonly: true
                })).append(' 단위'));
                newRow.append($('<td>', {class: 'text-right'})
                    .append($('<span>', {class: 'subtotal',
                        text: (parseInt(row.subtotal).toLocaleString() + ' 원') })));

                tableBody.append(newRow);
            });

            // 버튼 그룹 복구
            cancelEditButton.remove();
            saveChangesButton.remove();

            $('#edit-btn').show();
            $('#cancel-btn').show();
            $('#complete-btn').show();
            $('#print-form').show();

            // 물품 추가버튼 숨기기
            $('#add-items').hide();

            // 항목 입력 잠그기
            $('.form-control').prop('readonly', true);
            $('.item-price, .item-quantity').prop('readonly', true);
            $('#find-manager').off('click');
            $('#find-supplier').off('click');

            // 동적 적용된 insert.js 제거
            $('script[src="/path/to/insert.js"]').remove();

        });

        // [수정사항 저장] 버튼 누르면 변경사항 DB저장 후 변경사항 적용된 발주상세페이지 로드
        $('#save-changes-btn').on('click', function(event) {
            event.preventDefault();

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            // 주문정보 수집 = OrderDTO 형태
            var order = {
                orderId: $('#order-id').text(),
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

            // 수정 등록처리를 위한 전송
            $.ajax({
                url: '/tx/updateOrder',
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
                        alert('발주가 수정되었습니다.');
                        window.location.reload();
                    } else if (response === "mismatch") {
                        $('#loadingSpinner').hide();
                        alert('입력한 내용이 데이터베이스와 일치하지 않습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    console.log('Error: :', error)
                    alert('발주 수정처리 중 오류가 발생했습니다.')
                }
            });

        })

    });



    // [취소 - 예]
        $('#cancel-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/cancelOrder',
                method: 'POST',
                data: { orderId: orderId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(editHeader, editToken);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#cancel-order').modal('hide');
                        alert('발주가 취소되었습니다.');
                        window.location.href = '/tx/orderList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#cancel-order').modal('hide');
                        alert('발주 취소에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#cancel-order').modal('hide');
                    console.log('Error: :', error)
                    alert('발주 취소처리 중 오류가 발생했습니다.')
                }
            });

        });

    // [완료 - 예]
        $('#complete-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/completeOrder',
                method: 'POST',
                data: { orderId: orderId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#complete-order').modal('hide');
                        alert('발주가 완료되었습니다.');
                        window.location.href = '/tx/orderList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#complete-order').modal('hide');
                        alert('발주 완료에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#complete-order').modal('hide');
                    console.log('Error: :', error)
                    alert('발주 완료처리 중 오류가 발생했습니다.')
                }
            });

        });

        // 수정 - 수정취소 때 죽인 이벤트들 다시 수정 눌렀을 때 되살리는 메서드
        function setEventHandlers() {
            $('#find-manager').on('click', function(event) {
                openPopupAtMousePosition(event, '/tx/findManager', 600, 600);
            });

            $('#find-supplier').on('click', function(event) {
                openPopupAtMousePosition(event, '/tx/findSupplier', 600, 600);
            });
        }

    });

