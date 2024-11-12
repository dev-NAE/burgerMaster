let editToken = $("meta[name='_csrf']").attr("content");
let editHeader = $("meta[name='_csrf_header']").attr("content");
let saleId = $('#sale-id').text();

$(document).ready(function() {

    // [주문서 출력] 버튼 누르면 생기는 일
    $('#print-form').on('click', function() {

        // 출력에 필요한 데이터 저장
        const printData = {
            saleId: saleId,
            managerName: $('#manager-name').val(),
            franchiseName: $('#franchise-name').val(),
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
            window.open('/tx/saleForm', 'popupWindow',
            `width=700, height=700, top=100, left=500, resizable=no`);

        // 출력페이지로 데이터 전송
        $(printPopup).on('load', function() {
            printPopup.postMessage(printData, "*");
        });
    });

    // [목록으로 이동] 버튼 누르면 -> 목록 페이지로 이동
    $('#back-to-list').on('click', function () {
        window.location.href = '/tx/saleList';
    });

    // [수정] 버튼 누르면 생기는 일
    $('#edit-btn').on('click', function() {

        // 현재 값 저장 ('수정취소'시 복구용)
        const originalData = {
            managerCode: $('#manager-code').val(),
            managerName: $('#manager-name').val(),
            franchiseCode: $('#franchise-code').val(),
            franchiseName: $('#franchise-name').val(),
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
            text: '수정사항 저장',
            'data-toggle': 'modal',
            'data-target': '#edit-order'
        });

        $('#btn-group').append(cancelEditButton, saveChangesButton);

        // 주문서 출력 버튼 없애기
        $('#print-form').hide();

        // 물품 추가버튼 보이기
        $('#add-sale-items').show();

        // 주문기본정보(input) 수정 입력 가능
        $('.form-control').prop('readonly', false);
        $('.item-price, .item-quantity').prop('readonly', false);

        // insert.js 스크립트 동적 적용
        if (!$('script[src="/path/to/insert.js"]').length) {
            const scriptTag = $('<script>', {src: '/js/transaction/sale/insert.js'});
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

        // [수정취소] 버튼 누르면 변경없이 해당 수주상세페이지 로드
        $('#cancel-edit-btn').on('click', function () {

            // 원래 값 복구
            $('#manager-code').val(originalData.managerCode);
            $('#manager-name').val(originalData.managerName);
            $('#franchise-code').val(originalData.franchiseCode);
            $('#franchise-name').val(originalData.franchiseName);
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
            $('#add-sale-items').hide();

            // 항목 입력 잠그기
            $('.form-control').prop('readonly', true);
            $('.item-price, .item-quantity').prop('readonly', true);
            $('#find-manager').off('click');
            $('#find-franchise').off('click');

            // 동적 적용된 insert.js 제거
            $('script[src="/path/to/insert.js"]').remove();

        });

        // [수정사항 저장] - [예] 버튼 누르면 변경사항 DB저장 후 변경사항 적용된 수주상세페이지 로드
        $('#edit-yes').on('click', function(event) {
            event.preventDefault();

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            // 주문정보 수집 = SaleDTO 형태
            var sale = {
                saleId: $('#sale-id').text(),
                totalPrice: parseInt($('#total-price').val()),
                orderDate: $('#order_date').val(),
                dueDate: $('#due_date').val(),
                note: $('#note').val(),
                manager: $('#manager-code').val(),
                franchiseCode: $('#franchise-code').val()
            };

            // 주문품목정보 수집 = SaleItemsDTO 형태
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

            // 데이터 통합 = SaleRequestDTO 형태
            var saleRequest = {
                sale: sale,
                items: items
            }

            // 수정 등록처리를 위한 전송
            $.ajax({
                url: '/tx/updateSale',
                method: 'POST',
                contentType: 'application/json',
                dataType: 'text',
                beforeSend : function(xhr){
                    xhr.setRequestHeader(editHeader, editToken);
                },
                data: JSON.stringify(saleRequest),
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        alert('수주가 수정되었습니다.');
                        window.location.reload();
                    } else if (response === "mismatch") {
                        $('#loadingSpinner').hide();
                        alert('입력한 내용이 데이터베이스와 일치하지 않습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    console.log('Error: :', error)
                    alert('수주 수정처리 중 오류가 발생했습니다.')
                }
            });

        })

    });



    // [취소 - 예]
        $('#cancel-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/cancelSale',
                method: 'POST',
                data: { saleId: saleId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(editHeader, editToken);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#cancel-sale').modal('hide');
                        alert('수주가 취소되었습니다.');
                        window.location.href = '/tx/saleList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#cancel-sale').modal('hide');
                        alert('수주 취소에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#cancel-sale').modal('hide');
                    console.log('Error: :', error)
                    alert('수주 취소처리 중 오류가 발생했습니다.')
                }
            });

        });

    // [완료 - 예]
        $('#complete-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/completeSale',
                method: 'POST',
                data: { saleId: saleId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(editHeader, editToken);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#complete-sale').modal('hide');
                        alert('수주가 완료되었습니다.');
                        window.location.href = '/tx/saleList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#complete-sale').modal('hide');
                        alert('수주 완료에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#complete-sale').modal('hide');
                    console.log('Error: :', error)
                    alert('수주 완료처리 중 오류가 발생했습니다.')
                }
            });

        });

        // 수정 - 수정취소 때 죽인 이벤트들 다시 수정 눌렀을 때 되살리는 메서드
        function setEventHandlers() {
            $('#find-manager').on('click', function(event) {
                openPopupAtMousePosition(event, '/tx/findManager', 600, 600);
            });

            $('#find-franchise').on('click', function(event) {
                openPopupAtMousePosition(event, '/tx/findFranchise', 600, 600);
            });
        }

    });

