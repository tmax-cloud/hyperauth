# HyperAuth 구성
- Keycloak 11.0.2 Based : hyperauth b1.0.10.0 부터
- Keycloak 10.0.2 Based : hyperauth b1.0.9.29 버전까지
- EventListener SPI
- DB Extend SPI (CLIENT_AGREEMENT, EMAIL_VERIFICATION)
- 네이버로 로그인, 카카오로 로그인
- User Registration Form SPI
- Extend API Server SPI
- 설치 가이드 
  - https://github.com/tmax-cloud/hypercloud-install-guide/tree/4.1/HyperAuth 
- 최신이미지
  - https://hub.docker.com/repository/docker/tmaxcloudck/hyperauth 참고해서 가장 최신이미지 사용 권장

# API 
- 해당 API 문서는 keycloak을 확장해서 keycloak 서버에 추가적으로 개발한 API를 정리 한 것임
- keycloak 공식 Admin REST API 문서  
  - https://www.keycloak.org/docs-api/5.0/rest-api/index.html
- Prefix 
  - http://{HYPERAUTH_IP}/auth/realms/tmax  
- realmName 은 **tmax로 고정한다.**

## USER
- keycloak의 username은 한글을 지원하지 않기 때문에 attribute으로 관리해야한다. 아래에 해당하는 key값으로 관리하는 것으로 한다. (공통)
  - 한글 영어 구분이 없이 입력받을 경우 : username
  - 한글이름 : username_ko
  - 영어이름 : username_en
  
 ### UserCreate
  - URL  
  POST /user
  - HEADERS   
  Content-Type : application/json
  - BODY 
  ```json
  [
    {
      "username": "test@tmax.co.kr",
      "emailVerified": true,
      "email": "test@tmax.co.kr",
      "enabled": true,
      "attributes": {
        "dateOfBirth": "1992.01.02",
        "phone": "000-000-0000",
        "description": "userCreateSuccess",
        "department": "ck1-3",	
        "position": "developer"
      },
      "credentials": [{
        "value": "Qwerqwer1!"
      }]
    },
    {
      "username": "test2@tmax.co.kr",
      "emailVerified": true,
      "email": "test2@tmax.co.kr",
      "enabled": true,
      "attributes": {
        "dateOfBirth": "1992.01.02",
        "phone": "000-000-0000",
        "description": "userCreateSuccess",
        "department": "ck1-3",	
        "position": "developer"
      },
      "credentials": [{
        "value": "Qwerqwer1!"
      }]
    }
  ]
  ```
  - Description 
    - 복수의 사용자를 추가 할 수 있음.

 ### UserDelete
  - URL   
  DELETE /user/{userName}
  - QUERY PARAMETER   
  token : {AccessToken}
 ### UserDetail
   - URL    
   GET /user/{userName}
  
 ### UserAttributeUpdate
  - URL  
  PUT /user/{userName}
  - QUERY PARAMETER  
  token : {AccessToken}
  - HEADERS  
  Content-Type : application/json
  - BODY   
    - Update 하고 싶은 Attribute만 넣어주면 됨 
  ```json
  {
    "attributes": {
      "dateOfBirth": "1992.01.02",
      "phone": "000-000-0000",
      "description": "userUpdateSuccess",
      "department": "ck1-3",	
      "position": "developer"
    }
  }
  ```
  
## GroupMember
- GroupMember 관련 API를 사용하기 위한 중간관리자 user는 **isAdmin** Attribute에 자기 자신이 속해있고, 관리자인 Groups의 이름들을 가지고 있어야 한다.
- 여러 Group의 관리자인 유저는 , 로 구분해서 띄워쓰기 없이 추가한다.
- ex) key : isAdmin  |  value : groupA,groupB
- 중간 관리자의 token에 isAdmin claim 이 추가 되어 있는지 확인한다. 

### GroupMemberCreate
  - URL  
  POST /groupMember
  - QUERY PARAMETER  
  token : {AccessToken} **( 중간관리자의 token )**
  - HEADERS  
  Content-Type : application/json
  - BODY  
  ```json
  [
    {
      "username": "test@tmax.co.kr",
      "emailVerified": true,
      "email": "test@tmax.co.kr",
      "enabled": true,
      "attributes": {
        "dateOfBirth": "1992.01.02",
        "phone": "000-000-0000",
        "description": "userCreateSuccess",
        "department": "ck1-3",	
        "position": "developer"
      },
      "groups": [
        "woo-group", "woo-group2"
      ],   
      "credentials": [{
          "value": "Qwerqwer1!"
        }]
    },
    {
      "username": "test2@tmax.co.kr",
      "emailVerified": true,
      "email": "test2@tmax.co.kr",
      "enabled": true,
      "attributes": {
        "dateOfBirth": "1992.01.02",
        "phone": "000-000-0000",
        "description": "userCreateSuccess",
        "department": "ck1-3",	
        "position": "developer"
      },
      "groups": [
        "woo-group", "woo-group2"
      ],   
      "credentials": [{
          "value": "Qwerqwer1!"
        }]
    }
  ]
  ```
  - Description
    - 중간관리자는 groups 절에 명시 할 모든 group에 대한 중간 관리자여야 한다.
    - 복수의 사용자를 추가 할 수 있음.
  
### GroupMemberListGet
  - URL  
  GET /groupMember/{groupName}
  - QUERY PARAMETER  
  token : {AccessToken} **( 중간관리자의 token )**
  
### GroupMemberAttributeUpdate
  - URL  
  PUT /groupMember/{groupName}
  - QUERY PARAMETER  
  token : {AccessToken} **( 중간관리자의 token )** </br> userName : {userName}
  - HEADERS   
  Content-Type : application/json
  - BODY   
    -  Update 하고 싶은 Attribute만 넣어주면 됨 
  ```json
  {
    "attributes": {
      "dateOfBirth": "1992.01.02",
      "phone": "000-000-0000",
      "description": "groupMemberUpdateSuccess",
      "department": "ck1-3",	
      "position": "developer"
    }
  }  
  ```
  
## AGREEMENT  
- 이용약관 중 필수가 아닌 선택이 필요한 약관의 경우 user의 attribute으로 관리하는 것으로 한다. 아래의 key값으로 관리하는 것으로 한다. ( 공통 )
  - agreeMailOpt
  
### AgreementCreate ( Update와 동일하게 동작해서 같은 조건의 Agreement를 덮어쓴다. )
  - URL  
  POST /agreement
  - HEADERS   
  Content-Type : application/json
  - BODY  
    - Update 하고 싶은 Attribute만 넣어주면 됨  
  ```json
  {
   "clientName" : "hypercloud4",
   "realmName" : "tmax",
   "agreement" : "hypercloud4 agreemente version1 for test",
   "version" : "1"
  }
  ```
  
### AgreementDelete
  - URL  
  DELETE /agreement/{clientName}
  - QUERY PARAMETER  
  version : {version} </br> realmName : {realmName}
  
### AgreementGet
  - URL  
  GET /agreement/{clientName}
  - QUERY PARAMETER  
  version : {version} </br> realmName : {realmName}

## CERTS
### CertsGet
  - URL  
  GET /protocol/openid-connect/certs
  - Description  
  token validate에 쓰일 Certs를 가져올때 사용 </br> keys / x5c에 들어 있는 String이 Certificate String임.
  
## EMAIL
### EmailSend
  - URL  
  POST /email/{email_adress}
  - QUERY PARAMETER  
  resetPassword : t ( resetPassword 용으로 mail을 보낼때만, resetPassword : t 를 추가해서 보내주면 됨 )
  - Description : register 시, resetPassword 시 두가지 경우, 모두 메일로 인증 번호 6자리 보내주는 API
    - register : hyperauth에 등록된 이메일 일 경우, 에러 발생
    - resetPassword : hyperauth에 등록되지 않은 이메일 일 경우, 에러 발생

### EmailVerify
  - URL  
  GET /email/{email_address}
  - QUERY PARAMETER  
  code : 123456 </br> resetPassword : t ( resetPassword 용으로 인증번호 verify 할때만, resetPassword : t 를 추가해서 보내주면 됨 )
  - Description : register 시, resetPassword 시 두가지 경우, 사용자가 메일로 받은 인증번호를 인증하는 API
    - register : 인증번호가 맞으면 인증 끝
    - resetPassword : 인증번호가 맞으면, PasswordUpdate With Code 서비스를 부를 준비가 완료됨

## PASSWORD
### PasswordUpdate With Token
  - URL  
  PUT /password
  - QUERY PARAMETER  
  token : eyqasdfp.... </br> password : Qwerqwer1! </br> confirmPassword : Qwerqwer1! </br> email : {email_address}
  - Description  
    - 자기 자신만이 비밀번호를 바꿀 수 있는 API
    - AccessToken을 넣어준다

### PasswordUpdate With Code 
  - URL  
  PUT /password
  - QUERY PARAMETER  
  code : 123456 </br> password : Qwerqwer1! </br> confirmPassword : Qwerqwer1! </br> email : {email_address}
  - Description  
    - resetPassword 시 사용  
    - EmailVerify시 사용했던 email 과 code를 다시 한번 넣어주어야 함

### PasswordVerify
  - URL  
  GET /password
  - QUERY PARAMETER  
  userId : taegeon_woo@tmax.co.kr </br> password : admin 
  - Description  
    - id, password로 사용자의 비밀번호가 맞는지 아닌지를 판단하는 서비스

  
