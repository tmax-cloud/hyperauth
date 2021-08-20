// kakao
function loginWithKakao() {
  const key = document.getElementById("kakao-api-key").value;
  console.log(key);
  Kakao.init(key);
  console.log(Kakao.isInitialized());
  Kakao.Auth.login({
    success: function (authObj) {
      // alert(JSON.stringify(authObj))
      alert("인증되었습니다.");

      // 인증 완료 시, 탈퇴 신청 버튼에 vendor 값 넣어줌
      document
        .getElementById("withdrawal-submit-button")
        .setAttribute("data-vendor", "kakao");
      document
        .getElementById("withdrawal-submit-button")
        .removeAttribute("disabled");
    },
    fail: function (err) {
      alert(JSON.stringify(err));
    },
  });
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
