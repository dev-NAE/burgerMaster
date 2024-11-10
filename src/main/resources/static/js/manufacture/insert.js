/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */

$('input[type=number][maxlength]').on('input', function(ev) {
    var $this = $(this);
    var maxlength = $this.attr('maxlength');
    var value = $this.val();
    if (value && value.length >= maxlength) {
        $this.val(value.substr(0, maxlength));
    }
});

//가공품 리스트와 검색창 on
function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

//검색창 알고리즘
function filterFunction() {
  var input, filter, ul, li, a, i;
  input = document.getElementById("search");
  filter = input.value.toUpperCase();
  div = document.getElementById("myDropdown");
  a = div.getElementsByTagName("div");
  for (i = 0; i < a.length; i++) {
    txtValue = a[i].textContent || a[i].innerText;
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      a[i].style.display = "";
    } else {
      a[i].style.display = "none";
    }
  }
}

//가공품 선택 후 검색창 off
function change(itemName){
	document.getElementById('item').value = itemName;
	document.getElementById('search').value = null;
	document.getElementById("myDropdown").classList.toggle("show");
	getRM();
}

//가공품과 수량 입력값 변화 감지
function getRM() {
	const tbody = document.getElementById('rm');
	
	while(tbody.rows.length>0){
		tbody.deleteRow(0);
	}
	
	let item = $('#item').val();
	let amount = $('#amount').val();
	
	$.ajax({
		url:"/mf/getRM",
		type:"GET",
		data: {
			itemName: item
		},
		dataType:"json",
		success: function(response){
			const result = response;
			let str = "";
			$.each(result, function(i){
				str+="<tr>"
				str+="<td>"+result[i].itemCode+"</td><td>"
					+result[i].itemName+"</td><td>"
					+result[i].amount*amount+"</td><td>"
					+result[i].quantity+"</td>"
				str+="</tr>"
				$('#rm').append(str);
			});
		},
		error: function(xhr, status, error){
			console.error("데이터 불러오기 오류 : ", error);
			alert("데이터 가져오기 중 오류 발생. 다시 시도해 주세요.");
		}		
	});
};