# HyperAuthServer changelog!!
All notable changes to this project will be documented in this file.

<!-------------------- v1.1.3.0 start -------------------->

## HyperAuthServer 1.1.3.0 (2024. 08. 28. (수) 16:15:37 KST)

### Added

### Changed
  - [mod] [ims-330822] set option for immediate user deletion by 2smin

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.3.0 end --------------------->

<!-------------------- v1.1.2.13 start -------------------->

## HyperAuthServer 1.1.2.13 (2024. 07. 29. (월) 11:41:33 KST)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.13 end --------------------->

<!-------------------- v1.1.2.12 start -------------------->

## HyperAuthServer 1.1.2.12 (2024. 07. 19. (금) 09:38:43 KST)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.12 end --------------------->

<!-------------------- v1.1.2.11 start -------------------->

## HyperAuthServer 1.1.2.11 (2024. 07. 17. (수) 11:14:49 KST)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.11 end --------------------->

<!-------------------- v1.1.2.9 start -------------------->

## HyperAuthServer 1.1.2.9 (2024. 01. 23. (화) 14:03:50 KST)

### Added

### Changed
  - [mod] social login-emailAsUsername 비활성화시 가입절차 반복 오류 수정 / login 설정 에따라 login-update-profile 변경 by 2smin
  - [mod] initech provider에서 userinfo 전송하도록 수정 by 2smin
  - [mod] dockerhub -> hyperregistry 이미지 저장소 변경 by 2smin

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.9 end --------------------->

<!-------------------- v1.1.2.8 start -------------------->

## HyperAuthServer 1.1.2.8 (2023. 12. 08. (금) 11:11:54 KST)

### Added
- [feat] 신한라이프 Initech 외부 연동을 위한 예제 Initech 소셜로그인 Provider 추가 by taegeon_woo

### Changed
- [mod] oidc-provider로부터 userEmail을 받아오도록 수정 by 2smin

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.8 end --------------------->

<!-------------------- v1.1.2.7 start -------------------->

## HyperAuthServer 1.1.2.7 (2023. 10. 31. (화) 15:23:15 KST)

### Added
  - [feat] fido 가이드 업데이트 by taegeon_woo
  - [feat] External Url 입력을 통해 토큰에 Claim을 추가 할수 있는 Client Mapper 기능 및 가이드(readme.md) 추가 by taegeon_woo

### Changed
  - [mod] jenkinsfile maven 빌드시에 java 버전을 1.8 사용하도록 maven 설정 추가 by taegeon_woo
  - [mod] readme.md 리뉴얼 및 이미지 보강 작업 수행 by taegeon_woo
  - [mod] .gitignore 수정 ide 관련 파일 tracking 안되도록 수정 by taegeon_woo

### Fixed

### CRD yaml

### Etc
  - [etc] dockerfile layer 간소화 by 2smin

<!--------------------- v1.1.2.7 end --------------------->

<!-------------------- v1.1.2.6 start -------------------->

## HyperAuthServer 1.1.2.6 (2023. 01. 27. (금) 11:52:27 KST)

### Added

### Changed

### Fixed
  - [ims][297420] hyperauth 중복로그인 방지 기능 optional 하게 제공 by taegeon_woo

### CRD yaml

### Etc

<!--------------------- v1.1.2.6 end --------------------->

<!-------------------- v1.1.2.5 start -------------------->

## HyperAuthServer 1.1.2.5 (2022. 12. 01. (목) 16:37:17 KST)

### Added

### Changed

### Fixed
  - [ims][289418] 회원가입 (일반, 소셜로그인) 인증 메일을 통한 인증완료 시에 로그인 초기 화면으로 돌아가지 못하는 현상 해결 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.2.5 end --------------------->

<!-------------------- v1.1.2.4 start -------------------->

## HyperAuthServer 1.1.2.4 (2022. 09. 25. (일) 13:35:53 KST)

### Added

### Changed
  - [mod] Redirect URI 와일드 카드 중간 혹은 앞에도 들어가서 subdomain 고려 가능하게 끔 수정 by dnxorjs1
  - [mod] keycloak의 expire password 기능을 이용해서 비밀번호 변경을 유도할 경우, 다음에 변경하기가 동작하지 않는 현상 해결 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.4 end --------------------->

<!-------------------- v1.1.2.3 start -------------------->

## HyperAuthServer 1.1.2.3 (2022. 09. 13. (화) 17:00:25 KST)

### Added

### Changed
  - [mod] cnu 로그인 로고 이미지 변경 및 기존 이미지 삭제 by bin_lim
### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.3 end --------------------->

<!-------------------- v1.1.2.2 start -------------------->

## HyperAuthServer 1.1.2.2 (2022. 08. 19. (금) 19:19:09 KST)

### Added
  - [feat] 계정관리 메뉴 모바일 css 고려 작업 수행 by dnxorjs1

### Changed
  - [mod] test 배포 imageRegistry 변수화 by dnxorjs1
  - [mod] 탈퇴시 카카오로 로그인 모바일 디자인 적용 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.2 end --------------------->

<!-------------------- v1.1.2.1 start -------------------->

## HyperAuthServer 1.1.2.1 (2022. 08. 05. (금) 17:45:19 KST)

### Added

### Changed
  - [mod] 로그인 관련 화면에서 로그인 초기 화면으로 가야 하는 기능에서 client의 baseUrl이 등록되어 있지 않아도 갈수 있도록 restartLoginUrl로 가도록 로직 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.1 end --------------------->

<!-------------------- v1.1.2.0 start -------------------->

## HyperAuthServer 1.1.2.0 (2022. 07. 21. (목) 11:13:35 KST)

### Added

### Changed
  - [mod] hyperauth 로그 시스템 (keycloak, hyperauth, kafka, jboss ..) 전부 LOG_LEVEL 환경변수로 조절가능하게끔 초기 jboss-cli 명령 추가 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.2.0 end --------------------->

<!-------------------- v1.1.1.45 start -------------------->

## HyperAuthServer 1.1.1.45 (2022. 06. 13. (월) 17:18:03 KST)

### Added

### Changed
  - [mod] realm Export API에서 User Credential 정보 Export 여부 결정가능하도록 기능 및 버튼 추가 by dnxorjs1
  - [mod] userToken 관련 API Header bearer 토큰을 통해서도 받을 수 있도록 로직 추가 by dnxorjs1
  - [mod] admin console에서 export시 user를 export 할수 있도록 추가 구현 by dnxorjs1
  - [mod] Outer2ndFactor 인증 수단 고도화 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.45 end --------------------->

<!-------------------- v1.1.1.44 start -------------------->

## HyperAuthServer 1.1.1.44 (2022. 04. 07. (목) 15:29:27 KST)

### Added

### Changed
  - [mod] 코드에 realm.getDisplayName 로직 getName으로 일괄 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.44 end --------------------->

<!-------------------- v1.1.1.43 start -------------------->

## HyperAuthServer 1.1.1.43 (2022. 04. 07. (목) 15:12:22 KST)

### Added

### Changed
  - [mod] userExists API 에서 realm 이름을 displayname으로 가져오던걸 name으로 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.43 end --------------------->

<!-------------------- v1.1.1.42 start -------------------->

## HyperAuthServer 1.1.1.42 (2022. 03. 30. (수) 11:37:33 KST)

### Added

### Changed

### Fixed
  - [ims][279881] 개인정보처리방침 변경으로 인한 내용 변경 반영 요청 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.1.42 end --------------------->

<!-------------------- v1.1.1.41 start -------------------->

## HyperAuthServer 1.1.1.41 (2022. 01. 24. (월) 21:58:39 KST)

### Added
  - [feat] tmax-realm 최신 형상반영 by dnxorjs1

### Changed
  - [mod] client secret tmax_client_secret 으로 변경 by dnxorjs1

### Fixed
  - [ims][274662] 최초로그인시 출력되는 비밀번호 변경페이지 다음에 변경하기 기능 추가 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.1.41 end --------------------->

<!-------------------- v1.1.1.40 start -------------------->

## HyperAuthServer 1.1.1.40 (2021. 12. 28. (화) 11:52:27 KST)

### Added

### Changed
  - [mod] 비밀번호 변경 required action 화면에서 이전비밀번호와 동일 에러가 한번 뜨면 사라지지 않는 현상 해결 by dnxorjs1
  - [mod] 3개월 비밀번호 변경 페이지 저장 버튼 비활성화 안되는 현상 해결 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.40 end --------------------->

<!-------------------- v1.1.1.39 start -------------------->

## HyperAuthServer 1.1.1.39 (2021. 12. 27. (월) 17:37:44 KST)

### Added

### Changed
  - [mod] login-update-password.ftl 저장버튼 비활성화 안되는 현상 해결 by dnxorjs1
  - [mod] joinGroup.sh Contain 로직 equal으로 수정 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.39 end --------------------->

<!-------------------- v1.1.1.38 start -------------------->

## HyperAuthServer 1.1.1.38 (2021. 12. 21. (화) 14:01:42 KST)

### Added
  - [feat] supercloud 테마 추가, 관련 빌드 및 배포 시스템 추가 구현 by dnxorjs1

### Changed
  - [mod] wapl cnu 로그인 화면 모바일에서 잘리던 현상 수정 by bin_lim
  - [mod] strimzi kafka에 맞추어서 kafka producer 주소 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.38 end --------------------->

<!-------------------- v1.1.1.37 start -------------------->

## HyperAuthServer 1.1.1.37 (2021. 12. 07. (화) 14:38:33 KST)

### Added

### Changed
  - [mod] gatekeeper 가이드 및 예시 추가 by dnxorjs1
  - [mod] gatekeeper 가이드 audience mapper 설정 추가 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.37 end --------------------->

<!-------------------- v1.1.1.36 start -------------------->

## HyperAuthServer 1.1.1.36 (2021. 11. 30. (화) 17:33:21 KST)

### Added
  - [feat] keycloak-gatekeeper.pptx 추가 by dnxorjs1

### Changed
  - [mod] wapl 로그인 화면 모바일에서 잘리던 현상 수정 by bin_lim
  - [mod] 비밀번호 변경 required action이 있을 경우 3개월 비밀번호 변경 페이지 노출되지 않도록 조치 by dnxorjs1
  - [mod] 비밀번호 변경 관련 페이지 기획 및 디자인 통일 -김경민- by dnxorjs1
  - [mod] baseUrl이 없는 client의 wapl 테마 로그인 화면에서 npe 나는 현상 수정 by dnxorjs1
  - [ims] [274662 UPDATE_PASSWORD required action 관련 페이지 구현 [mod] 비밀번호 변경 관련 페이지 저장버튼 활성화 관련 로직 고도화 [mod] typo 수정 [mod] ux 패턴 고도화 by dnxorjs1
  - [mod] keycloak-service jar 수정 : updatePassword에 기존비밀번호와 같을 경우 에러 발생하도록 로직 추가 by dnxorjs1
  - [mod] 비밀번호 변경 로직에서 UPDATE_PASSWORD required action이 존재할 경우 지워주는 로직 추가 by dnxorjs1
  - [mod] keycloak-gatekeeper 가이드 Yaml 추가 by dnxorjs1
  - [mod] joinGroup shell script 고도화 : http--> https, insecure 옵션 추가, userlistget 10000명으로 변경 by dnxorjs1
  - [mod] 기본 테마 캐시 정책 1달 --> 1주일 로 변경, 모든 유저를 특정 userGroup에 속하게 하는 script추가 by dnxorjs1
  - [mod] js, css 버전관리 배포 시스템 버그 수정 by dnxorjs1
  - [mod] 유저 삭제 api에서 hypercloud4 userrole 삭제 api 재추가 (정책) by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.36 end --------------------->

<!-------------------- v1.1.1.35 start -------------------->

## HyperAuthServer 1.1.1.35 (2021. 11. 09. (화) 00:11:15 KST)

### Added

### Changed
  - [mod] tmax 관련 theme 전체 캐싱 방지 js, css 버전 쿼리파라미터 추가 by dnxorjs1
  - [mod] 회원가입 후 10분후 메일인증 확인하는 Timer 코드버그 수정 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.35 end --------------------->

<!-------------------- v1.1.1.34 start -------------------->

## HyperAuthServer 1.1.1.34 (2021. 11. 04. (목) 17:26:35 KST)

### Added

### Changed
  - [mod] cert-manager 사용 인증서 생성 방식으로 바뀌면서 jks(keystore, truststore) 파일 이름 변경, 이전 인증서 사용불가 주의 요망 !! by dnxorjs1

### Fixed
  - [ims][273313] direct grant 방식 로그인시에 temp lock 초기화 안되는 현상 추가 개발 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.1.34 end --------------------->

<!-------------------- v1.1.1.33 start -------------------->

## HyperAuthServer 1.1.1.33 (2021. 10. 05. (화) 11:37:08 KST)

### Added

### Changed
  - [mod]유저 프로필 이미지 관련 UI 로직 수정 by min9120
  - [mod] 사진 업로드 제한 문구 추가 by dnxorjs1
  - [mod] account 프로필 이미지 파일 사이즈 조건 메시지 추가 by min9120
  - [mod] 사진 upload시 closedChanndelException 나는 현상 해결: jboss undertow http2 enable설정 false로 변경 하는 cli 스크립트 추가 by dnxorjs1
  - [mod] 테마 리뉴얼 적용 머지 by dnxorjs1
  - [mod] custom 메일에도 보내는 이름 cutom하게 설정 할수 있도록 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.33 end --------------------->

<!-------------------- v1.1.1.32 start -------------------->

## HyperAuthServer 1.1.1.32 (2021. 09. 14. (화) 15:56:12 KST)

### Added

### Changed
  - [mod] userDelete API 버그 수정 및 hypercloud4 call 삭제 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.32 end --------------------->

<!-------------------- v1.1.1.31 start -------------------->

## HyperAuthServer 1.1.1.31 (2021. 09. 06. (월) 19:11:03 KST)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.31 end --------------------->

<!-------------------- v1.1.1.30 start -------------------->

## HyperAuthServer 1.1.1.30 (2021. 09. 06. (월) 17:30:10 KST)

### Added

### Changed
  - [mod] 신한은행 goldwing 2차인증 대응용 Outer2ndFactor Authenticator 구현 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.30 end --------------------->

<!-------------------- v1.1.1.29 start -------------------->

## HyperAuthServer 1.1.1.29 (2021. 09. 02. (목) 15:58:53 KST)

### Added

### Changed

### Fixed
  - [ims][269772] 비밀번호 변경시 비밀번호 확인과 다름에도 완료 페이지 노출되는 현상 해결 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.1.29 end --------------------->

<!-------------------- v1.1.1.28 start -------------------->

## HyperAuthServer 1.1.1.28 (2021. 09. 02. (목) 13:15:35 KST)

### Added
  - [feat] supervds private 로그인 테마 추가 by dnxorjs1

### Changed
  - [mod] 테마 superVDS private 고려 코드 추가 by dnxorjs1
  - [mod] hyperauth legacy tar 생성 로직 추가 by dnxorjs1
  - [mod] 프로필 사진 gif 형식 지원가능하게 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.28 end --------------------->

<!-------------------- v1.1.1.27 start -------------------->

## HyperAuthServer 1.1.1.27 (2021. 08. 26. (목) 15:38:45 KST)

### Added

### Changed
  - [mod] 메일 html 상대경로 --> jboss_home을 이용한 경로로 수정 by dnxorjs1
  - [mod] profile-picture imageUrl 서버 Url에 따라서 변수화 할수 있도록 변경 by dnxorjs1

### Fixed

### CRD yaml

### Etc
  - [etc] commit_rule.md  commit 형식 가이드 추가 by dnxorjs1

<!--------------------- v1.1.1.27 end --------------------->

<!-------------------- v1.1.1.26 start -------------------->
## HyperAuthServer 1.1.1.26 (2021년 08월 25일 수 오후  3:20:31)

### Added
  - [feat] tibero 포함된 install guide 및 참고 자료 추가 by dnxorjs1

### Changed
  - [mod] legacy 환경에서 메일 전송시 html 문서를 찾지 못해서 텅 비어 오던 현상 해결. html 경로 절대경로에서 상대경로로 변경 [mod] 유저 프로필 사진 관리 API Picture Provider 추가 (admin console, account console) by dnxorjs1
  - [mod] 환경변수 처리 로직 default값 처리 추가 리팩토링 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.26 end --------------------->

<!-------------------- v1.1.1.25 start -------------------->
## HyperAuthServer 1.1.1.25 (2021년 08월 25일 수 오후  3:20:27)

### Added

### Changed
  - [mod] 배포시스템 통합 by dnxorjs1

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.25 end --------------------->

<!-------------------- v1.1.1.24 start -------------------->
## HyperAuthServer 1.1.1.24 (2021년 08월 25일 수 오후  3:20:03)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.24 end --------------------->

<!-------------------- v1.1.1.23 start -------------------->
## HyperAuthServer 1.1.1.23 (2021년 08월 25일 수 오후  3:18:48)

### Added

### Changed

### Fixed
  - [ims][268265] CNU테마 비밀번호 찾기 문구 수정 by dnxorjs1

### CRD yaml

### Etc

<!--------------------- v1.1.1.23 end --------------------->

<!-------------------- v1.1.1.22 start -------------------->
## HyperAuthServer 1.1.1.22 (2021년 08월 25일 수 오후  3:18:23)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.22 end --------------------->

<!-------------------- v1.1.1.21 start -------------------->
## HyperAuthServer 1.1.1.21 (2021년 08월 25일 수 오후  3:17:32)

### Added

### Changed

### Fixed

### CRD yaml

### Etc

<!--------------------- v1.1.1.21 end --------------------->
