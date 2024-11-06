let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

let orderId = $('#order-id').text();

$(document).ready(function() {





// 발주서 출력
    $('#print-form').on('click', function() {
        window.open('/tx/orderForm', 'popupWindow', `width=700, height=900, top=100, left=500, resizable=no`);
    });



// [목록으로 이동] 시 이동
    $('#back-to-list').on('click', function () {
        window.location.href = '/tx/orderList';
    });

// [수정] 시 버튼 변경 + 폼 활성화 + 수정 후 저장
$('#modify-btn').on('click', function() {



})


// [취소 - 예]
    $('#cancel-yes').on('click', function() {

        // 중복클릭 방지용 로더(스피너) 작동
        $('#loadingSpinner').show();

        $.ajax({
            url: '/tx/cancelOrder',
            method: 'POST',
            data: { orderId: orderId },
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
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

});
