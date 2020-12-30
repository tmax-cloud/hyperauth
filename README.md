# HyperAuth
- **설치 가이드**

  - https://github.com/tmax-cloud/install-hyperauth
  - **EmailOTP 2-factor 인증 기능 사용 가이드**
    - Authentication - Bindings - Brower Flow : **Browser with EmailOTP** 선택
    - OTP 인증을 추가 하고 싶은 user에게 **otpEnable : true** Attribute을 추가
    - id /pw 입력하고 난 후, 메일전송이 완료되면, otp code를 입력하는 화면으로 전환
  - `SecretQuestion 2-factor 인증 기능 사용 가이드 `(Alpha)
    - Authentication - Bindings - Brower Flow : **Browser with secret question** 선택
    - Authentication - Required Actions : **Secret Question Enabled**, **Default Action**으로 선택하여 질문의 답을 추가하게 강제
  - **IP 차단 기능 사용 가이드**
    - Authentication - Bindings - Brower Flow : **Browser with security policy** 선택
    - IP Block 정책을 걸고자 하는 user에게 Attribute을 추가
      1. **ipBlock** : true
      2. **ipPermitList** : 192.168.6.196/16##172.22.6.2/24##172.21.6.3/24 

- **이미지 정보**

  ```sh
  docker pull tmaxcloudck/hyperauth:latest
  ```

  - Base Image
    - Keycloak 10.0.2  :  b1.0.0.0 ~ b1.0.9.29
    - Keycloak 11.0.2  :  b1.0.10.0 ~ latest
  - TmaxRealm.json
    - https://github.com/tmax-cloud/install-hyperauth/blob/main/manifest/3.tmax-realm-export.json
    
- **정책**
  - User Attribute Key-Value 값 (공통 및 고정)
    - USER
      - 사용자의 username은 attribute으로 관리
        - user_name : 한글/영어
        - user_name_ko : 한글
        - user_name_en : 영어
    - AGREEMENT
      - Hyperauth의 선택 약관
        - agreeMailOpt : true / false
      - Client별 최초 로그인시 약관
        - agree_ischecked_{ClientName} : true 
          - ex) agree_ischecked_portal : true / agree_ischecked_hypermeeting : true
  
- **API 정보**

  - [API Documentation](/APIDOC.md)

# Feature

- `Login/Logout Event 발생 시, Auditing 기능` (Alpha)
- **회원가입/탈퇴 시, HyperCloud4 Role 생성/삭제 기능**
  - NSC, RBC 등 요청 리소스 생성 권한 부여
- **중복 Login 방지 기능**
  - 한 유저의 Client당 로그인 session을 하나만 유지
- **회원 가입 시 Email 인증 기능**
  - 가입 5분 후, 이메일 인증을 수행하지 않은 사용자 정보 삭제 

- **Client 별 약관 CRUD 기능**

- `네이버로 로그인, 카카오로 로그인` (Alpha)
- `회원가입 시, 핸드폰 번호 인증 기능` (Alpha)
- **회원 탈퇴 신청 기능**
  - 탈퇴 신청 후, 사용자 데이터 30일 유지
  - 탈퇴 철회는 관리자 문의를 통해 지원
- **2-factor 인증 기능**
  - **EmailOTP 기능**
    - 유효시간, 인증숫자의 개수는 설정으로 변경 가능 (default: 10min, 6자리)
  - `Secret Question 기능` (Alpha)
    - 질문에 대한 답을 입력하는 추가 인증 기능
    - 현재 질문이 하나로 고정되어 있음
    - What is your mom's first name?
- **IP 차단 기능**
  - 사용자 별 IP 차단 기능 제공
    - WhiteList 기반 관리
    - ipBlock attribute 가 true 일때, ipPermitList의 **IP(CIDR)에 대한 접근 허용**
    - 복수개의 CIDR를 등록시 ##으로 구분해서 추가
- **그룹 관리자 관련 기능**
  - 그룹 하위 사용자들에 대한 CRU 기능
  - 기 사용자를 그룹에 초대하는 기능
  - 그룹에 대한 생성, 그룹 관리자로 승격은 HyperAuth 관리자가 직접 제어
- **사용자 비밀번호 관련 기능**
  - 사용자가 자신의 비밀번호 변경 기능 (자신의 Token 필요)
  - 비밀번호 찾기 기능 (인증 코드를 Email으로 발송)
- **BCrypt Password Hash 알고리즘 추가**
  - keycloak이 기본적으로 제공하는 pbkdf2-sha256 알고리즘 외에 BCrypt 알고리즘을 이용해 password를 hashing 할 수 있게 지원
  - Authentication > Password Policy : Add Policy > **Hashing Algorithm : bcrypt** 입력
  - 참고 : pbkdf2-sha256 혹은 Bcrypt hash 알고리즘을 사용한 외부 인증 서버로 부터는 비밀번호 migration을 지원가능

  
