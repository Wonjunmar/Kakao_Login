package com.example.demo.member.service;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.dto.MemberLoginReq;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private int expiredTimeMs;

    public Member getMemberByEmail(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public void kakaoSignup(String userName) {
        memberRepository.save(Member.builder()
                .email(userName)
                .password(passwordEncoder.encode("kakao"))
                .authority("ROLE_USER")
                .build());
    }

    public String kakaoLogin(String userName) {
        return JwtUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
    }

    public String login(MemberLoginReq memberLoginReq) {
        Member member = memberRepository.findByEmail(memberLoginReq.getEmail()).get();

        if (passwordEncoder.matches(memberLoginReq.getPassword(), member.getPassword())) {
            return JwtUtils.generateAccessToken(member.getEmail(), secretKey, expiredTimeMs);
        }

        return null;
    }
}
