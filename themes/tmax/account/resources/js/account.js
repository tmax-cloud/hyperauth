const serverUrl = document.location.origin;
const MAX_NAME_LANGTH = 50;
const getAccessToken = function () {
  return sessionStorage.getItem("accessToken");
};

const validationStates = {
  email: false,
  password: false,
  passwordConfirm: false,
  username: false,
};

function validateUserName() {
  const username = document.getElementById("userNameAttr");
  let result1 = false;
  let result2 = false;
  let result3 = false;

  if (username.value.length < MAX_NAME_LANGTH) {
    //username.classList.add("has_error");
    showErrorMessage("error_username_over", false);
    validationStates.username = true;
    checkAllInputStates();
    //return true;
    result1 = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_username_over", true);
    validationStates.username = false;
    checkAllInputStates();
    //return false;
    result1 = false;
  }

  if (!!username.value && username.value != "") {
    //username.classList.remove("has_error");
    showErrorMessage("error_username_empty", false);
    validationStates.username = true;
    checkAllInputStates();
    //return true;
    result2 = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_username_empty", true);
    validationStates.username = false;
    checkAllInputStates();
    //return false;
    result2 = false;
  }
  const formatReg = /^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s]+$/;

  //email.classList.remove("has-error");
  if (
    formatReg.test(username.value) ||
    !(!!username.value && username.value != "")
  ) {
    showErrorMessage("error_username_format", false);
    //username.classList.remove("has_error");
    validationStates.email = true;
    checkAllInputStates();
    //return true;
    result3 = true;
  } else {
    showErrorMessage("error_username_format", true);
    //username.classList.add("has_error");
    validationStates.email = false;
    checkAllInputStates();
    //return false;
    result3 = false;
  }
  let result = false;
  if (result1 && result2 && result3) {
    username.classList.remove("has_error");
    result = true;
  } else {
    username.classList.add("has_error");
    result = false;
  }

  return result;
}
function showErrorMessage(errorId, visible) {
  const error = document.getElementById(errorId);
  error.style.display = visible ? "block" : "none";
}

function checkAllInputStates() {
  const registerButton = document.getElementById("account-save-button");
  for (let value of Object.values(validationStates)) {
    if (!value) {
      registerButton.disabled = true;
      return;
    }
  }

  registerButton.disabled = false;
}

function buttonAbled() {
  //error check
  let nameTestResult = validateUserName();
  let result = true;
  const username = document.getElementById("userNameAttr");
  const originName = document.getElementById("originName");

  if (!nameTestResult) {
    result = false;
  } else {
    result = true;
    if (username.value == originName.value) {
      result = false;
    } else {
      result = true;
    }
  }
  if (result) {
    document.getElementById("account-save-button").removeAttribute("disabled");
  } else {
    document
      .getElementById("account-save-button")
      .setAttribute("disabled", "disabled");
  }
}

function isEmptyPassword() {
  const password = document.getElementById("password");
  let result = false;

  if (!!password.value && password.value != "") {
    //username.classList.remove("has_error");
    showErrorMessage("error_password_empty", false);
    validationStates.password = true;
    checkAllInputStates();
    //return true;
    result = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_password_empty", true);
    validationStates.password = false;
    checkAllInputStates();
    //return false;
    result = false;
  }
  password.classList.remove("has_error");
  showErrorMessage("error_password_wrong", false);
  if (!result) {
    password.classList.add("has_error");
  }
  return result;
}

function withdrawalSubmitButtonAbled() {
  if (isEmptyPassword()) {
    document
      .getElementById("withdrawal-submit-button")
      .removeAttribute("disabled");
  } else {
    document
      .getElementById("withdrawal-submit-button")
      .setAttribute("disabled", "disabled");
  }
}

function clickEye(e) {
  if (e.classList.contains("activate")) {
    e.classList.remove("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "password";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "password";
    }
  } else {
    e.classList.add("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "text";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "text";
    }
  }
}

function openModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
}

function openSaveModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".saveModal").classList.remove("hidden");
}

function closeSaveModal() {
  document.querySelector(".saveModal").classList.add("hidden");
}

function openCancelModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".cancelModal").classList.remove("hidden");
}
function closeCancelModal() {
  document.querySelector(".cancelModal").classList.add("hidden");
}

function changeName() {
  const saveForm = document.getElementById("account-update-form");

  const email = document.createElement("input");
  email.setAttribute("type", "hidden");
  email.setAttribute("name", "email");
  email.setAttribute("value", document.getElementById("email").value);
  saveForm.appendChild(email);

  const submitAction = document.createElement("input");
  submitAction.setAttribute("type", "hidden");
  submitAction.setAttribute("name", "submitAction");
  submitAction.setAttribute("value", "Save");
  saveForm.appendChild(submitAction);

  saveForm.submit();
  openSaveModal();
}

function cancelChangeName() {
  const cancelForm = document.getElementById("account-update-form");

  const email = document.createElement("input");
  email.setAttribute("type", "hidden");
  email.setAttribute("id", "email");
  email.setAttribute("name", "email");
  email.setAttribute("value", document.getElementById("email").value);
  cancelForm.appendChild(email);

  const submitAction = document.createElement("input");
  submitAction.setAttribute("type", "hidden");
  submitAction.setAttribute("id", "submitAction");
  submitAction.setAttribute("name", "submitAction");
  submitAction.setAttribute("value", "Cancel");
  cancelForm.appendChild(submitAction);

  cancelForm.submit();
}

function openWithdrawalPage() {
  document.getElementById("account-update").classList.add("hidden");
  document.getElementById("withdrawal-step1").classList.remove("hidden");

  // withdrawal-step1 페이지 노출시에 약관 가져오는 것으로 변경 (private에서 아예 콜을 안하게 하기위함)
  getTerms()
}

function closeWithdrawalPage() {
  document.getElementById("withdrawal-form").reset();
  document.getElementById("withdrawal-step1").classList.add("hidden");
  document.getElementById("withdrawal-step2").classList.add("hidden");
  document.getElementById("withdrawal-success").classList.add("hidden");
  document.getElementById("withdrawal-failure").classList.add("hidden");
  document.getElementById("account-update").classList.remove("hidden");
}

function nextWithdrawalPage() {
  document.getElementById("withdrawal-step1").classList.add("hidden");
  document.getElementById("withdrawal-step2").classList.remove("hidden");
}

async function submitWithdrawal() {
  const withdrawalForm = document.getElementById("withdrawal-form");
  const withdrawalUrl = withdrawalForm.getAttribute("action");
  const stateChecker = document.getElementById("stateChecker-withdrawal").value;

  document
    .getElementById("withdrawal-submit-button")
    .setAttribute("disabled", "disabled");
  document
    .getElementById("withdrawal-cancel-button")
    .setAttribute("disabled", "disabled");

  let withForm = new FormData();
  withForm.append("stateChecker", stateChecker);
  let rejectService = "";

  let passwordResult = false;
  let withdrawalResult = false;

  // vendor에서 인증 받지 않은 경우만 패스워드 검증 실행
  if (
    !document
      .getElementById("withdrawal-submit-button")
      .getAttribute("data-vendor")
  ) {
    try {
      const email = document.getElementById("email-withdrawal").value;
      const password = document.getElementById("password").value;

      const params = new URLSearchParams();
      params.append("password", password);
      let passwordUrl = `${serverUrl}/auth/realms/tmax/password`;

      const passwordResp = await axios.patch(passwordUrl, params, {
        params: {
          userId: email,
        },
      });
      if (passwordResp.data == "Password is Correct") {
        passwordResult = true;
      }
    } catch (e) {
      console.error(e);
      showErrorMessage("error_password_wrong", true);
      validationStates.password = false;
      document.getElementById("password").classList.add("has_error");
      document
        .getElementById("withdrawal-submit-button")
        .setAttribute("disabled", "disabled");
      return passwordResult;
    }
  }

  try {
    const withdrawalResp = await axios.post(withdrawalUrl, withForm, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    let outString = withdrawalResp.data;
    let serviceList = [
      "wapl",
      "tmaxcloud",
      "hypermeeting",
      "waplclass",
      "waplmath",
    ];

    if (serviceList.includes(outString)) {
      rejectService = outString;
      withdrawalResult = false;
    }

    withdrawalResult = true;
  } catch (e) {
    rejectService = e.response.data;
    withdrawalResult = false;
    console.error(e);
  }

  if (withdrawalResult) {
    document.getElementById("withdrawal-step2").classList.add("hidden");
    document.getElementById("withdrawal-success").classList.remove("hidden");
    return withdrawalResult;
  } else {
    //test service
    //let testService = "테스트 서비스"
    const MSG = document.getElementById("disableService");
    //MSG.innerHTML = testService + "의 "+ MSG.innerHTML;
    MSG.innerHTML = rejectService + MSG.innerHTML;
    document.getElementById("withdrawal-step2").classList.add("hidden");
    document.getElementById("withdrawal-failure").classList.remove("hidden");
    return withdrawalResult;
  }
}

function openAgreementModal(agr_number) {
  //let agreementContent = '';
  if (agr_number == "1") {
    document
      .getElementById("account_terms_modal_title")
      .classList.remove("hidden");
    document.getElementById("account_terms_title").classList.remove("hidden");
    document.getElementById("account_terms").classList.remove("hidden");
    //agreementContent = "OneAccount Agreement"
  } else {
    document
      .getElementById("service_terms_modal_title")
      .classList.remove("hidden");
    document.getElementById("service_terms_title").classList.remove("hidden");
    document.getElementById("service_terms").classList.remove("hidden");
    //agreementContent = "Tmax Agreement"
  }

  //document.getElementById("Agreement_content").innerHTML = agreementContent;

  document.querySelector(".agreementModal").classList.remove("hidden");
}
function cloaseAgreementModal() {
  document.getElementById("account_terms_modal_title").classList.add("hidden");
  document.getElementById("account_terms_title").classList.add("hidden");
  document.getElementById("account_terms").classList.add("hidden");
  document.getElementById("service_terms_modal_title").classList.add("hidden");
  document.getElementById("service_terms_title").classList.add("hidden");
  document.getElementById("service_terms").classList.add("hidden");
  document.querySelector(".agreementModal").classList.add("hidden");
}

function getTerms() {
  getTerm("account_terms");
  getTerm("service_terms");
};

async function getTerm(company) {
  try {
    const term = document.getElementById(company);
    const version = term.classList.contains("en") ? "latest_en" : "latest_ko";
    const resp = await axios.get(
      `${serverUrl}/auth/realms/tmax/agreement/${company}?version=${version}&realmName=tmax`
    );
    term.innerHTML = resp.data;
  } catch (e) {
    console.error(e);
  }
}

function clickEye(e) {
  if (e.classList.contains("activate")) {
    e.classList.remove("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "password";
    }
  } else {
    e.classList.add("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "text";
    }
  }
}
