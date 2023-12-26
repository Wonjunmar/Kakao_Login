package com.example.demo.member.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoService {
    public String getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Object.class
        );

        String str = ("" + response.getBody());
        System.out.println(str);
        Gson gson = new Gson();
        System.out.println(str.split(",")[0]+","+str.split(",")[2]+"}");
        Map<String, Object> result = gson.fromJson(str.split(",")[0]+","+str.split(",")[2]+"}", Map.class);
        Map<String, Object> properties = (Map<String, Object>)result.get("properties");

        return (String)properties.get("nickname");
    }

    public String getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "a37968703680f6c8a221bf2103cc1eb5");
        params.add("redirect_uri", "http://localhost:8080/member/kakao");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                Object.class
        );

        Gson gson = new Gson();
        Map<String, Object> result = gson.fromJson("" + response.getBody(), Map.class);
        String accessToken = "" + result.get("access_token");

        return accessToken;
    }
}
