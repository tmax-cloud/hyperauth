# HyperAuth API Specification
- HyperAuth에서 **확장 기능으로 제공하는 API 목록**
- **Keycloak 공식 Admin REST API 문서**
  - https://www.keycloak.org/docs-api/5.0/rest-api/index.html 

## USER
사용자의 username은 attribute으로 관리 (Key 값 고정)

- user_name : 한글/영어
- user_name_ko : 한글
- user_name_en : 영어

 #### Create
  - **Description** 

      <u>권한체크 없이</u> 사용자를 추가하는 API

      복수 사용자를 추가 가능 (List)

  - **RequestURL**
    
    POST https://{HYPERAUTH_IP}/auth/realms/tmax/user
    
  - **RequestHeader**
    
    Content-Type : application/json
    
  - **QueryParam**

      x

  - **PathParam**

      x

  - **RequestBody**
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
      ...
    },
    ...
  ]
  ```
- **ResponseBody**

  X

 #### Delete

  - **Description** 

    <u>사용자 자신의 권한으로</u> 사용자를 삭제하는 API

  - **RequestURL**

    DELETE https://{HYPERAUTH_IP}/auth/realms/tmax/user/{userName}

  - **RequestHeader**

    X

  - **QueryParam**

    token : {AccessToken}

  - **PathParam**

    x

  - **RequestBody**

    X

  - **ResponseBody**

    X

 #### Get
   - **Description** 
     
      <u>권한체크 없이</u> 사용자 정보를 조회하는 API
      
  - **RequestURL**

       GET https://{HYPERAUTH_IP}/auth/realms/tmax/user/{userName}

  - **RequestHeader**

       X

  - **QueryParam**

       token : {AccessToken}

  - **PathParam**

       x

  - **RequestBody**

       X

  - **ResponseBody**

       X

 #### AttributeUpdate
  - **Description** 
    
    <u>사용자 자신의 권한으로</u> 사용자 정보를 수정하는 API
    
    <u>사용자 자신의 권한으로</u> 탈퇴를 신청하는 API (겸)
    
  - **RequestURL**

      PUT https://{HYPERAUTH_IP}/auth/realms/tmax/user/{userName}

  - **RequestHeader**

      Content-Type : application/json

  - **QueryParam**

      token : {AccessToken}

       withdrawal : t  (사용자 탈퇴 신청을 할 경우 추가)

  - **PathParam**

      x

  - **RequestBody**

        - Update를 원하는 Attribute만 추가
      - 탈퇴 신청 시, 필요 없음

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

  - **ResponseBody**

      X
## GroupMember

- 중간관리자는 **isAdmin** Attribute에 자신이 관리자로 속해있는 GroupsName을 가짐

- 여러 Group의 관리자인 유저는 , 로 구분해서 띄워쓰기 없이 추가

- Example

  ```json
  {
    "attributes": {
      "isAdmin": "Group1,Group2"
    }
  }
  ```

- 중간 관리자의 token에 isAdmin claim이 필요
  - clients의 Mapper로 등록 or Client Scopes의 등록을 통해서 token에 custom claim을 추가 가능
  - 참고 : https://www.keycloak.org/docs/latest/server_admin/#_clients

#### Create
  - **Description** 

    <u>중간관리자의 권한으로</u> 사용자를 추가하는 API

    중간관리자는 groups attribute의 모든 group에 대한 중간 관리자

    복수의 사용자를 추가 가능 (List)

  - **RequestURL**

    POST https://{HYPERAUTH_IP}/auth/realms/tmax/groupMember

  - **RequestHeader**

    Content-Type : application/json

  - **QueryParam**

    token : {AccessToken} **(중간관리자의 token)**

    **PathParam**

    x

  - **RequestBody**

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

    

  - **ResponseBody**

    X
#### Get
  - **Description** 
    
    <u>중간관리자의 권한으로</u> 해당 그룹의 하위 사용자 정보 전체를 조회하는 API
    
  - **RequestURL**

    GET https://{HYPERAUTH_IP}/auth/realms/tmax/groupMember/{groupName}

  - **RequestHeader**

    X

  - **QueryParam**

    token : {AccessToken} **(중간관리자의 token)**

  - **PathParam**

    x

  - **RequestBody**

    X

  - **ResponseBody**

    X

#### AttributeUpdate
  - **Description** 
    
    <u>중간관리자의 권한으로</u> 해당 그룹의 하위 사용자 attribute를 수정하는 API
    
  - **RequestURL**

    PUT https://{HYPERAUTH_IP}/auth/realms/tmax/groupMember/{groupName}

  - **RequestHeader**

    Content-Type : application/json

  - **QueryParam**

    token : {AccessToken} **(중간관리자의 token)**

    userName : {userName}

  - **PathParam**

    x

  - **RequestBody**

      - Update 하고 싶은 Attribute만 넣어주면 됨 

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

  - **ResponseBody**

    X
## AGREEMENT  

- 이용약관 중, 선택동의 약관의 경우 user의 attribute에 아래 key를 추가 (공통)
  - agreeMailOpt : true
  
  - #### Create/Update

      - **Description** 

        <u>권한체크없이</u> Client의 Agreement를 추가/수정하는 API

        같은 client/version의 Ageement를 삭제 후, 생성

      - **RequestURL**

        POST https://{HYPERAUTH_IP}/auth/realms/tmax/agreement

      - **RequestHeader**

        Content-Type : application/json

      - **QueryParam**

        X

      - **PathParam**

        x

      - **RequestBody**

          ```json
          {
           "clientName" : "hypercloud4",
           "realmName" : "tmax",
           "agreement" : "hypercloud4 agreemente version1 for test",
           "version" : "1"
          }
          ```

      - **ResponseBody**

        X
#### Delete

  - **Description** 
    
    <u>권한체크없이</u> 특정 client의 약관을 삭제하는 API
    
  - **RequestURL**

    DELETE https://{HYPERAUTH_IP}/auth/realms/tmax/agreement/{clientName}

  - **RequestHeader**

    X

  - **QueryParam**

    version : {version}

    realmName : {realmName}

  - **RequestBody**

    X

  - **ResponseBody**

    X

#### Get
  - **Description** 
    
    <u>권한체크없이</u> 특정 client의 약관을 조회하는 API
    
  - **RequestURL**

    GET https://{HYPERAUTH_IP}/auth/realms/tmax/agreement/{clientName}

  - **RequestHeader**

    X

  - **QueryParam**

    version : {version}

    realmName : {realmName}

  - **RequestBody**

    X

  - **ResponseBody**

    X

## CERTS
#### Get
  - **Description** 
    
    <u>권한체크없이</u> token verify를 위한 cert를 조회하는 API
    
    keys / x5c에 들어 있는 String이 Certificate String
    
  - **RequestURL**

    GET https://{HYPERAUTH_IP}/auth/realms/tmax/protocol/openid-connect/certs

  - **RequestHeader**

    X

  - **QueryParam**

    version : {version}

    realmName : {realmName}

  - **RequestBody**

    X

  - **ResponseBody**

    X

## EMAIL
### EmailSend
  - **Description** 

    <u>권한체크없이</u> 회원가입 시, 비밀번호 초기화 요청시에 메일로 인증 번호를 전송하는 API

  - **RequestURL**

    POST https://{HYPERAUTH_IP}/auth/realms/tmax/email/{email_adress}

  - **RequestHeader**

    X

  - **QueryParam**

    resetPassword : t  (Optional : 비밀번호 초기화 요청시)

  - **RequestBody**

    X

  - **ResponseBody**

    X

### EmailVerify
  - **Description** 
    
    <u>권한체크없이</u> 회원가입, 비밀번호 초기화 시에 인증번호를 검증하는 API
    
    - 회원가입 시 : 가입 절차 완료
    - 비밀번호 초기화 시 : 인증번호가 맞으면, PasswordUpdate With Code 서비스 호출 가능
    
  - **RequestURL**

    GET https://{HYPERAUTH_IP}/auth/realms/tmax/email/{email_address}

  - **RequestHeader**

    X

  - **QueryParam**

    code : {codeDIGIT}

    resetPassword : t (Optional : 비밀번호 초기화 요청시)

  - **RequestBody**

    X

  - **ResponseBody**

    X

## PASSWORD
#### UpdateWithToken
  - **Description** 
    
    <u>자기자신의 권한으로</u> 비밀번호를 변경하는 API
    
  - **RequestURL**

    PUT https://{HYPERAUTH_IP}/auth/realms/tmax/password

  - **RequestHeader**

    X

  - **QueryParam**

    token : {accessToken}

    password : {changedPassword}

    confirmPassword : {changedPassword}

    email : {email_address}

  - **RequestBody**

    X

  - **ResponseBody**

    X

#### UpdateWithCode 
  - **Description** 

    resetPassword 이후 호출 가능한 비밀번호 변경 API

    EmailVerify시 사용했던 email 과 code를 다시 한번 넣어주어야 함

  - **RequestURL**

    PUT https://{HYPERAUTH_IP}/auth/realms/tmax/password

  - **RequestHeader**

    X

  - **QueryParam**

    code : {code}

    password : {changedPassword}

    confirmPassword : {changedPassword}

    email : {email_address}

  - **RequestBody**

    X

  - **ResponseBody**

    X

#### Verify
  - **Description** 

    <u>권한체크 없이</u> 사용자의 비밀번호가 맞는지 검증하는 API

  - **RequestURL**

    GET https://{HYPERAUTH_IP}/auth/realms/tmax/password

  - **RequestHeader**

    X

  - **QueryParam**

    userId : {email_address}

    password : {password}

  - **RequestBody**

    X

  - **ResponseBody**

    X
    
## SESSION

 #### Get
  - **Description** 

      <u>로그인시 발급받는 session id로 </u> 자동로그인 여부 (isRememberMe) 를 확인하는 API

  - **RequestURL**
    
    GET https://{HYPERAUTH_IP}/auth/realms/tmax/session

  - **PathParam**

      {sessionId}
      
  - **ResponseBody**

    String : on/off   




  
