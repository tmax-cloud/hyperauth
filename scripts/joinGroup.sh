#!/bin/bash
hyperauthserver=$1
echo hyperauthserver : $hyperauthserver
password=$2
echo admin password : $password
usergroup=$3
echo join usergroup : $usergroup

# get Admin Token
token=$(curl -k -X POST 'https://'$hyperauthserver'/auth/realms/master/protocol/openid-connect/token' \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=admin" \
 -d 'password='$password'' \
 -d 'grant_type=password' \
 -d 'client_id=admin-cli' | jq -r '.access_token')

echo accessToken : $token

# Get Hypercloud5 UserGroup ID
groupid=$(curl -k -X GET 'https://'$hyperauthserver'/auth/admin/realms/tmax/groups' \
  -H "authorization: Bearer $token" | jq '[.[] | select(.name == "'$usergroup'") | .id ]' | cut -f 2 -d '[' | cut -f 1 -d ']' | tr -d '"' | tr -d '' )

groupid2=$(echo $groupid | tr -d '')
echo $usergroup group id : $groupid2

useridlist=$(curl -i -k -X GET 'https://'$hyperauthserver'/auth/admin/realms/tmax/users?max=10000' \
   -H "Authorization:Bearer $token" | grep id | jq .[].id | tr -d '"')
echo useridlist : $useridlist 


for userid in $useridlist
do
	echo userid : $userid
	echo 'https://'$hyperauthserver'/auth/admin/realms/tmax/users/'$userid'/groups/'$groupid2''
        curl -k 'https://'$hyperauthserver'/auth/admin/realms/tmax/users/'$userid'/groups/'$groupid2'' \
        -X 'PUT' \
        -H "content-type: application/json;charset=UTF-8" \
        -H "authorization: Bearer $token" 
done

