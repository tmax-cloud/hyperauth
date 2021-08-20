const serverUrl = document.location.origin;

const getAccessToken = function () {
  return sessionStorage.getItem("accessToken");
};

function clickEye(e) {
  if (e.classList.contains("activate")) {
    e.classList.remove("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "password";
    } else if (e.id === "eye-password-new") {
      document.getElementById("password-new").type = "password";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "password";
    }
  } else {
    e.classList.add("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "text";
    } else if (e.id === "eye-password-new") {
      document.getElementById("password-new").type = "text";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "text";
    }
  }
}

const validationPasswordStates = {
  password: false,
  passwordNew: false,
  passwordConfirm: false,
};

function showErrorMessage(errorId, visible) {
  const error = document.getElementById(errorId);
  error.style.display = visible ? "block" : "none";
}

function checkPasswordInputStates() {
  const registerButton = document.getElementById("password-save-button");
  for (let value of Object.values(validationPasswordStates)) {
    if (!value) {
      registerButton.disabled = true;
      return;
    }
  }
  registerButton.disabled = false;
}

function validatePassword() {
  const password = document.getElementById("password");
  showErrorMessage("error_password_wrong", false);

  if (isEmptyPasswordField()) {
    password.classList.add("has_error");
    validationPasswordStates.password = false;
    checkPasswordInputStates();
    return false;
  } else {
    password.classList.remove("has_error");
    validationPasswordStates.password = true;
    checkPasswordInputStates();
    return true;
  }
}
function isEmptyPasswordField() {
  const password = document.getElementById("password");
  const passwordValue = password.value;
  // let count = 0;
  let hasError = false;
  if (!!password.value && passwordValue.value != "") {
    showErrorMessage("error_old_password_empty", false);
  } else {
    showErrorMessage("error_old_password_empty", true);
    hasError = true;
  }
  return hasError;
}

function validatePasswordNew() {
  const passwordNew = document.getElementById("password-new");
  if (validatePasswordField()) {
    passwordNew.classList.add("has_error");
    validationPasswordStates.passwordNew = false;
    checkPasswordInputStates();
    return false;
  } else {
    passwordNew.classList.remove("has_error");
    validationPasswordStates.passwordNew = true;
    checkPasswordInputStates();
    return true;
  }
}

/*
function validatePassword() {
  const password = document.getElementById("password");
  if (validatePasswordField()) {
    password.classList.add("has-error");
    validationPasswordStates.password = false;
    checkPasswordInputStates();
    return false;
  } else {
    password.classList.remove("has-error");
    validationPasswordStates.password = true;
    checkPasswordInputStates();
    return true;
  }
}
*/
function validatePasswordField() {
  const password = document.getElementById("password-new");
  const passwordValue = password.value;
  // const reg = [/[0-9]/, /[a-z]/, /[A-Z]/, /[~!@#$%^&*()_+|<>?:{}]/];
  const passwordReg = /^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\d!@#$%^&*()<>?]{1,}$/;
  // let count = 0;
  let hasError = false;
  if (!!password.value && passwordValue.value != "") {
    showErrorMessage("error_password_empty", false);
    // MEMO: 비밀번호 길이 체크
    if (!/^.{9,20}$/.test(passwordValue)) {
      showErrorMessage("error_password_length", true);
      hasError = true;
    } else {
      showErrorMessage("error_password_length", false);
    }
    // MEMO: 대문자, 소문자, 숫자, 특수문자 모두 충족 체크
    // for (let i = 0; i < reg.length; i++) {
    //   if (reg[i].test(passwordValue)) {
    //     count++;
    //   }
    //   if (count >= 4) {
    //     showErrorMessage("error_password_type", false);
    //   }
    // }
    // if (count < 4) {
    //   showErrorMessage("error_password_type", true);
    //   hasError = true;
    // }

    // MEMO: 대문자, 소문자, 숫자, 특수문자 중 최소 3개 충족 체크
    if (passwordReg.test(passwordValue)) {
      showErrorMessage("error_password_type", false);
    } else {
      showErrorMessage("error_password_type", true);
      hasError = true;
    }
  } else {
    showErrorMessage("error_password_empty", true);
    hasError = true;
  }
  return hasError;
}

function validatePasswordConfirm() {
  const password = document.getElementById("password-confirm");
  if (validatePasswordConfirmField()) {
    password.classList.add("has_error");
    validationPasswordStates.passwordConfirm = false;
    checkPasswordInputStates();
    return false;
  } else {
    password.classList.remove("has_error");
    validationPasswordStates.passwordConfirm = true;
    checkPasswordInputStates();
    return true;
  }
}

function validatePasswordConfirmField() {
  const passwordConfirm = document.getElementById("password-confirm");
  const password = document.getElementById("password-new");
  const passwordConfirmValue = passwordConfirm.value;
  const passwordValue = password.value;
  // const reg = [/[0-9]/, /[a-z]/, /[A-Z]/, /[~!@#$%^&*()_+|<>?:{}]/];
  const passwordReg = /^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\d!@#$%^&*()<>?]{1,}$/;
  // let count = 0;
  let hasError = false;
  if (!!passwordConfirm.value && passwordConfirmValue.value != "") {
    showErrorMessage("error_password_confirm_empty", false);
    // MEMO: 비밀번호 길이 체크

    if (!(passwordConfirmValue == passwordValue)) {
      showErrorMessage("error_password_confirm_unmatch", true);
      hasError = true;
    } else {
      showErrorMessage("error_password_confirm_unmatch", false);
    }
  } else {
    showErrorMessage("error_password_confirm_empty", true);
    hasError = true;
  }
  return hasError;
}

async function submitWithdrawal() {
  const withdrawalForm = document.getElementById("withdrawal-form");
  const withdrawalUrl = withdrawalForm.getAttribute("action");
  const stateChecker = withdrawalForm.getAttribute("stateChecker");

  let withForm = new FormData();
  withForm.append("stateChecker", stateChecker);
  let rejectService = "";

  //const sessionEmail = sessionStorage.getItem('email');

  const email = withdrawalForm.getAttribute("email");
  const password = withdrawalForm.getAttribute("password");

  //alert(sessionEmail);
  let passwordResult = false;
  let withdrawalResult = false;

  try {
    const withdrawalResp = await axios.post(withdrawalUrl, withForm, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    rejectService = withdrawalResp.data;
    withdrawalResult = true;
  } catch (e) {
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
    MSG.innerHTML = rejectService + "의 " + MSG.innerHTML;
    document.getElementById("withdrawal-step2").classList.add("hidden");
    document.getElementById("withdrawal-failure").classList.remove("hidden");
    return withdrawalResult;
  }
}

async function submitPassword() {
  const passwordForm = document.getElementById("password-form");
  const password = document.getElementById("password").value;
  const email = document.getElementById("username").value;

  let passwordResult = false;

  try {
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
    validationPasswordStates.password = false;
    document.getElementById("password").classList.add("has_error");
    checkPasswordInputStates();
    return passwordResult;
  }

  passwordForm.submit();
}

function openCancelModal() {
  document.getElementById("cancelModal").classList.remove("hidden");
}

function cancelChangePassword() {
  location.href = document.location;
}
