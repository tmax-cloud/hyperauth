#!/bin/bash
hyperauthserver=$1
echo hyperauthserver : $hyperauthserver
password=$2
echo admin password : $password
usergroup=$3
echo join usergroup : $usergroup

# get Admin Token
token=$(curl -X POST 'http://'$hyperauthserver':8080/auth/realms/master/protocol/openid-connect/token' \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=admin" \
 -d 'password='$password'' \
 -d 'grant_type=password' \
 -d 'client_id=admin-cli' | jq -r '.access_token')

echo accessToken : $token

# Get Hypercloud5 UserGroup ID
groupid=$(curl -X GET 'http://'$hyperauthserver':8080/auth/admin/realms/tmax/groups' \
  -H "authorization: Bearer $token" | jq '[.[] | select(.name | contains("'$usergroup'")) | .id ]' | cut -f 2 -d '[' | cut -f 1 -d ']' | tr -d '"' | tr -d '' )

groupid2=$(echo $groupid | tr -d '')
echo $usergroup group id : $groupid2

useridlist=$(curl -i -X GET 'http://'$hyperauthserver':8080/auth/admin/realms/tmax/users' \
   -H "Authorization:Bearer $token" | grep id | jq .[].id | tr -d '"')
echo useridlist : $useridlist 


for userid in $useridlist
do
	echo userid : $userid
	echo 'http://'$hyperauthserver':8080/auth/admin/realms/tmax/users/'$userid'/groups/'$groupid2''
        curl 'http://'$hyperauthserver':8080/auth/admin/realms/tmax/users/'$userid'/groups/'$groupid2'' \
        -X 'PUT' \
        -H "content-type: application/json;charset=UTF-8" \
        -H "authorization: Bearer $token" 
done

