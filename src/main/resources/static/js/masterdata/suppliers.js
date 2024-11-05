const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function() {
	loadSuppliers();
	setupEventHandlers();
});

function setupEventHandlers() {
	$('#searchForm').on('submit', e => {
		e.preventDefault();
		currentPage = 0;
		loadSuppliers();
	});

	$('#searchCodeBtn').on('click', updateSupplierCode);
	
	$('#editBtn').click(switchToEditMode);
	$('#deleteBtn').click(deleteSupplier);
	
	$('#supplierForm input').on('input blur', function() {
		validateField($(this));
	});
	$('#supplierForm select').on('change', function() {
		validateField($(this));
	});
	
	$('#saveBtn').click(function() {
		let isValid = true;
		
		$('#itemForm input, #itemForm select').each(function() {
			if (!validateField($(this))) {
				isValid = false;
			}
		});
		
		if (!isValid) {
			Swal.fire({
				icon: 'error',
				title: '입력 오류',
				text: '입력값을 확인해주세요.'
			});
			return;
		}
		
		saveSupplier();
	});
}

