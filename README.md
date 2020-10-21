# HyperAuth
- 해당 API 문서는 keycloak을 확장해서 keycloak 서버에 추가적으로 개발한 API를 정리 한 것임
- keycloak 공식 Admin REST API 문서 : https://www.keycloak.org/docs-api/5.0/rest-api/index.html
- 설치 가이드 
  - https://github.com/tmax-cloud/hypercloud-install-guide/tree/4.1/HyperAuth 
- 최신이미지
  - https://hub.docker.com/repository/docker/tmaxcloudck/hyperauth 참고해서 가장 최신이미지 사용 권장

# API 
- Prefix 
  - http://{HYPERAUTH_IP}/auth/admin/realms/tmax  

## USER
  
 ### UserCreate
  - URL  
  POST /user
  - HEADERS   
  Content-Type : application/json
  - BODY 
  ```json
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
  }
  ```

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
### GroupMemberCreate
  - URL  
  POST /groupMember
  - QUERY PARAMETER  
  token : {AccessToken} ( 중간관리자의 token )
  - HEADERS  
  Content-Type : application/json
  - BODY  
  ```json
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
  }
  ```
  
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
### PasswordUpdate With Code 
  - URL  
  PUT /passworrd
  - QUERY PARAMETER  
  code : 123456 </br> password : Qwerqwer1! </br> confirmPassword : Qwerqwer1! </br> email : {email_address}
  - Description  
    - resetPassword 시 사용  
    - EmailVerify시 사용했던 email 과 code를 다시 한번 넣어주어야 함

  
