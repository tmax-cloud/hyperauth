var url = document.location.origin;
// var serverUrl = "https://172.22.6.11";
var serverUrl = document.location.origin;
var isMailSent = false;

// forgot password page
function idChange(send_email) {
  const error_wrong_email = document.getElementById("error_wrong_email");
  const email_class = document.getElementById("email").classList;
  const error_none_email = document.getElementById("error_none_email");
  const error_empty_email = document.getElementById("error_empty_email");
  error_wrong_email.style.display = "none";
  error_none_email.style.display = "none";
  error_empty_email.style.display = "none";
  email_class.contains("has-error") && email_class.remove("has-error");
  setTimeout(() => {
    const id_input = document.getElementById("email");
    const email_button = document.getElementById(send_email);
    if (!isMailSent) {
      if (id_input.value !== "") {
        email_button.disabled = false;
      } else {
        email_button.disabled = true;
      }
    }
  }, 300);
}

async function postVerifiactionEmail(email) {
  const email_button = document.getElementById("send_email");
  const error_wrong_email = document.getElementById("error_wrong_email");
  const email_class = document.getElementById("email").classList;
  const formatReg = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9][a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
  if (!!email && email != "" && formatReg.test(email)) {
    error_wrong_email.style.display = "none";

    // 서버에러 표현 dom
    const error_none_email = document.getElementById("error_none_email");
    const error_register_by_sns = document.getElementById(
      "error_register_by_sns"
    );
    const error_empty_email = document.getElementById("error_empty_email");

    email_class.contains("has-error") && email_class.remove("has-error");
    try {
      error_none_email.style.display = "none";
      error_register_by_sns.style.display = "none";
      error_empty_email.style.display = "none";

      email_class.contains("has-error") && email_class.remove("has-error");
      email_button.disabled = true;
      isMailSent = true;
      await axios
        .post(`${serverUrl}/auth/realms/tmax/email/${email}?resetPassword=t`)
        .catch((err) => {
          if (err.response.data === "Federated Identity User") {
            error_register_by_sns.style.display = "block";
            email_class.contains("has-error") || email_class.add("has-error");
          } else if (err.response.data === "No Corresponding User") {
            error_none_email.style.display = "block";
            email_class.contains("has-error") || email_class.add("has-error");
          } else if (err.response.data === "Email Not Found") {
            error_empty_email.style.display = "block";
            email_class.contains("has-error") || email_class.add("has-error");
          }
          // catch로 이동하기 위한 에러 발생
          throw new Error(err.response.data);
        });
      const forgot_password = document.getElementById("forgot_password");
      const verify_password = document.getElementById("verify_password");
      forgot_password.style.display = "none";
      verify_password.style.display = "block";
    } catch (e) {
      console.error(e);
    } finally {
      email_button.disabled = false;
    }
  } else {
    error_wrong_email.style.display = "block";
    email_class.contains("has-error") || email_class.add("has-error");
  }
}

async function clickSendEmailButton(email) {
  const id_input = document.getElementById(email);
  await postVerifiactionEmail(id_input.value);
}

async function clickResendEmailButton(email) {
  document.getElementById("resend-button").classList.add("isDisabled");
  document.getElementById("verification_input").value = "";
  const verficiation_input = document.getElementById("verification_input");
  const verfication_class = verficiation_input.classList;
  const error_wrong_code = document.getElementById("error_wrong_code");
  verfication_class.contains("has-error") &&
    verfication_class.remove("has-error");
  error_wrong_code.style.display = "none";
  await clickSendEmailButton(email);
  openModal();
  document.getElementById("resend-button").classList.remove("isDisabled");
}

// verify password page
function verificationChange(verification_confirm) {
  const verficiation_input = document.getElementById("verification_input");
  const verfication_class = verficiation_input.classList;
  const error_wrong_code = document.getElementById("error_wrong_code");
  verfication_class.contains("has-error") &&
    verfication_class.remove("has-error");
  error_wrong_code.style.display = "none";
  setTimeout(() => {
    const verficiation_input = document.getElementById("verification_input");
    const confirm_button = document.getElementById(verification_confirm);
    if (verficiation_input.value !== "") {
      confirm_button.disabled = false;
    } else {
      confirm_button.disabled = true;
    }
  }, 300);
}

async function clickVerficiationConfirmButton() {
  const verficiation_input = document.getElementById("verification_input");
  const confirm_button = document.getElementById("verification_confirm");
  const verfication_class = verficiation_input.classList;
  const error_wrong_code = document.getElementById("error_wrong_code");
  error_wrong_code.style.display = "none";
  verfication_class.contains("has-error") &&
    verfication_class.remove("has-error");
  try {
    confirm_button.disabled = true;
    const id_input = document.getElementById("email");
    const resp = await axios.get(
      `${serverUrl}/auth/realms/tmax/email/${id_input.value}?code=${verficiation_input.value}&resetPassword=t`
    );
    confirm_button.disabled = false;
    const verify_password = document.getElementById("verify_password");
    const update_password = document.getElementById("update_password");
    verify_password.style.display = "none";
    update_password.style.display = "block";
  } catch (e) {
    confirm_button.disabled = false;
    error_wrong_code.style.display = "block";
    verfication_class.contains("has-error") ||
      verfication_class.add("has-error");
    console.error(e);
  }
}

const modal = document.querySelector(".modal");

function openModal() {
  modal.classList.remove("hidden");
}

function closeModal() {
  modal.classList.add("hidden");
}

// update password page
function passwordChange() {
  const password_input = document.getElementById("password");
  const password_confirm_input = document.getElementById("password_confirm");
  const save_button = document.getElementById("save_password");

  if (password_input.value !== "" && password_confirm_input.value !== "") {
    save_button.disabled = false;
  } else {
    save_button.disabled = true;
  }
}

function passwordRemoveValidation() {
  const none = document.getElementById("error_none_password");
  const leng = document.getElementById("error_length_password");
  const wrong = document.getElementById("error_wrong_password");
  const same = document.getElementById("error_sameAsBefore_password");
  none.style.display = "none";
  leng.style.display = "none";
  wrong.style.display = "none";
  same.style.display = "none";
}

function passwordValidation() {
  const pwd = document.getElementById("password");
  const pwdValue = pwd.value;
  const pwd_class = pwd.classList;
  // const num = pwdValue.search(/[0-9]/g);
  // const eng = pwdValue.search(/[A-z]/g);
  // const spe = pwdValue.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
  const passwordReg = /^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\d!@#$%^&*()<>?]{1,}$/;
  pwd_class.contains("has-error") && pwd_class.remove("has-error");
  passwordRemoveValidation();
  if (pwdValue === "") {
    const none = document.getElementById("error_none_password");
    none.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
  } else if (pwdValue.length < 9 || pwdValue.length > 20) {
    const leng = document.getElementById("error_length_password");
    leng.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
  } else if (!passwordReg.test(pwdValue)) {
    const wrong = document.getElementById("error_wrong_password");
    wrong.style.display = "block";
    pwd_class.contains("has-error") || pwd_class.add("has-error");
  }
}

function passwordConfirmRemoveValidation() {
  const none = document.getElementById("error_none_password_confirm");
  const wrong = document.getElementById("error_wrong_password_confirm");
  none.style.display = "none";
  wrong.style.display = "none";
}

function passwordConfirmValidation() {
  const pwdValue = document.getElementById("password").value;
  const pwdConfirm = document.getElementById("password_confirm");
  const pwdConfirmValue = pwdConfirm.value;
  const pwdConfirm_class = pwdConfirm.classList;

  pwdConfirm_class.contains("has-error") &&
    pwdConfirm_class.remove("has-error");
  passwordConfirmRemoveValidation();
  if (pwdConfirmValue === "") {
    const none = document.getElementById("error_none_password_confirm");
    none.style.display = "block";
    pwdConfirm_class.contains("has-error") || pwdConfirm_class.add("has-error");
  } else if (pwdValue !== pwdConfirmValue) {
    const wrong = document.getElementById("error_wrong_password_confirm");
    wrong.style.display = "block";
    pwdConfirm_class.contains("has-error") || pwdConfirm_class.add("has-error");
  }
}

async function clickPasswordSaveButton() {
  const password_input = document.getElementById("password");
  const password_confirm_input = document.getElementById("password_confirm");
  const save_button = document.getElementById("save_password");
  const verficiation_input = document.getElementById("verification_input");
  // const formData = new FormData();
  // formData.append("password", password_input.value);
  // formData.append("confirmPassword", password_confirm_input.value);
  const formData = new URLSearchParams();
  formData.append("password", password_input.value);
  formData.append("confirmPassword", password_confirm_input.value);
  try {
    save_button.disabled = true;
    const id_input = document.getElementById("email");
    const options = {
      method: "PUT",
      data: formData,
      params: {
        code: verficiation_input.value,
        email: id_input.value,
      },
      url: `${serverUrl}/auth/realms/tmax/password`,
    };
    // const resp = await axios.put(
    //   `${serverUrl}/auth/realms/tmax/password`,
    //   formData,
    //   {
    //     params: {
    //       code: verficiation_input.value,
    //       email: id_input.value,
    //     },
    //   }
    // );
    await axios(options).catch((err) => {
      if (err.response.data === "sameWithOldPassword") {
        const pwd = document.getElementById("password");
        const pwd_class = pwd.classList;
        passwordRemoveValidation();
        const same = document.getElementById("error_sameAsBefore_password");
        same.style.display = "block";
        pwd_class.contains("has-error") || pwd_class.add("has-error");
        throw new Error(
          "It is the same as your current password. Please set a new password."
        );
      }
    });
    save_button.disabled = false;
    const update_password = document.getElementById("update_password");
    const reset_password_complete = document.getElementById(
      "reset_password_complete"
    );
    update_password.style.display = "none";
    reset_password_complete.style.display = "block";
  } catch (e) {
    save_button.disabled = false;
    console.error(e);
  }
}
