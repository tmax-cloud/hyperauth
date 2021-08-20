function setButtonDisabled() {
  const save_button = document.getElementById("save_password");
  save_button.disabled = true;
}

function passwordRemoveValidation() {
  const wrapperClassList = document.getElementById("password_wrapper")
    .classList;
  const inputClassList = document.getElementById("password").classList;
  const none = document.getElementById("error_none_password");
  const leng = document.getElementById("error_length_password");
  const wrong = document.getElementById("error_wrong_password");
  const exist = document.getElementById("error_exist_password_confirm");
  none.style.display = "none";
  leng.style.display = "none";
  wrong.style.display = "none";
  if (!!exist) {
    exist.style.display = "none";
  }
  inputClassList.contains("has-error") && inputClassList.remove("has-error");
  wrapperClassList.add("marginBottom");
}

function passwordValidation() {
  const wrapperClassList = document.getElementById("password_wrapper")
    .classList;
  const pwd = document.getElementById("password");
  const pwdValue = pwd.value;
  const pwd_class = pwd.classList;
  const passwordReg = /^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\d!@#$%^&*()<>?]{1,}$/;
  pwd_class.contains("has-error") && pwd_class.remove("has-error");
  passwordRemoveValidation();
  if (pwdValue === "") {
    const none = document.getElementById("error_none_password");
    none.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
    wrapperClassList.contains("marginBottom") &&
      wrapperClassList.remove("marginBottom");
  } else if (pwdValue.length < 9 || pwdValue.length > 20) {
    const leng = document.getElementById("error_length_password");
    leng.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
    wrapperClassList.contains("marginBottom") &&
      wrapperClassList.remove("marginBottom");
    return false;
  } else if (!passwordReg.test(pwdValue)) {
    const wrong = document.getElementById("error_wrong_password");
    wrong.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
    wrapperClassList.contains("marginBottom") &&
      wrapperClassList.remove("marginBottom");
    return false;
  }
  return true;
}

function passwordConfirmRemoveValidation() {
  const wrapperClassList = document.getElementById("confirm_password_wrapper")
    .classList;
  const inputClassList = document.getElementById("confirmPassword").classList;
  const none = document.getElementById("error_none_password_confirm");
  const wrong = document.getElementById("error_wrong_password_confirm");
  const exist = document.getElementById("error_exist_password_confirm");
  none.style.display = "none";
  wrong.style.display = "none";
  if (!!exist) {
    exist.style.display = "none";
  }
  inputClassList.contains("has-error") && inputClassList.remove("has-error");
  wrapperClassList.add("marginBottom");
}

function passwordConfirmValidation() {
  const wrapperClassList = document.getElementById("confirm_password_wrapper")
    .classList;
  const pwdValue = document.getElementById("password").value;
  const pwdConfirm = document.getElementById("confirmPassword");
  const pwdConfirmValue = pwdConfirm.value;
  const pwdConfirm_class = pwdConfirm.classList;

  pwdConfirm_class.contains("has-error") &&
    pwdConfirm_class.remove("has-error");
  passwordConfirmRemoveValidation();
  if (pwdConfirmValue === "") {
    const none = document.getElementById("error_none_password_confirm");
    none.style.display = "block";
    pwdConfirm_class.contains("has-error") || pwdConfirm_class.add("has-error");
    wrapperClassList.contains("marginBottom") &&
      wrapperClassList.remove("marginBottom");
    return false;
  } else if (pwdValue !== pwdConfirmValue) {
    const wrong = document.getElementById("error_wrong_password_confirm");
    wrong.style.display = "block";
    pwdConfirm_class.contains("has-error") || pwdConfirm_class.add("has-error");
    wrapperClassList.contains("marginBottom") &&
      wrapperClassList.remove("marginBottom");
    return false;
  }
  return true;
}

function passwordChange(inputId) {
  const save_button = document.getElementById("save_password");

  if (passwordValidation() && passwordConfirmValidation()) {
    save_button.disabled = false;
  } else {
    save_button.disabled = true;
  }
}
