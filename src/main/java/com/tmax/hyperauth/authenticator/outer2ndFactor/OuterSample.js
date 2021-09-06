function getURLParams(){
    const queryString =window.location.search;
    console.log(queryString);
    //let testURL = 'http://localhost:63342/keycloak-services/keycloak-spi/com/tmax/hyperauth/authenticator/outer2ndFactor/OuterSample.html?user_name=taegeon_woo@tmax.co.kr&realm_name=tmax&tab_id=kaIFpobtHXo&additional_param=010-4878-8544&secret_key=topsecret';
    //testURL 대신 queryString 넣어주면 됨
    const URLParams = new URLSearchParams(queryString);
    for(const param of URLParams){
        console.log(param)
    }
    const user_name = URLParams.get('user_name')
    const realm_name = URLParams.get('realm_name')
    const tab_id = URLParams.get('tab_id')
    const additional_param = URLParams.get('additional_param')
    console.log(realm_name);

    const resultURL = `http://localhost:8080/auth/realms/tmax/outer2ndFactor?user_name=${user_name}&realm_name=${realm_name}&tab_id=${tab_id}&secret_key=topsecret&additional_param=${additional_param}`

    return resultURL;
}
window.onload = function(){
    const certificationButton = document.getElementById('certificationBt');

    certificationButton.onclick = function (event) {
        console.log('버튼')
        try {
            axios.get(
                getURLParams()
            ).then((response) => {
                console.log(response)
                if(response.status ==200){
                    alert("인증성공!!\nHyperauth 페이지에서 인증완료를 눌러 인증과정을 마쳐주세요.")
                }
            });

        } catch (e) {
            console.error(e);
        }
    }
}