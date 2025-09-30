# Gemini Project Guide

## 프로젝트 목적
이 프로젝트는 Spring Boot + React 기반으로 운영되는 아이돌 굿즈 커머스 플랫폼입니다.

## 기술 스택
- Backend: Spring Boot 3.x, JPA, MySQL, Spring Security, Oauth2-Client
- Frontend: React (Vite)
- Infra: Redis

## 개발 규칙
- 모든 API 응답은 ApiResponse<T> 포맷을 따른다.
- JWT 인증/인가 처리
- Git Flow 전략: feature → dev → main

## TODO
- [ ] Oauth2 회원가입을 유저가 동의를 하면 유저의 추가적인 정보(nickname)을 입력받는 과정이 필요함
