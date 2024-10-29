function openModal(mode, itemCode = null) {
	const modal = $('#itemModal');
	const viewSection = $('#viewSection');
	const form = $('#itemForm');
	const saveBtn = $('#saveBtn');
	const editBtn = $('#editBtn');
	const deleteBtn = $('#deleteBtn');

	viewSection.hide();
	form.hide();
	saveBtn.hide();
	editBtn.hide();
	deleteBtn.hide();

	if (mode === 'register') {
		$('#modalTitle').text('품목 등록');
		$('#itemCode').val('');
		$('#itemName').val('');
		$('#itemType').val('');
		$('#useYn').val('Y');
		viewSection.hide();
		form.show();
		saveBtn.show();
	} else if (mode === 'detail') {
		$('#modalTitle').text('품목 상세');
		viewSection.show();
		//조회
	}

	modal.modal('show');
}

