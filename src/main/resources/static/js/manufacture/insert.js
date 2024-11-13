let over = false;

//수량 최대 자릿수
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
  a = div.getElementsByTagName("li");
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
function change(itemName, itemCode){
	document.getElementById('item').value = itemName;
	document.getElementById('itemCode').value = itemCode;
	document.getElementById('search').value = null;
	document.getElementById("myDropdown").classList.toggle("show");
	getRM();
}

//재료표 리셋
function reset(){
	const tbody = document.getElementById('rm');
		
	while(tbody.rows.length>0){
		tbody.deleteRow(0);
	}
	
	over = false;
}

//가공품과 수량 입력값 변화 감지
function getRM() {
	
	reset();
	
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
			console.log(result);
			
			let str = "";
			$.each(result, function(i){
				str = "";
				str+="<tr>";
				str+="<td>"+result[i].itemCode+"</td><td>"
					+result[i].itemName+"</td>";
				if((result[i].amount*amount)>result[i].quantity){
					str+="<td style=\"color:red;\">"
					over = true;
				} else {
					str+="<td>"
				};
				str+=result[i].amount*amount+"</td><td>"
					+result[i].quantity+"</td>";
				str+="</tr>";
				$('#rm').append(str);
			});
			
			if(over) {
				$('#confirmationModalLabel').html('작업 지시를 등록하시겠습니까?<br>(현재 보유 중인 재료가 충분하지 않습니다.)');
			} else {
				$('#confirmationModalLabel').text('작업 지시를 등록하시겠습니까?');
			}
		},
		error: function(xhr, status, error){
			console.error("데이터 불러오기 오류 : ", error);
			alert("데이터 가져오기 중 오류 발생. 다시 시도해 주세요.");
		}		
	});
}

//입력값이 모두 입력되면 제출 버튼 활성화
function inputCheck() {
	if(($("#item").val()=='')||($("#amount").val()=='')||($("#deadline").val()=='')){
		$('#submit-btn').attr("disabled", true);
	} else {
		$('#submit-btn').attr("disabled", false);
	}
}

$(function(){
	$('#item').on('input', function(){
		inputCheck();
	})
	$('#amount').on('input', function(){
		inputCheck();
	})
	$('#deadline').on('input', function(){
		inputCheck();
	})
})

//리셋 버튼 함수
$("button[type='reset']").click(function () {
	$('#submit-btn').attr("disabled", true);
	reset();
});

//모달 활성화
$(document).ready(function() {
    $('#submit-btn').click(function() {
        $('#confirmationModal').modal('show');
    });
	
    $('#confirmSubmitBtn').click(function() {
        $('#input-form').unbind('submit').submit();
		alert('작업 지시가 등록되었습니다.');
    });
});

//날짜 포맷 변경
let now = Date.now();
let timeOff = new Date().getTimezoneOffset()*60000;
let today = new Date(now-timeOff).toISOString().split("T")[0];

document.getElementById('deadline').setAttribute("min", today);
