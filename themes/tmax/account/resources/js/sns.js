// kakao
function loginWithKakao() {
  const key = document.getElementById("kakao-api-key").value;
  const kakao_username =  document.getElementById("kakao-username").value;
  
  Kakao.init(key);
  //console.log(Kakao.isInitialized());
  Kakao.Auth.login({
    success: function (authObj) {
      Kakao.API.request({
        url: '/v2/user/me',
        data: {
            property_keys: ["kakao_account.email"]
        },
        success: function(response) {
            if(response.kakao_account.email ==kakao_username){
              // alert("인증되었습니다.")
              // 인증 완료 시, 탈퇴 신청 버튼에 vendor 값 넣어줌
              //modal로 인증되었습니다. 띄우기만 하면 될듯
              openSNSAuthModal();

              document
              .getElementById("withdrawal-submit-button")
              .setAttribute("data-vendor", "kakao");
              document
              .getElementById("withdrawal-submit-button")
              .removeAttribute("disabled");
            } else {
              alert("인증 실패")
            }
        },
        fail: function(error) {
            console.log(error);
        }
    });

      
    },
    fail: function (err) {
      alert(JSON.stringify(err));
    },
  });
}

function openSNSAuthModal() {
  document.querySelector(".modal").classList.remove("hidden");
}

function okSNSAuth() {
  document.querySelector(".modal").classList.add("hidden");
}

// naver
if (document.getElementById("naver_id_login")) {
  var naver_id_login = new naver_id_login(
    "8dWFYbRLx92jgjXvABWI",
    `https://${window.location.host}/auth/realms/tmax/account`
  );
  var state = naver_id_login.getUniqState();
  naver_id_login.setButton("white", 3, 40);
  naver_id_login.setDomain(
    `https://${window.location.host}/auth/realms/tmax/account`
  );
  naver_id_login.setState(state);
  // naver_id_login.setPopup();
  naver_id_login.init_naver_id_login();
  if (naver_id_login.oauthParams.access_token) {
    alert(naver_id_login.oauthParams.access_token);

    // 인증 완료 시, 탈퇴 신청 버튼에 vendor 값 넣어줌
    document
      .getElementById("withdrawal-submit-button")
      .setAttribute("data-vendor", "naver");
    document
      .getElementById("withdrawal-submit-button")
      .removeAttribute("disabled");
  }
}
