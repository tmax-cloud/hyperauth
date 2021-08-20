const checkEl = document.querySelector("input[type='checkbox']");
const offEl = document.querySelector("#off");
const onEl = document.querySelector("#on");
const formElEmailOtp = document.querySelector("#emailOtpAuthUrl");
const formElSimpleLogin = document.querySelector("#simpleLoginUrl");
if (checkEl.checked === true) {
  offEl.style.display = "none";
  onEl.style.display = "";
}

checkEl.addEventListener("click", (e) => {
  document.getElementById("additionalAuth-save-button").removeAttribute("disabled");
  if (offEl.style.display === "none") {
    offEl.style.display = "";
  } else {
    offEl.style.display = "none";
  }

  if (onEl.style.display === "none") {
    onEl.style.display = "";
  } else {
    onEl.style.display = "none";
  }
});

formElEmailOtp.addEventListener("submit", (e) => {
  if (checkEl.checked !== true) {
    const input = document.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", "otpEnable");
    input.setAttribute("value", "false");
    formEl.append(input);

    // checkEl.value="false"
    // checkEl.checked="true";
  }
});

formElSimpleLogin.addEventListener("submit", (e) => {
  if (checkEl.checked !== true) {
    const input = document.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", "simpleLogin");
    input.setAttribute("value", "false");
    formEl.append(input);

    // checkEl.value="false"
    // checkEl.checked="true";
  }
});

function openCancelModal() {
  document.getElementById("cancelModal").classList.remove("hidden");
}

function cancelChangeAdditionalAuth() {
  location.href = document.location;
}
