<!DOCTYPE html>
<html lang="kr">
<meta charset="utf-8" />
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


    <spring:eval expression="@environment.getProperty('kakao.js')" var="kakao_js"/>

</script>

<head>
    <!-- 카카오 로그인 js 임포트 start -->
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    <script src="/js/login.js"></script>
    <!-- 카카오 로그인 js 임포트 end -->

</head>
<body>
<div>
    <h2> 카카오 Rest Api 로그인 </h2>
    <img src="/images/kakao_login_medium_wide.png" onclick="restLogin();"/>
</div>

<div>
    <h2> 카카오 Javascript 로그인 </h2>
    <img src="/images/kakao_login_medium_wide.png" onclick="jsLogin();"/>
</div>

<div>
    <h2> 로그아웃 </h2>
    <button id= "btn3" onclick ="jsLogout();"> JS LOGOUT </button>
</div>


</body>
</html>