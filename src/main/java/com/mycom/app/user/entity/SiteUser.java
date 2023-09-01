package com.mycom.app.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

//(가입전)사용자
@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//회원id.pk.자동증가

    @Column(unique = true)
    private String username;//회원이름.uk

    @NotNull
    private String password;//비밀번호

    @Column(unique = true)
    private String email;//이메일.uk
}