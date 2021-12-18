<!DOCTYPE html>
<html lang="kr">
<meta charset="utf-8" />
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
    window.onload = function(){
        localStorage.tk = '${token}';


        $.ajax({
            url: 'http://localhost:8080/user/detail',
            type: 'POST',
            headers: {"X-AUTH-TOKEN": '${token}'
                },
            success: function (data) {
                alert("성공 : "+ JSON.stringify(data));
            }
        });


    }


</script>

<head>
</head>
<body>
<div>
    <h2> 메인페이지 With JWT  </h2>
    <h4> JWT : ${token} </h4>
</div>


</body>
</html>