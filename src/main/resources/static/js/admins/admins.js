let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let CHECK_ID = false;

$(document).ready(function(){
    $('#btn_search').click(function () {
        let search = $('#search').val();
        console.log(search);
        window.location.href="/manager/list?search=" + search;
    })
    funcFillChangeModal();
    $("#createManager").click(function(){
        $("#createModal").modal();

    });
    $('#checkId').click(function () {
        checkMangerId();
    });
    // 정규식 경고 설정
    showRegexp();
});
// 수정 모달 정보 전달
function funcFillChangeModal(){
    $(".content tr").click(function(){
        $("#managerChangeModal").modal();
        $("#manager_id_change_modal").val($(this).children('td:eq(0)').text());
        $("#manager_name_change_modal").val($(this).children('td:eq(1)').text());
        $("#manager_email_change_modal").val($(this).children('td:eq(2)').text());
        $("#manager_phone_change_modal").val($(this).children('td:eq(3)').text());
        let roleText = $(this).children('td:eq(4)').text();
        let roles = roleText.split(",");
        $('input[name=changeRole]').prop('checked', false);
        if(roleText !== ''){
            for(let index in roles){
                $('input[name=changeRole][value='+roles[index]+']').prop("checked",true);
            }
        }
    });
}
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
    $('#manager_pass_change_modal').keyup(function() {
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
function check_modal_text(action){
    let id = $('#manager_id_'+ action +'_modal');
    let pass = $('#manager_pass_'+ action +'_modal');
    let phone = $('#manager_phone_'+ action +'_modal');
    let email = $('#manager_email_'+ action +'_modal');

    let idRegexp = /^[a-zA-Z]+[a-zA-Z0-9]{5,19}$/g;
    let passRegexp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
    let phoneRegexp = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/;
    let emailRegexp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    if(!idRegexp.test(id.val())){
        console.log(id.val());
        id.addClass('is-invalid');
        id.focus();
        return false;
    }
    if(!passRegexp.test(pass.val())){
        console.log(pass.val());
        pass.addClass('is-invalid');
        pass.focus();
        return false;
    }
    if(!phoneRegexp.test(phone.val())){
        console.log(phone.val());
        phone.addClass('is-invalid');
        phone.focus();
        return false;
    }
    if(!emailRegexp.test(email.val())){
        console.log(email.val());
        email.addClass('is-invalid');
        email.focus();
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
    if(CHECK_ID && check_modal_text('create')){
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
            success : function(manager){
                console.log(manager);
                let tr = $('<tr>');
                tr.append($('<td>').text(manager.managerId));
                tr.append($('<td>').text(manager.name));
                tr.append($('<td>').text(manager.email));
                tr.append($('<td>').text(manager.phone));
                tr.append($('<td>').text(manager.managerRole));
                $('#listBody').prepend(tr);
                funcFillChangeModal();

            },error : function(){
                console.log('관리자 생성 실패');
            }
        });
    }else{

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
    if(check_modal_text('change')){

        $.ajax({
            type: "POST",
            url : "/manager/update",
            data : {
                managerId : $("#manager_id_change_modal").val().trim(),
                pass : $("#manager_pass_change_modal").val().trim(),
                name : $("#manager_name_change_modal").val().trim(),
                phone : $("#manager_phone_change_modal").val().trim(),
                email : $("#manager_email_change_modal").val().trim(),
                managerRole : createCheckString('changeRole'),
            },
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType : "json",
            success : function(manager){
                console.log(manager);
                let tr = $('#' + manager.managerId);
                tr.children('td:eq(1)').text(manager.name);
                tr.children('td:eq(2)').text(manager.email);
                tr.children('td:eq(3)').text(manager.phone);
                tr.children('td:eq(4)').text(manager.managerRole);

            },error : function(request,status,error){
                console.log('관리자 수정 실패');
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
    }else{
    }
}

// manager delete
function deleteManager(){

}