let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function(){
    $(".content tr").click(function(){
        $("#managerChangeModal").modal();
        $("#manager_id_change_modal").val($(this).children('td:eq(0)').text());
        $("#manager_name_change_modal").val($(this).children('td:eq(1)').text());
        $("#manager_phone_change_modal").val($(this).children('td:eq(2)').text());
        $("#manager_email_change_modal").val($(this).children('td:eq(3)').text());
        let roles = $(this).children('td:eq(4)').text().split(",");
        for(let role in roles){
            $("input[name='changeRole'][value="+role+"]").prop("checked",true);
        }
    });
    $("#createManager").click(function(){
        $("#createModal").modal();


    });
});
// 중복 확인
function checkMangerId(){

}
//관리자 생성하기
function createManager(){
    console.log($("#manager_id_create_modal").val())
    console.log($("#manager_pass_create_modal").val())
    console.log($("#manager_name_create_modal").val())
    console.log($("#manager_phone_create_modal").val())
    console.log($("#manager_email_create_modal").val())
    console.log(createCheckString('managerRole'))
    $.ajax({
        type: "POST",
        url : "/manager/create",
        data : {
            managerId : $("#manager_id_create_modal").val(),
            pass : $("#manager_pass_create_modal").val(),
            name : $("#manager_name_create_modal").val(),
            phone : $("#manager_phone_create_modal").val(),
            email : $("#manager_email_create_modal").val(),
            managerRole : createCheckString('managerRole'),
        },
        beforeSend : function(xhr)
        {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            xhr.setRequestHeader(header, token);
        },
        contentType : "application/x-www-form-urlencoded; charset=utf-8",
        dataType : "json",
        success : function(data){
            let report = data;
        },error : function(){
            console.log('관리자 생성 실패');
        }
    });
};
function createCheckString(name){
    var len = $('input[name=' + name + ']:checked').length;
    var checkArr = [];
    if(len > 1){
        $("input[name=" + name + "]:checked").each(function(e){
            var value = $(this).val();
            checkArr.push(value);
        })
    };
    return checkArr.join(',');
}

// manager 정보 수정
function changeManager(){

}

// manager delete
function deleteManager(){

}