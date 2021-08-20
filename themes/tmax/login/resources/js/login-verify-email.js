
function goToLoginHome(loginUrl) {
  location.href = loginUrl;
}

function openModal() {
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
  location.reload();
}

function clickResendEmailButton(resendLink) {
  // document.getElementById("resend-link").href = resendLink;
  // TODO: href 실행해서 메일 전송되고 화면 리프레시 될 때 modal 띄워주는 방법 생각하기
  document.getElementById("resend-link").classList.add("isDisabled");
  axios.post(resendLink).then(() => {
    openModal();
    document.getElementById("resend-link").classList.remove("isDisabled");
  });
}
