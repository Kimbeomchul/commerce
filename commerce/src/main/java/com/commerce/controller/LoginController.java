package com.commerce.controller;

import com.commerce.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

//    // 회원가입
//    @PostMapping("/join")
//    public Long join(@RequestBody Map<String, String> user) {
//        return userRepository.save(com.kakao.entity.user.builder()
//                .(user.get("email"))
//                .password(passwordEncoder.encode(user.get("password")))
//                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
//                .build()).getId();
//    }
//
//    // 로그인
//    @PostMapping("/login")
//    public String login(@RequestBody Map<String, String> user) {
//        com.kakao.entity.user member = userRepository.findById(user.get("email"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
//    }



    // 유저정보
    @PostMapping("/user/detail")
    public String detail(HttpServletRequest req) {
        System.out.println("이름 : " + jwtTokenProvider.getAllClaims(jwtTokenProvider.resolveToken(req)).get("name"));
        System.out.println("이미지 : " + jwtTokenProvider.getAllClaims(jwtTokenProvider.resolveToken(req)).get("image"));
        System.out.println("카카오아이디 : " + jwtTokenProvider.getUserPk(jwtTokenProvider.resolveToken(req)));
        return "";
    }
}

