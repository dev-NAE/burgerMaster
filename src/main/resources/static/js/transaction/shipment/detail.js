let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let shipmentId = $('#shipment-id').text();

function checkComplete() {
    let qsStatus = $('#qs-status').val();
    if (qsStatus === '검품완료') {
        $('#complete-btn').prop('disabled', false);
    } else {
        $('#complete-btn').prop('disabled', true);
    }
}


$(document).ready(function() {

    checkComplete();

    // [송장 출력] 버튼 누르면 생기는 일
    $('#print-invoice').on('click', function() {

        // 출력에 필요한 데이터 저장
        const printData = {
            shipmentId: shipmentId,
            saleId: $('#sale-id').val(),
            orderDate: $('#order-date').val(),
            dueDate: $('#due-date').val(),
            shipDate: $('#ship-date').val(),
            franchiseName: $('#franchise-name').val(),
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
            window.open('/tx/invoiceForm', 'popupWindow',
                `width=700, height=700, top=100, left=500, resizable=no`);

        // 출력페이지로 데이터 전송
        $(printPopup).on('load', function() {
            printPopup.postMessage(printData, "*");
        });
    });


    // 검품상태 리프레시 버튼 -> 누르면 검품상태와 처리상태 현행화
    $('#refresh-qs-status').on('click', function(event) {
        $.ajax({
            url: '/tx/syncShipStatus',
            method: 'GET',
            data: { shipmentId: shipmentId },
            success: function(response) {
                $('#shipment-status').text(response.status);
                $('#qs-status').val(response.qsStatus);
                checkComplete();
            },
            error: function(error) {
                console.error('에러 발생: ', error);
            }
        })


    });


    // [목록으로 이동] 버튼 누르면 -> 목록 페이지로 이동
    $('#back-to-list').on('click', function () {
        window.location.href = '/tx/shipList';
    });


    // [취소 - 예]
        $('#cancel-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/cancelShip',
                method: 'POST',
                data: { shipmentId: shipmentId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#cancel-sale').modal('hide');
                        alert('출하가 취소되었습니다.');
                        window.location.href = '/tx/shipList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#cancel-sale').modal('hide');
                        alert('출하 취소에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#cancel-sale').modal('hide');
                    console.log('Error: :', error)
                    alert('출하 취소처리 중 오류가 발생했습니다.')
                }
            });

        });


    // [완료 - 예]
        $('#complete-yes').on('click', function() {

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/tx/completeShip',
                method: 'POST',
                data: { shipmentId: shipmentId },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#complete-ship').modal('hide');
                        alert('출하가 완료되었습니다.');
                        window.location.href = '/tx/shipList';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#complete-ship').modal('hide');
                        alert('출하 완료에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#complete-ship').modal('hide');
                    console.log('Error: :', error)
                    alert('출하 완료처리 중 오류가 발생했습니다.')
                }
            });

        });


    });

