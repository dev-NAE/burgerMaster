let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let CHECK_ID = false;

$(document).ready(function(){
    $(".content tr").click(function(){
        $("#managerChangeModal").modal();
        $("#manager_id_change_modal").val($(this).children('td:eq(0)').text());
        $("#manager_name_change_modal").val($(this).children('td:eq(1)').text());
        $("#manager_phone_change_modal").val($(this).children('td:eq(2)').text());
        $("#manager_email_change_modal").val($(this).children('td:eq(3)').text());
        let roleText = $(this).children('td:eq(4)').text();
        let roles = roleText.split(",");
        $('input[name=changeRole]').prop('checked', false);
        for(let index in roles){
            $('input[name=changeRole][value='+roles[index]+']').prop("checked",true);
        }
    });
    $("#createManager").click(function(){
        $("#createModal").modal();

    });
    $('#checkId').click(function () {
        checkMangerId();
    });
    // 정규식 경고 설정
    showRegexp();
});
// 정규식 경고 설정
function showRegexp() {
    // 아이디
    $("#manager_id_create_modal").keyup(function() {
        let regexp = /^[a-zA-Z]+[a-zA-Z0-9]{5,19}$/g;
        let v = $(this).val();
        if (!regexp.test(v)) {
            $(this).addClass('is-invalid');
            CHECK_ID = false;
        }else{
            $(this).removeClass('is-invalid');
        }
    });
    // 비밀번호
    $('#manager_pass_create_modal').keyup(function() {
        let regexp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
        let v = $(this).val();
        if (!regexp.test(v)) {
            $(this).addClass('is-invalid');
        }else{
            $(this).removeClass('is-invalid');
        }
    });
    //전화 번호
    let phone = 'input[type=tel]';
    $(phone).keyup(function() {
        $(this).val($(this).val().replace(/[^0-9]/gi, "").replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`));
    });
    $(phone).focusout(function() {
        let regexp = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/;
        let v = $(this).val();
        if (!regexp.test(v)) {
            $(this).addClass('is-invalid');
        }else{
            $(this).removeClass('is-invalid');
        }
    });
    //이메일
    $('input[type=email]').focusout(function() {
        let regexp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        let v = $(this).val();
        if (!regexp.test(v)) {
            $(this).addClass('is-invalid');
        }else{
            $(this).removeClass('is-invalid');
        }
    });
}
//정규식 검사
function check_create_text(){
    let id = $('#manager_id_create_modal');
    let pass = $('#manager_pass_create_modal');
    let phone = $('#manager_phone_create_modal');
    let email = $('#manager_email_create_modal');

    let idRegexp = /^[a-zA-Z]+[a-zA-Z0-9]{5,19}$/g;
    let passRegexp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
    let phoneRegexp = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/;
    let emailRegexp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    if(!idRegexp.test(id.val())){
        id.addClass('is-invalid');
        id.focus();
        return false;
    }
    if(!passRegexp.test(pass.val())){
        id.addClass('is-invalid');
        id.focus();
        return false;
    }
    if(!phoneRegexp.test(phone.val())){
        id.addClass('is-invalid');
        id.focus();
        return false;
    }
    if(!emailRegexp.test(email.val())){
        id.addClass('is-invalid');
        id.focus();
        return false;
    }
    return true;
}
// 중복 확인
function checkMangerId(){
    let managerId = $("#manager_id_create_modal").val().trim();
    if(typeof managerId != "undefined" && managerId != null && managerId !== ''){
        $.ajax({
            type: "POST",
            url : "/manager/check/id",
            data : {
                managerId : managerId,
            },
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType : "json",
            success : function(bool){
                console.log('중복 확인 완료');
                // true 중복 있음
                if(bool){
                    let resultInfo = $('#resultCheckId');
                    $("#manager_id_create_modal").addClass('is-invalid');
                    resultInfo.removeClass();
                    resultInfo.addClass('callout callout-danger p-2');
                    resultInfo.children('p').text('중복된 아이디가 있습니다');
                    CHECK_ID = false;
                }else{
                    let resultInfo = $('#resultCheckId');
                    $("#manager_id_create_modal").removeClass('is-invalid');
                    resultInfo.removeClass();
                    resultInfo.addClass('callout callout-success p-2');
                    resultInfo.children('p').text('사용 가능한 아이디입니다');
                    CHECK_ID = true;
                }
            },error : function(){
                console.log('중복 확인 실패');
            }
        });
    }
}
//관리자 생성하기
function createManager(){
    if(CHECK_ID && check_create_text()){
        $.ajax({
            type: "POST",
            url : "/manager/create",
            data : {
                managerId : $("#manager_id_create_modal").val().trim(),
                pass : $("#manager_pass_create_modal").val().trim(),
                name : $("#manager_name_create_modal").val().trim(),
                phone : $("#manager_phone_create_modal").val().trim(),
                email : $("#manager_email_create_modal").val().trim(),
                managerRole : createCheckString('managerRole'),
            },
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType : "json",
            success : function(data){
                let manager = data;
                console.log(manager);

            },error : function(){
                console.log('관리자 생성 실패');
            }
        });
    }else{
        alert('입력 데이터에 문제가 있습니다');
    }
}
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