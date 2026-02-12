# 🌌 Universe — K-pop Live Commerce Platform

> **K-pop 아티스트의 라이브 방송과 굿즈 판매를 동시에 진행할 수 있는 실시간 커머스 플랫폼**

---

## 📘 프로젝트 개요

**Universe**는 K-pop 팬과 아티스트를 실시간으로 연결하는 **라이브 커머스 서비스**입니다.  
아티스트는 실시간 방송 중 상품을 등록·판매할 수 있고,  
사용자는 채팅을 통해 소통하며 굿즈를 구매할 수 있습니다.  

> 🎯 **목표:** 실시간성과 안정성을 모두 갖춘 “라이브 + 커머스 통합 플랫폼” 구축

---

## ⚙️ 기술 스택

### 🧩 Backend
![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=flat-square&logo=jsonwebtokens&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=flat-square&logo=hibernate&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)

### 🎨 Frontend
![React](https://img.shields.io/badge/React-20232A?style=flat-square&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=vite&logoColor=white)

### ☁️ Infra & DevOps
![Nginx](https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white)
![Naver Cloud](https://img.shields.io/badge/Naver_Cloud-03C75A?style=flat-square&logo=naver&logoColor=white)

### 🤖 External API
![Papago](https://img.shields.io/badge/Papago_Translation-1EC800?style=flat-square&logo=naver&logoColor=white)
![Clova](https://img.shields.io/badge/Clova_Speech_Recognition-00C73C?style=flat-square&logo=naver&logoColor=white)

---

## 🧩 아키텍처 개요
Universe-Gateway (Spring Cloud Gateway)
Universe-Eureka (Service Registry)
Universe-Core (회원 / 상품 / 주문 / 결제)
Universe-Chat (실시간 채팅, Redis Pub/Sub)
Universe-Live (WebRTC / Node.js / Mediasoup)
Universe-React (User / Partner Front)

- **Gateway & Eureka** : 서비스 트래픽 관리 및 디스커버리  
- **Core Service** : 회원, 상품, 주문, 결제 로직  
- **Chat Service** : Redis Pub/Sub 기반 STOMP 브로커 + 욕설 필터링 (Aho-Corasick)  
- **Live Service** : WebRTC 방송 송출 및 녹화  
- **Front (React)** : 사용자·파트너 포털 분리  

---

## 💬 주요 기능

| 기능 | 설명 |
|------|------|
| 🎥 **실시간 라이브 방송** | Mediasoup(WebRTC) 기반 실시간 스트리밍 |
| 💬 **실시간 채팅** | WebSocket + STOMP, Redis Pub/Sub 구조 |
| 🧱 **욕설/도배 필터링** | Aho-Corasick + Sliding Window Rate Limiting |
| 🔐 **JWT 인증 구조** | Access/Refresh Token 기반 로그인 |
| 🛍️ **회원, 주문, 아티스트, 상품 관리** | 등록, 수정, 삭제, 조회 (JPA Fetch Join 최적화) |
| 💳 **주문/결제** | 비동기 결제 모듈 설계 |
| 🗂️ **파일 업로드 관리** | Naver Cloud Object Storage + UUID 기반 구조 |
| 🚀 **배포 자동화** | Nginx Reverse Proxy + SSL 인증 |

---

## ⚡ 트러블슈팅 & 성과

- **문제:** 스트리밍 녹화 파일이 Object Storage에 저장되지 않음  
  → **해결:** FFmpeg 파이프라인 경로 수정 및 권한 재설정  
- **문제:** JPA 조회 성능 저하 (N+1 Issue)  
  → **해결:** Fetch Join + DTO Projection 구조로 전환
- **문제:** Gateway 통신 시 인증 토큰이 여러 서비스 간 전달되지 않음  
  → **해결:** Filter와 GlobalInterceptor를 활용한 **JWT 전파 로직**을 추가하고,  
  Eureka 등록 시 서비스 ID 기반 라우팅을 명확히 분리하여  
  MSA 환경에서의 인증 일관성을 확보했습니다.

> 구조적으로 **확장 가능한 MSA 운영 환경**을 완성했습니다.
---

## 🚀 배포 환경

| 항목 | 구성 |
|------|------|
| 서버 | Naver Cloud Ubuntu 22.04 |
| Reverse Proxy | Nginx (SSL 적용) |
| 백엔드 | Spring Boot (Gateway 적용)|
| 프론트엔드 | React (Port 4444, 5555) |
| 실시간 서버 | Node.js + Mediasoup (Port 4000) |
| DB | MySQL 8.0 / Redis |
| 배포 방식 | GitHub Actions (CI/CD) |

---

## 🧠 배운 점

- MSA 설계 및 Gateway 기반 트래픽 관리의 구조적 이해  
- Naver Cloud + Nginx를 통한 안정적 서비스 배포 환경 구축
- WBS 활용 및 코드 리뷰 협업을 통한 예측 가능한 개발

---

## 📚 Repository Links

| Repo | Description |
|------|-------------|
| [Universe-Gateway](https://github.com/Sinsegae-FeelSangITMal/Universe-Gateway) | API Gateway (Spring Cloud) |
| [Universe-Eureka](https://github.com/Sinsegae-FeelSangITMal/Universe-Eureka) | Eureka Server |
| [Universe](https://github.com/Sinsegae-FeelSangITMal/Universe) | 메인 서비스 (회원, 상품, 주문, 결제) |
| [Universe-Chat](https://github.com/Sinsegae-FeelSangITMal/Universe-Chat) | Redis 기반 실시간 채팅 |
| [Universe-Live](https://github.com/Sinsegae-FeelSangITMal/Universe-Live) | WebRTC 방송 송출 서버 |
| [Universe-React-User](https://github.com/Sinsegae-FeelSangITMal/Universe-React-User) | 사용자 웹 |
| [Universe-React-Admin](https://github.com/Sinsegae-FeelSangITMal/Universe-React-Admin) | 판매자 웹 |

---

## 🪄 시연 영상 & 자료

🎥 [시연 영상 보기](https://drive.google.com/file/d/1SYu84j_FMIriWkIKdmgym0e0DD_X1Xzk/view)  
📄 [발표 자료 (PPT)](https://docs.google.com/presentation/d/1GvcczQARDtKKJ9SUf_hcCfi8P8SOUuuR/edit?slide=id.g38ca26eb2e9_0_2#slide=id.g38ca26eb2e9_0_2)

---

> 💜 Universe: Designed, Developed, and Deployed by Team Universe (2025)
>  
> “실시간, 안정성, 확장성 — 모두를 잡은 K-pop 라이브 커머스 플랫폼”


