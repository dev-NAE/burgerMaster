
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

        $('#ship-id').text(printData.shipmentId);
        $('#sale-id').text(printData.saleId);
        $('#order-date').text(printData.orderDate);
        $('#due-date').text(printData.dueDate);
        $('#ship-date').text(printData.shipDate);
        $('#franchise').text(printData.franchiseName).css('font-size', '1.5em');
        $('#total-price').text(parseInt(printData.totalPrice).toLocaleString()).css('font-weight', 'bold');

        console.log(printData.shipDate);

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
