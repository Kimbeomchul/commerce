<!DOCTYPE html>
<html lang="kr">
<meta charset="utf-8" />
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">
    window.onload = function(){

        let regdate = new Date();
        document.getElementById("date").value = regdate.toLocaleString();
        console.log(regdate.toLocaleString());
    }

</script>

<head>
</head>
<body>
<div>
    <form action="http://localhost:8080/register" method="POST" class="addBook shadow" onsubmit="DoJoinForm__submit(this); return false;">

        <h2>회원가입</h2>
        <div class="textForm">
            <input name="name" type="text" class="item_title" placeholder="이름" value="${name}" required>
        </div>
        <div class="textForm">
            <input name="email" type="text" class="item_image" placeholder="이메일" value="${email}"required>
        </div>
        <div class="textForm">
            <input name="age" type="text" class="item_content" placeholder="나이" value="${age}"required>
        </div>

        <input type="hidden" name="social_id" value="${social_id}">
        <input type="hidden" name="image" value="${image}">
        <input type="hidden" name="connected" value="${connected}">
        <input id ="date" type="hidden" name="regdate" value="">

        <input type="submit" class="btn22" value="등록"/>

    </form>
</div>


</body>
</html>