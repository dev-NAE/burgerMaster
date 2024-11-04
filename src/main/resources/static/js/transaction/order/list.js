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
    openPopupAtMousePosition(event, '/tx/addItems', 800, 600)
});

// 팝업에서 선택한 물품 폼에 입력
function setItemInfo(itemCode, itemName) {
    $('#item-name').val(itemName);
}

// 거래처 검색 팝업
$('#find-supplier').on('click', function(event) {
    openPopupAtMousePosition(event, '/tx/findSupplier', 600, 600)
});

// 팝업에서 선택한 거래처 폼에 입력
function setSupplier(supplierCode, supplierName) {
    $('#supplier-name').val(supplierName);
}

// 데이터테이블 커스텀
let listData = [];

$(function () {
    $('#order-list').DataTable({
        data: data,
        "paging": true,
        "pageLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "info": false,
        "autoWidth": false,
        "responsive": true,
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
        "columnDefs": [

        ],
        "columns": [
            {
                data: "orderId",
                render: function(data) {
                    return data;
                }
            },
            {
                data: "supplierName",
                render: function(data) {
                    return data;
                }
            },
            {
                data: "itemName",
                render: function(data) {
                    return data;
                }
            },
            {
                data: "totalPrice",
                render: function(data) {
                    return data;
                }
            },
            {
                data: "orderDate",
                render: function (data) {
                    return data;
                }
            },
            {
                data: "dueDate",
                render: function(data) {
                    return data;
                }
            },
            {
                data: "status",
                render: function (data) {
                    return data;
                }
            }
        ]
    });
});