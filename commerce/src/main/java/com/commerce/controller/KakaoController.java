package com.commerce.controller;

import com.commerce.component.JwtTokenProvider;
import com.commerce.entity.user;
import com.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class KakaoController {

    @Value("${kakao.rest}")
    private String rest_key;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // REST Api 버전
    // 카카오 로그인
    // test URL : https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=0283e78b831185c25b7ed36ea030a098&redirect_uri=http://localhost:8080/auth/kakao/callback

    @RequestMapping(value = "auth/kakao/callback")
    public ModelAndView FindAuthCode(@RequestParam("code") String code, RedirectAttributes redirectAttributes){


        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", rest_key);
        params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        params.add("code",code);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // 토큰값 Json 형식으로 가져오기위해 생성
        JSONObject jo = new JSONObject(response.getBody());

        // 토큰결과값
        System.out.println("토큰 결과값 : " + response);

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Authorization", "Bearer "+ jo.get("access_token"));
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String >> kakaoProfileRequest2= new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JSONObject jo2 = new JSONObject(response2.getBody());
        System.out.println("사용자 정보 추출 : " + jo2);




        Map<String ,String> datas = new HashMap<>();

        datas.put("kakao" , String.valueOf(jo2.get("id")));
        datas.put("image" , "IMAGE");
        datas.put("name" , "범철");


        // 디비저장
        userRepository.save(user.builder()
                .kakao(datas.get("kakao"))
                .image(datas.get("image"))
                .name(datas.get("name"))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();

        ModelAndView mav = new ModelAndView();

        // 디비 존재여부
        try{
            com.commerce.entity.user member = userRepository.findByKakao(datas.get("kakao"))
                    .orElseThrow(() -> new IllegalArgumentException("미가입자"));

            System.out.println(jwtTokenProvider.createToken(member.getUsername(), member.getImage(),member.getName(),member.getRoles()));
            mav.addObject("jwt", jwtTokenProvider.createToken(member.getUsername(),  member.getImage(),member.getName(), member.getRoles()));
        }catch(Exception e){
            // 회원가입 페이지로 이동
            System.out.println("회원가입페이지로 이동 해야 함");
            return null;
        }

        mav.setViewName("cb");
        return mav;
    }

}
