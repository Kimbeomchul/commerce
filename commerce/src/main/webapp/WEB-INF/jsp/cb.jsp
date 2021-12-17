<!DOCTYPE html>
<html lang="kr">
<meta charset="utf-8" />
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script type="text/javascript">
    function getDetail(){
        var jwt = '${jwt}';


        const headers = {
            'X-AUTH-TOKEN': jwt,
        }

        axios.post('http://localhost:8080/user/detail', {}, {headers})
            .then(res => { // headers: {…} 로 들어감.
                console.log('send ok', res.data)
            })
    }
</script>

<head>
</head>
<body>
<div>
    <h2> 사용자 정보 불러오기  </h2>
    <img src="/images/kakao_login_medium_wide.png" onclick="getDetail();"/>
</div>


</body>
</html>