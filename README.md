# HyperAuth

## API 
* Prefix : http://{HYPERAUTH_IP}/auth/admin/realms/tmax  

### USER
* UserCreate
  * URL : POST /user
  * HEADERS :  Content-Type : application/json
  * BODY : 
  {
    "username": "test",
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

* UserDelete
  * URL: DELETE /user/{userName}
  * QUERY PARAMETER:  token : {AccessToken}
  
* UserDetail
  * URL: GET /user/{userName}
  
* UserAttributeUpdate
  * URL: PUT /user/{userName}
  * QUERY PARAMETER:  token : {AccessToken}
  * HEADERS :  Content-Type : application/json
  * BODY : ( Update 하고 싶은 Attribute만 넣어주면 됨 )
  {
    "attributes": {
      "dateOfBirth": "1992.01.02",
      "phone": "000-000-0000",
      "description": "userUpdateSuccess",
      "department": "ck1-3",	
      "position": "developer"
    }
  }
  

 
