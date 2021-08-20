if (!!document.getElementById("user-email-text")) {
  if (
    !!window.sessionStorage.getItem("idp-user-email") &&
    window.sessionStorage.getItem("idp-user-email") != ""
  ) {
    document.getElementById(
      "user-email-text"
    ).innerHTML = window.sessionStorage.getItem("idp-user-email");
  }
}

function openModal() {
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
  location.reload();
}

function clickResendEmailButton(resendLink) {
  document.getElementById("resend-link").classList.add("isDisabled");
  axios.post(resendLink).then(() => {
    openModal();
    document.getElementById("resend-link").classList.remove("isDisabled");
  });
}
