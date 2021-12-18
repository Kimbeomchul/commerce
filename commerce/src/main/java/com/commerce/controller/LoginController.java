package com.commerce.controller;

import com.commerce.component.JwtTokenProvider;
import com.commerce.entity.user;
import com.commerce.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;


@RequiredArgsConstructor
@RestController
public class LoginController {


    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Value("${kakao.rest}")
    private String kakao_rest;

    @Value("${naver.client}")
    private String naver_client;

    @Value("${naver.secret}")
    private String naver_secret;



    // 네이버 로그인 state 난수 생성
    public String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }



    // 로그인 분기
    @RequestMapping("/login")
    public RedirectView MainLogin(@RequestParam("login_with") String login_with){
        RedirectView rv = new RedirectView();

        switch (login_with){
            case "kakao":
                rv.setUrl("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+ kakao_rest +"&redirect_uri=http://localhost:8080/auth/kakao/callback");
                break;
            case "google":
                System.out.println("구글 미구현");
                break;
            case "naver":
                String state = generateState();
                rv.setUrl("https://nid.naver.com/oauth2.0/authorize?client_id="+ naver_client +"&response_type=code&redirect_uri=http://localhost:8080/auth/naver/callback&state="+ state);
                break;
            case "facebook":
                break;
        }

        return rv;
    }




    // 수정일 : 2021-12-19
    // 작성자 : 김범철
    // 네이버 로그인
    @RequestMapping(value = "auth/naver/callback")
    public ModelAndView NaverLogin(@RequestParam("code") String code, @RequestParam("state") String state){

        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("client_id", naver_client);
        params.add("client_secret", naver_secret);
        params.add("grant_type", "authorization_code");
        params.add("state", state);  // state 일치를 확인
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,httpHeaders);

        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // 토큰값 Json 형식으로 가져오기위해 생성
        JSONObject jo = new JSONObject(response.getBody());

        // 토큰결과값
        System.out.println("네이버 토큰 결과값 : " + response);

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Authorization", "Bearer "+ jo.get("access_token"));
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String >> kakaoProfileRequest2= new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        // 토큰을 사용하여 사용자 정보 추출
        JSONObject jo2 = new JSONObject(response2.getBody());
        System.out.println("네이버 사용자 정보 추출 : " + jo2);
        ModelAndView mav = new ModelAndView();


        // 디비 존재여부
        try{
            // 회원인지 아닌지 체크
            com.commerce.entity.user member = userRepository.findBySocial(String.valueOf(jo2.getJSONObject("response").get("id")))
                    .orElseThrow(() -> new IllegalArgumentException("미가입자"));

            String token = jwtTokenProvider.createToken(member.getSocial() ,member.getName(), member.getAge(),member.getEmail(),member.getImage(), member.getConnected(),member.getRegdate(), member.getRoles());
            mav.addObject("token", token);
            mav.setViewName("cb");

        }catch(Exception e){

            // 회원가입 페이지로 이동
            System.out.println("회원가입하러 이동" + e);
            mav.addObject("social_id", jo2.getJSONObject("response").get("id"));
            mav.addObject("connected", "naver");
            mav.addObject("image", jo2.getJSONObject("response").get("profile_image"));
            mav.addObject("name", jo2.getJSONObject("response").get("name"));
            mav.addObject("email", jo2.getJSONObject("response").get("email"));
            mav.setViewName("join");
        }

        return mav;
    }


    // 수정일 : 2021-12-19
    // 작성자 : 김범철
    // 카카오 로그인
    @RequestMapping(value = "auth/kakao/callback")
    public ModelAndView KakaoLogin(@RequestParam("code") String code, RedirectAttributes redirectAttributes){


        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id", kakao_rest);
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
        System.out.println("카카오 토큰 결과값 : " + response);

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
        System.out.println("카카오 사용자 정보 추출 : " + jo2);

        ModelAndView mav = new ModelAndView();


        // 디비 존재여부
        try{
            // 회원인지 아닌지 체크
            com.commerce.entity.user member = userRepository.findBySocial(String.valueOf(jo2.get("id")))
                    .orElseThrow(() -> new IllegalArgumentException("미가입자"));

            String token = jwtTokenProvider.createToken(member.getSocial() ,member.getName(), member.getAge(),member.getEmail(),member.getImage(), member.getConnected(),member.getRegdate(), member.getRoles());
            mav.addObject("token", token);
            mav.setViewName("cb");

        }catch(Exception e){

            // 회원가입 페이지로 이동
            System.out.println("회원가입하러 이동" + e);
            mav.addObject("social_id", jo2.get("id"));
            mav.addObject("connected", "kakao");
            mav.addObject("image", jo2.getJSONObject("properties").get("profile_image"));
            mav.addObject("name", jo2.getJSONObject("properties").get("nickname"));
            mav.addObject("email",jo2.getJSONObject("kakao_account").get("email"));
            mav.setViewName("join");
        }

        return mav;
    }


    @RequestMapping("register")
    public ModelAndView Register(HttpServletRequest req){

        ModelAndView mav =new ModelAndView();
        try{

            userRepository.save(user.builder()
                    .social(req.getParameter("social_id"))
                    .email(req.getParameter("email"))
                    .name(req.getParameter("name"))
                    .image(req.getParameter("image"))
                    .age(req.getParameter("age"))
                    .connected(req.getParameter("connected"))
                    .regdate(req.getParameter("regdate"))
                    .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                    .build()).getId();

            com.commerce.entity.user member = userRepository.findAllBySocial(req.getParameter("social_id"))
                    .orElseThrow(() -> new IllegalArgumentException("회원가입 비정상 처리"));

            String token = jwtTokenProvider.createToken(member.getSocial() ,member.getName(), member.getAge(),member.getEmail(),member.getImage(), member.getConnected(), member.getRegdate(), member.getRoles());

            mav.addObject("token", token);
            mav.setViewName("cb");

        }catch(Exception e){
            System.out.println("회원가입 실패" + e);
            // mav 404
        }

        return mav;
    }



    // 유저정보
    @PostMapping("/user/detail")
    public Claims detail(HttpServletRequest req) {
        System.out.println(jwtTokenProvider.getAllClaims(jwtTokenProvider.resolveToken(req)));
        return jwtTokenProvider.getAllClaims(jwtTokenProvider.resolveToken(req));
    }
}


