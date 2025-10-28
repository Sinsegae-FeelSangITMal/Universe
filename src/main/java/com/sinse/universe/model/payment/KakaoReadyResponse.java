package com.sinse.universe.model.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KakaoReadyResponse {

    private String tid;  //결제 고유 번호, 20자
    private String next_redirect_app_url;  //요청한 client가 모바일 앱일 경우 카카오톡 결제 페이지 Redirect URL
    private String next_redirect_mobile_url;  //요청한 client가 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
    private String next_redirect_pc_url;  //요청한 client가 pc 웹일 경우 카카오톡 결제 페이지 Redirect URL
    private String android_app_scheme;
    private String ios_app_scheme;
    private String created_at;  //결제 준비 요청 시간
}
