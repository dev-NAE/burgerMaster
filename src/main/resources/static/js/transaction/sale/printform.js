
window.onresize = function() {
    window.resizeTo(700, 700);
};

function printForm(){
    window.print();
    return false;
}

$(document).ready(function () {
    $(window).on('message', function(event) {
        const printData = event.originalEvent.data;
        const tableBody = $('#item-table tbody');

        $('#sale-id').text(printData.saleId);
        $('#order-date').text(printData.orderDate);
        $('#due-date').text(printData.dueDate);
        $('#franchise').text(printData.franchiseName).css('font-size', '1.5em');
        $('#total-price').text(parseInt(printData.totalPrice).toLocaleString()).css('font-weight', 'bold');
        $('#manager-name').text(printData.managerName);

        printData.tableRows.forEach((row, index) => {
            const rowNum = index + 1;
            const newRow = $('<tr>');
            newRow.append($('<td>', {text: rowNum}));
            newRow.append($('<td>', {text: row.itemName}));
            newRow.append($('<td>', {text: (parseInt(row.itemPrice).toLocaleString() + ' 원')}).css('text-align', 'right'));
            newRow.append($('<td>', {text: parseInt(row.itemQuantity).toLocaleString()}).css('text-align', 'right'));
            newRow.append($('<td>', {text: (parseInt(row.subtotal).toLocaleString() + ' 원')}).css('text-align', 'right'));
            tableBody.append(newRow);
        });
    });
});
