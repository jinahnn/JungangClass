package com.mycom.app.user.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//(가입전)사용자
@Getter
@Setter
@ToString
public class SiteUserDTO {
    private Long id;//회원id.pk.자동증가
    private String username;//회원이름
    private String password;//비밀번호
    private String email;//이메일
}
