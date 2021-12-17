// 카카오 로그인 js

// 카카오 sdk 초기화
Kakao.init('8b783d375f8f95534a8248d8fc9a14e5');

// sdk 초기화 여부 판단
console.log("kakao_initialized : " + Kakao.isInitialized());


// 로그인 이후 Redirect Url 설정 방법
// Kakao.Auth.authorize({
//     redirectUri: '이동 url '
// });

//  RestAPI 카카오 로그인
function restLogin(){
    location.href= 'https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=0283e78b831185c25b7ed36ea030a098&redirect_uri=http://localhost:8080/auth/kakao/callback';
}


// 자바스크립트 카카오로그인
function jsLogin() {
    Kakao.Auth.login({
        success: function(res) {
            // access_token
            Kakao.Auth.setAccessToken(res.access_token);

            //유저정보 가져오기
            getUser();
        },
        fail: function(err) {
            alert(JSON.stringify(err))
        },
    })
}


// 자바스크립트 카카오 로그아웃
function jsLogout(){
    if (!Kakao.Auth.getAccessToken()) {
        alert("비로그인 사용자");
        return;
    }

    Kakao.Auth.logout(function() {
        console.log(Kakao.Auth.getAccessToken());
    });

}

// 자바스크립트 카카오 유저정보가져오기
function getUser(){
    Kakao.API.request({
        url: '/v2/user/me',

        // 주석과 같이 특정 값만 가져올수있음
        // data: {
        //     property_keys: ["kakao_account.email","kakao_account.gender"]
        // },
        success: function(response) {
            console.log("유저정보 : " + JSON.stringify(response));
        },
        fail: function(error) {
            console.log(error);
        }
    });

}


// 자바스크립트 카카오 사용자정보 저장
function updateUser(){
    Kakao.API.request({
        url: '/v1/user/update_profile',

        // 예시 데이터
        data: {
            properties: {
                nickname: 'DCX',
                age: '32'
            },
        },
        success: function(response) {
            console.log(response);
        },
        fail: function(error) {
            console.log(error);
        }
    });
}

