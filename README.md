# HyperAuth
- **설치 가이드**

  - https://github.com/tmax-cloud/install-hyperauth
    - kafka cluster topic server 추가 설치의 경우, Step 4. Kafka Topic Server 설치 만 추가 수행하면 됨 
    
  - **Topic Consumer가이드**
    - [TopicConsumerExample.java](src/main/java/com/tmax/hyperauth/eventlistener/consumer/EventConsumer.java)
      - TODO 부분 수행 
        - Keystore, Truststore 발급, Secret 생성 및 volume mount
        - Password Secret을 이용해서 변수 처리
        - GROUP_ID_CONFIG를 각 hyperauth client name으로 수정
          - group id 별로 topic에 쌓인 데이터를 읽어가는 위치를 나타내는 offset이 달라짐
          - 2 consumer가 한 group id를 공유할 경우, 데이터를 처리하지 못하는 경우 발생
      - Topic Event 객체 (Json)
        - ex) {"type":"LOGIN","userName":"admin@tmax.co.kr","userId":"3c2f0ab5-5c6e-4739-b9db-877ebcfbcd29","time":1610614165834,"realmId":"tmax","clientId":"hypercloud4","sessionId":"fe9b95f7-eb51-4504-9110-d2322a9f1bc1","ipAddress":"192.168.6.107","details":{"auth_method":"openid-connect","auth_type":"code","redirect_uri":"https://172.22.6.2:31304/?first","consent":"no_consent_required","code_id":"fe9b95f7-eb51-4504-9110-d2322a9f1bc1","username":"admin@tmax.co.kr"}}
        - ex) {"type":"LOGIN_ERROR","userName":"admin@tmax.co.kr","userId":"3c2f0ab5-5c6e-4739-b9db-877ebcfbcd29","time":1610614321868,"realmId":"tmax","clientId":"hypercloud4","ipAddress":"192.168.6.107","error":"invalid_user_credentials","details":{"auth_method":"openid-connect","auth_type":"code","redirect_uri":"https://172.22.6.2:31304/?first","code_id":"afa917bc-c54d-4fa0-b04e-d902baf0f3d5","username":"admin@tmax.co.kr"}}
        - [TopicEvent.java](src/main/java/com/tmax/hyperauth/eventlistener/provider/TopicEvent.java)
      - **현재 Publish 중인 Event Verb (정책)**
        - https://www.keycloak.org/docs-api/6.0/javadocs/org/keycloak/events/EventType.html 참조
        - 추가
          - USER_WITHDRAWAL : 유저가 탈퇴 신청을 한 경우
          - USER_WITHDRAWAL_CANCEL : 유저가 탈퇴신청을 철회한 경우
          - USER_DELETE : 탈퇴 신청을 한 유저가 실제로 지워진 경우, Admin Console에서 Admin 권한으로 유저가 삭제된 경우  
    - https://cwiki.apache.org/confluence/display/KAFKA/Clients 
      - java외 다른 언어의 경우 참조할 것.
      - Golang 가이드 : https://github.com/tmax-cloud/hypercloud-api-server/blob/master/util/Consumer/KafkaConsumer.go
    - 현재 Topic Event Data 보관 기간 : 7일
    
  - **비밀번호 변경 3개월 경과시 비밀번호 변경 유도 기능 사용 가이드**
    - Authentication - Bindings - Brower Flow : **Browser With PasswordUpdateAlert** 선택
    - 비밀번호 변경( 한적이 없는 경우, 최초 생성 ) 3개월 경과시, 로그인 시 비밀번호 변경 유도 화면 나타남
    - 과거에 사용하던 비밀번호 사용 불가능
    - 다음에 변경하기 : 1개월 후에 다시 나타남
    
  - **탈퇴 신청 로그인시, 탈퇴 신청 철회 화면 사용 가이드**
    - Authentication - Bindings - Brower Flow : **Browser With User Withdrawal Cancel ** 선택
    - 탈퇴 취소 버튼 클릭 시, 탈퇴 신청이 취소되고, 정상적으로 서비스 이용가능
  
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
      
- **LOG 수집 가이드**
  - kubectl exec -it -n hyperauth -c log-collector $(kubectl get pod -n hyperauth | grep hyperauth | cut -d ' ' -f1) bash
  - cd logs/
  - 날짜별 수집된 로그를 확인 할 수 있음.
  
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
    - 현재사용중인 Key
      - **user_name**
      - **ipBlock**
      - **ipPermitList**
      - **isAdmin**
      - **otpEnable**
      - **agreeMailOpt**
      - **agree_ischecked_{client_name}**
      - **agreeAdv{client_name}Opt**
      - **withdrawal_unqualified_{client_name}**
    - USER
      - 사용자의 username은 attribute으로 관리
        - user_name : 한글/영어
        - user_name_ko : 한글
        - user_name_en : 영어
    - AGREEMENT
      - Hyperauth(공통)의 선택 약관
        - agreeMailOpt : true / false
      - Client별 최초 로그인시 약관
        - agree_ischecked_{ClientName} : true 
          - ex) agree_ischecked_portal : true / agree_ischecked_hypermeeting : true
      - Client별 수신동의 (선택약관)
        - agree{항목종류}{ClientName}Opt : true/false
          - ex) agreeAdvPortalOpt : true / agreeAdvHyperMeetingOpt : true
  
- **API 정보**

  - [API Documentation](/APIDOC.md)

# Feature

- `Login/Logout Event 발생 시, Auditing 기능` (Alpha)
- **회원가입/탈퇴 시, HyperCloud4 Role 생성/삭제 기능**
  - NSC, RBC 등 요청 리소스 생성 권한 부여
- **중복 Login 방지 기능**
  - 한 유저의 Client당 로그인 session을 하나만 유지
- **회원 가입 시 Email 인증 기능**
  - 가입 10분 후, 이메일 인증을 수행하지 않은 사용자 정보 삭제 

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
  - 그룹 관리자는 그룹에 속해 있어야 하고, attribute으로 isAdmin : {groupName} 을 가지고 있어야 한다.
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

  
