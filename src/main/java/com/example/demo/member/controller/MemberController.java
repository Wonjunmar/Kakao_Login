package com.example.demo.member.controller;

import com.example.demo.member.model.dto.MemberLoginReq;
import com.example.demo.member.model.dto.MemberSignupReq;
import com.example.demo.member.service.KakaoService;
import com.example.demo.member.service.MemberService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.member.model.Member;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;
    private KakaoService kakaoService;
    private AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, KakaoService kakaoService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity login(@RequestBody MemberLoginReq memberLoginReq) {

        return ResponseEntity.ok().body(memberService.login(memberLoginReq));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kakao")
                            // 인가 코드 받아오는 코드
    public ResponseEntity kakao(String code) {
        System.out.println(code);
        /////////////////////////////////
        // 인가 코드로 토큰 받아오는 코드
        String accessToken = kakaoService.getKakaoToken(code);
        ///////////////////////////////////////////////////////////////
        // 토큰으로 사용자 정보 받아오는 코드
        String userName = kakaoService.getUserInfo(accessToken);
        //////////////////////////////////////////////
        // 가져온 사용자 정보로 DB 확인
        Member member = memberService.getMemberByEmail(userName);
        if(member == null) {
            // DB에 없으면 회원 가입
            memberService.kakaoSignup(userName);
        }
        // 로그인 처리(JWT 토큰 발급)
        return ResponseEntity.ok().body(memberService.kakaoLogin(userName));
    }
}
