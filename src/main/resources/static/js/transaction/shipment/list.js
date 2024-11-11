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

// 물품 검색 팝업
$('#find-item').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/addSaleItems', 800, 600)
});

// 팝업에서 선택한 물품 폼에 입력
function setItemInfo(itemCode, itemName) {
    $('#item-name').val(itemName);
}

// 가맹점 검색 팝업
$('#find-franchise').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/findFranchise', 600, 600)
});

// 팝업에서 선택한 거래처 폼에 입력
function setFranchise(franchiseCode, franchiseName) {
    $('#franchise-name').val(franchiseName);
}


//검색조회

$(document).ready(function() {
    // 조회 버튼 클릭 이벤트
    $('#search-btn').on('click', function() {
        // 각 필드의 값을 가져옵니다.
        let status = $('#status').val();
        let franchiseName = $('#franchise-name').val();
        let shipDateStart = $('#ship_date-start').val();
        let shipDateEnd = $('#ship_date-end').val();
        let itemName = $('#item-name').val();
        let dueDateStart = $('#due_date-start').val();
        let dueDateEnd = $('#due_date-end').val();

        $.ajax({
            url: "/tx/searchShips",
            type: "GET",
            data: {
                status: status,
                franchiseName: franchiseName,
                shipDateStart: shipDateStart,
                shipDateEnd: shipDateEnd,
                itemName: itemName,
                dueDateStart: dueDateStart,
                dueDateEnd: dueDateEnd
            },
            dataType: "json",
            success: function(response) {
                $('#ship-list').DataTable().clear().rows.add(response).draw();
            },
            error: function(xhr, status, error) {
                console.error("검색 오류:", error);
                alert("검색 중 오류 발생. 다시 시도해 주세요.");
            }
        });
    });
});


// 데이터테이블 날짜출력포맷
let formatDate = (date) => {
    if (!date) return "";
    let d = new Date(date);
    let year = d.getFullYear();
    let month = String(d.getMonth() + 1).padStart(2, '0');
    let day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

// 데이터테이블 커스텀
$(function () {
    const dataTable = $('#ship-list').DataTable({
        ajax: {
            url: "/tx/shipInfo",
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: ''
        },
        "paging": true,
        "pageLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "info": false,
        "autoWidth": false,
        "responsive": false,
        "order": [[0, "desc"]],
        "language": {
            decimal: "",
            emptyTable: "조건에 맞는 데이터가 없습니다",
            loadingRecords: "로딩 중...",
            zeroRecords: "항목이 존재하지 않습니다",
            paginate: {
                "first": "처음",
                "last": "마지막",
                "next": "다음",
                "previous": "이전"
            }
        },
        "columns": [
            { data: "shipmentId", className: "text-center" },
            { data: "franchiseName", className: "text-center" },
            { data: "itemName", className: "text-center",
                render: function(data, type, row) {
                    return row.itemCount > 1 ? `${data} 외 ${row.itemCount - 1}건` : data;
                }
            },
            { data: "totalPrice", className: "text-right",
                render: function(data) {
                    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }
            },
            { data: "shipDate", className: "text-center",
                render: function(data) {
                    return formatDate(data);
                }
            },
            { data: "dueDate", className: "text-center",
                render: function(data) {
                    return formatDate(data);
                }
            },
            { data: "status", className: "text-center",
                render: function(data, type, row) {
                    let color = data === '출하완료' ? 'blue' : data === '출하취소' ? 'red' : 'black';
                    return `<span style="color:${color}">${data}</span>`;
                }
            }
        ],
        rowCallback: function(row, data) {

            // 페이지 영역 간격 주기
            $('.dataTables_paginate').css('margin-top', '20px');

            // 상태별 행 색상 다르게
            if (data.status === '수주완료') {
                $(row).css('background-color', 'rgba(198, 239, 255, 0.5)');
            } else if (data.status === '수주취소') {
                $(row).css('background-color', 'rgba(248, 222, 222, 0.5)');
            } else if (data.status === '검품완료') {
                $(row).css('background-color', 'rgba(255,237,150,0.5)');
            }

            // 행 누르면 상세페이지로 이동하게
            $(row).on('click', function() {
                window.location.href = `/tx/shipDetail?shipId=${data.shipmentId}`;
            });

            $(row).css('cursor', 'pointer');
        },
        dom: 'rtip', // DataTables 자체 버튼 숨김
        buttons: [
            {
                extend: 'excelHtml5',
                title: '출하 조회(' + getCurrentDateTime() + ')',
                text: '.xlsx로 저장'
            }
        ]
    });

    $('#download-to-excel').on('click', function() {
        dataTable.button('.buttons-excel').trigger();
    });
});

// 엑셀파일명 시간출력용
function getCurrentDateTime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(now.getDate()).padStart(2, '0');

    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}-${minutes}-${seconds}`;
}