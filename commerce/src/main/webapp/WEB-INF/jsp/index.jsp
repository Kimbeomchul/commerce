<!DOCTYPE html>
<html lang="kr">
<meta charset="utf-8" />
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script src="https://apis.google.com/js/platform.js" async defer></script>

<script type="text/javascript">

    function login(login_with){
        location.href = "http://localhost:8080/login?login_with="+login_with;
    }

</script>

<head>
    <!-- 카카오 로그인 js 임포트 start -->
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    <script src="/js/login.js"></script>
    <!-- 카카오 로그인 js 임포트 end -->

</head>
<body>

<div>
    <h2> 네이버 로그인 </h2>
    <img src="/images/naverLogin.png" onclick="login('naver');"/>

</div>
<div>
    <h2> 카카오 로그인 </h2>
    <img src="/images/kakaoLogin.png" onclick="login('kakao');"/>
</div>
<div>
    <h2> 구글 로그인 </h2>
    <img src="/images/googleLogin.png" onclick="login('google');"/>
</div>
<div>
    <h2> 페이스북 로그인 </h2>
    <img src="/images/facebookLogin.png" onclick="login('facebook');"/>
</div>

<div>
    <h2> 로그아웃 </h2>
    <button id= "btn3" onclick ="jsLogout();"> JS LOGOUT </button>
</div>


</body>
</html>