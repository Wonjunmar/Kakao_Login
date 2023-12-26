package com.example.demo.member.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberLoginRes {
    Integer id;
    String email;
}
