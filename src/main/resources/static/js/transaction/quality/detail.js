let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let qsId = $('#qs-id').text();
let qsStatus = $('#qs-status').text();
let $submitButton = $('#complete-btn');
let $form = $('#sq-form');

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
    openPopupAtMousePosition(event, '/quality/findManager', 600, 600)
});

// 팝업에서 선택한 담당자 폼에 입력
function setManager(managerCode, managerName) {
    $('#manager-code').val(managerCode);
    $('#manager-name').val(managerName);
    checkInputs();
}

function checkInputs() {
    let allChecked = true;
    $form.find('input[required]').each(function () {
        if (!this.value || !this.checkValidity()) {
            allChecked = false;
            return false;
        }
    });
    $submitButton.prop('disabled', !allChecked);
}


$(document).ready(function() {

    // 상태가 검품완료일때, 담당자 인풋, 담당자 팝업, 메모인풋 비활성화
    if (qsStatus === '검품완료') {
        $('#manager-code').prop('readonly', true);
        $('#manager-name').prop('readonly', true);
        $('#find-manager').off('click');
        $('#note').prop('readonly', true);
    }

    // 페이지 로드 시 적용
    checkInputs();

    // [목록으로 이동] 버튼 누르면 -> 목록 페이지로 이동
    $('#back-to-list').on('click', function(event) {
        event.preventDefault();
        window.location.href = '/quality/qualityShipment';
    });

    $submitButton.on('click', function(event) {
        event.preventDefault();
    })

    // [완료 - 예]
        $('#complete-yes').on('click', function(event) {

            event.preventDefault();

            // 중복클릭 방지용 로더(스피너) 작동
            $('#loadingSpinner').show();

            $.ajax({
                url: '/quality/completeQS',
                method: 'POST',
                data: { 'qsId' : qsId,
                        'manager' : $('#manager-code').val(),
                        'note' : $('#note').val()
                },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if (response === "success") {
                        $('#loadingSpinner').hide();
                        $('#complete-qs').modal('hide');
                        alert('출하검품이 완료되었습니다.');
                        window.location.href = '/quality/qualityShipment';
                    } else {
                        $('#loadingSpinner').hide();
                        $('#complete-qs').modal('hide');
                        alert('출하 검품완료에 실패했습니다')
                    }
                },
                error: function(error) {
                    $('#loadingSpinner').hide();
                    $('#complete-ship').modal('hide');
                    console.log('Error: :', error)
                    alert('출하 검품완료처리 중 오류가 발생했습니다.')
                }
            });

        });


    });

