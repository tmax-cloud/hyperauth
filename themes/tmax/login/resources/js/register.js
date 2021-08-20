// const url = document.location.origin;
// const serverUrl = "https://172.22.6.11";
const serverUrl = document.location.origin;

let emailVerificationSuccess = false;
// MEMO: 각 filed의 상태관리를 위한 object. validation 미통과이면 false, 통과이면 true.
const validationStates = {
  email: false,
  password: false,
  passwordConfirm: false,
  username: false,
};

// axios response data
const EMAIL_SEND_FAILED = "Email Send Failed";
const EMAIL_WRONG_CODE = "Wrong Verification Code";
const EMAIL_ALREADY_EXISTS = "User Already Exists with the Email";
const EMAIL_TIME_EXPIRED = "email Verification Time Expired";

// error html div IDs
const ERROR_EMAIL_EXIST = "error_email_exist";
const ERROR_EMAIL_FORMAT = "error_email_format";
const ERROR_PASSWORD_EMPTY = "error_password_empty";
const ERROR_PASSWORD_LENGTH = "error_password_length";
const ERROR_PASSWORD_TYPE = "error_password_type";
const ERROR_PASSWORD_CONFIRM_EMPTY = "error_password_confirm_empty";
const ERROR_PASSWORD_CONFIRM_MATCH = "error_password_confirm_match";
const ERROR_USERNAME_EMPTY = "error_username_empty";

// term names
// MEMO : register.ftl의 각 term 부분 id와 동일해야 함
const TERM_ACCOUNT_PRIVACY_DUTY = "account_privacy_duty_terms";
const TERM_ACCOUNT = "account_terms";
const TERM_SERVICE = "service_terms";
const TERM_PRIVACY = "privacy_terms";
const TERM_THIRD_PRIVACY = "third_privacy_terms";
const termList = [
  TERM_ACCOUNT_PRIVACY_DUTY,
  TERM_ACCOUNT,
  TERM_SERVICE,
  TERM_PRIVACY,
  TERM_THIRD_PRIVACY,
];

// -- 회원가입 약관 동의 페이지 (step1) --

// MEMO : 약관 내용들 서버에서 가져오는 부분
function getTerms(language) {
  termList.forEach((termName) => {
    getTerm(termName, language);
  });
}

async function getTerm(termName, language) {
  try {
    const term = document.getElementById(termName);
    const version = `latest_${language}`;
    const resp = await axios.get(
      `${serverUrl}/auth/realms/tmax/agreement/${termName}?version=${version}&realmName=tmax`
    );
    term.innerHTML = resp.data;
  } catch (e) {
    console.error(e);
  }
}

function clickAgreeInput(e) {
  const chkbox_all = document.getElementById("check_all");
  const chkboxes = document.getElementsByName("agree");
  const agree_button = document.getElementById("agree_button");
  if (e === "check_all") {
    for (let i = 0; i < chkboxes.length; ++i) {
      chkboxes[i].checked = chkbox_all.checked;
    }
    if (chkbox_all.checked) {
      agree_button.disabled = false;
    } else {
      agree_button.disabled = true;
    }
  } else {
    const chkbox = document.getElementById(e);
    if (chkbox_all.checked && !chkbox.checked) {
      chkbox_all.checked = false;
    }
    let hasFalse = false;

    // MEMO: chkboxes의 첫번째와 마지막 선택지는 (선택)이여서 chkboxes.length-1 까지만 체크유무 확인
    for (let i = 1; i < chkboxes.length - 1; ++i) {
      if (!chkboxes[i].checked) {
        hasFalse = true;
      }
    }
    if (hasFalse) {
      agree_button.disabled = true;
    } else {
      agree_button.disabled = false;
    }
  }
}

function clickArrow(e) {
  const arrow = document.getElementById(e);
  const term = document.getElementById(e.split("-")[0]);
  if (arrow.classList.contains("up")) {
    arrow.classList.remove("up");
    term.style.display = "none";
  } else {
    arrow.classList.add("up");
    term.style.display = "block";
  }
}

function clickAgreeButton() {
  const step1 = document.getElementById("step1");
  const step2 = document.getElementById("step2");
  step1.style.display = "none";
  step2.style.display = "block";
}

// -- 사용자 정보 입력 페이지 (step2) --

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

function validateEmail() {
  const email = document.getElementById("email");
  const formatReg = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9][a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
  showErrorMessage(ERROR_EMAIL_EXIST, false);
  email.classList.remove("has-error");
  if (!!email.value && email.value != "" && formatReg.test(email.value)) {
    showErrorMessage(ERROR_EMAIL_FORMAT, false);
    email.classList.remove("has-error");
    validationStates.email = true;
    checkAllInputStates();
    return true;
  } else {
    showErrorMessage(ERROR_EMAIL_FORMAT, true);
    email.classList.add("has-error");
    validationStates.email = false;
    checkAllInputStates();
    return false;
  }
}

function validatePassword() {
  const password = document.getElementById("password");
  if (validatePasswordField()) {
    password.classList.add("has-error");
    validationStates.password = false;
    checkAllInputStates();
    return false;
  } else {
    password.classList.remove("has-error");
    validationStates.password = true;
    checkAllInputStates();
    return true;
  }
}

function validatePasswordField() {
  const password = document.getElementById("password");
  const passwordValue = password.value;
  const passwordReg = /^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\d!@#$%^&*()<>?]{1,}$/;
  let hasError = false;
  if (!!password.value && passwordValue.value != "") {
    showErrorMessage(ERROR_PASSWORD_EMPTY, false);
    // MEMO: 비밀번호 길이 체크
    if (!/^.{9,20}$/.test(passwordValue)) {
      showErrorMessage(ERROR_PASSWORD_LENGTH, true);
      hasError = true;
    } else {
      showErrorMessage(ERROR_PASSWORD_LENGTH, false);
    }

    // MEMO: 대문자, 소문자, 숫자, 특수문자 중 최소 3개 충족 체크
    if (passwordReg.test(passwordValue)) {
      showErrorMessage(ERROR_PASSWORD_TYPE, false);
    } else {
      showErrorMessage(ERROR_PASSWORD_TYPE, true);
      hasError = true;
    }
  } else {
    showErrorMessage(ERROR_PASSWORD_EMPTY, true);
    hasError = true;
  }
  return hasError;
}

function validatePasswordConfirm() {
  const passwordConfirm = document.getElementById("password-confirm");
  if (validatePasswordConfirmField()) {
    passwordConfirm.classList.add("has-error");
    validationStates.passwordConfirm = false;
    checkAllInputStates();
    return false;
  } else {
    passwordConfirm.classList.remove("has-error");
    validationStates.passwordConfirm = true;
    checkAllInputStates();
    return true;
  }
}

function validatePasswordConfirmField() {
  const password = document.getElementById("password");
  const passwordConfirm = document.getElementById("password-confirm");
  let hasError = false;
  if (!!passwordConfirm.value && passwordConfirm.value != "") {
    showErrorMessage(ERROR_PASSWORD_CONFIRM_EMPTY, false);
    if (password.value === passwordConfirm.value) {
      showErrorMessage(ERROR_PASSWORD_CONFIRM_MATCH, false);
    } else {
      showErrorMessage(ERROR_PASSWORD_CONFIRM_MATCH, true);
      hasError = true;
    }
  } else {
    showErrorMessage(ERROR_PASSWORD_CONFIRM_EMPTY, true);
    hasError = true;
  }
  return hasError;
}

function validateUserName() {
  const username = document.getElementById("user.attributes.user_name");
  if (!!username.value && username.value != "") {
    username.classList.remove("has-error");
    showErrorMessage(ERROR_USERNAME_EMPTY, false);
    validationStates.username = true;
    checkAllInputStates();
    return true;
  } else {
    username.classList.add("has-error");
    showErrorMessage(ERROR_USERNAME_EMPTY, true);
    validationStates.username = false;
    checkAllInputStates();
    return false;
  }
}

// MEMO : input이 공백일 땐 에러문구 제거하는 함수
function removeEmailValidationErrors() {
  const email = document.getElementById("email");
  if (email.value === "") {
    email.classList.remove("has-error");
    showErrorMessage(ERROR_EMAIL_EXIST, false);
    showErrorMessage(ERROR_EMAIL_FORMAT, false);
  } else {
    validateEmail();
  }
}

function removePwdValidationErrors() {
  const password = document.getElementById("password");
  if (password.value === "") {
    password.classList.remove("has-error");
    showErrorMessage(ERROR_PASSWORD_EMPTY, false);
    showErrorMessage(ERROR_PASSWORD_LENGTH, false);
    showErrorMessage(ERROR_PASSWORD_TYPE, false);
  } else {
    validatePassword();
  }
}

function removePwdConfirmValidationErrors() {
  const passwordConfirm = document.getElementById("password-confirm");
  if (passwordConfirm.value === "") {
    passwordConfirm.classList.remove("has-error");
    showErrorMessage(ERROR_PASSWORD_CONFIRM_EMPTY, false);
    showErrorMessage(ERROR_PASSWORD_CONFIRM_MATCH, false);
  } else {
    validatePasswordConfirm();
  }
}

function removeUsernameValidationErrors() {
  const username = document.getElementById("user.attributes.user_name");
  if (username.value === "") {
    username.classList.remove("has-error");
    showErrorMessage(ERROR_USERNAME_EMPTY, false);
  } else {
    validateUserName();
  }
}

function showErrorMessage(errorId, visible) {
  const error = document.getElementById(errorId);
  error.style.display = visible ? "block" : "none";
}

function checkAllInputStates() {
  const registerButton = document.getElementById("register_button");
  for (let value of Object.values(validationStates)) {
    if (!value) {
      registerButton.disabled = true;
      return;
    }
  }

  registerButton.disabled = false;
}

// MEMO : 계정존재유무에 대한 UI validation을 위해 api로 체크
function validateEmailExists() {
  const userEmail = document.getElementById("email").value;
  const valid = axios
    .get(`${serverUrl}/auth/realms/tmax/user/${userEmail}/exists`)
    .then((resp) => {
      // MEMO : 계정 이미 존재하는 경우
      if (resp.data) {
        showErrorMessage(ERROR_EMAIL_EXIST, true);
        email.classList.add("has-error");
        return false;
      } else {
        return true;
      }
    })
    .catch((error) => {
      return true;
    });

  return valid;
}

function validateInputFields() {
  // MEMO : 회원가입 약관 optional 항목들에 대해서 체크유무에 따라 user attribute 값을 추가하기 위해 작성한 부분
  const checked = document.getElementById("check_7").checked ? true : false;
  const agreeMailOpt = document.createElement("input");
  agreeMailOpt.setAttribute("type", "hidden");
  agreeMailOpt.setAttribute("id", "user.attributes.agreeMailOpt");
  agreeMailOpt.setAttribute("name", "user.attributes.agreeMailOpt");
  agreeMailOpt.setAttribute("value", checked);
  document.getElementById("kc-register-form").appendChild(agreeMailOpt);

  const overAge14 = document.getElementById("check_1");
  if (!overAge14.checked) {
    const under_14 = document.createElement("input");
    under_14.setAttribute("type", "hidden");
    under_14.setAttribute("id", "user.attributes.under_14");
    under_14.setAttribute("name", "user.attributes.under_14");
    under_14.setAttribute("value", true);
    document.getElementById("kc-register-form").appendChild(under_14);
  }

  validateEmailExists().then((result) => {
    if (
      result &&
      validateEmail() &&
      validatePassword() &&
      validatePasswordConfirm() &&
      validateUserName()
    ) {
      document.getElementById("kc-register-form").submit();
      document.getElementById("register_button").disabled = true;
    } else {
      return false;
    }
  });
}

function clickBackButtonOfRegister() {
  // MEMO : 한 페이지 안에서 step1,2가 나뉘어서 뒤로가기버튼 동작을 페이지 상태에 따라 수동으로 처리함
  const step1 = document.getElementById("step1");
  const step2 = document.getElementById("step2");
  if (step1.style.display == "block" && step2.style.display == "none") {
    window.history.back();
  } else if (step1.style.display == "none" && step2.style.display == "block") {
    step1.style.display = "block";
    step2.style.display = "none";
  }
}
