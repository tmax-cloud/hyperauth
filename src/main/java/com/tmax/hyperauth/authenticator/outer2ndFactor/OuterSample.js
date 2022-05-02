function getURLParams(){
    const queryString =window.location.search;
    const URLParams = new URLSearchParams(queryString);
    const user_name = URLParams.get('user_name')
    const realm_name = URLParams.get('realm_name')
    const tab_id = URLParams.get('tab_id')
    const additional_param = URLParams.get('additional_param')

    const authURL = `localhost:8180`  // FIXME : change to Hyperauth URL

    const resultURL = `http://${authURL}/auth/realms/tmax/outer2ndFactor?user_name=${user_name}&realm_name=${realm_name}&tab_id=${tab_id}&secret_key=topsecret&additional_param=${additional_param}`

    return resultURL;
}
window.onload = function(){
    const certificationButton = document.getElementById('certificationBt');

    certificationButton.onclick = function (event) {
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