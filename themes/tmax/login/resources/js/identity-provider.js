var idpServerUrl = document.location.origin;

var IDP_TERM_ACCOUNT_PRIVACY_DUTY = "account_privacy_duty_terms";
var IDP_TERM_ACCOUNT = "account_terms";
var IDP_TERM_SERVICE = "service_terms";
var IDP_TERM_PRIVACY = "privacy_terms";
var IDP_TERM_THIRD_PRIVACY = "third_privacy_terms";
var idpTermList = [
  IDP_TERM_ACCOUNT_PRIVACY_DUTY,
  IDP_TERM_ACCOUNT,
  IDP_TERM_SERVICE,
  IDP_TERM_PRIVACY,
  IDP_TERM_THIRD_PRIVACY,
];

function showErrorMessage(errorId, visible) {
  const error = document.getElementById(errorId);
  error.style.display = visible ? "block" : "none";
}

function validateEmail(input, event) {
  input.classList.remove("has-error");
  const emailInput = document.getElementById("email");
  const nextButton = document.getElementById("next-button");
  const formatReg = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9][a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
  if (
    event.keyCode === 13 &&
    !!emailInput.value &&
    emailInput.value != "" &&
    formatReg.test(emailInput.value)
  ) {
    checkEmailExist();
  } else {
    if (
      !!emailInput.value &&
      emailInput.value != "" &&
      formatReg.test(emailInput.value)
    ) {
      showErrorMessage("error_email_format", false);
      emailInput.classList.remove("has-error");
      nextButton.disabled = false;
    } else {
      showErrorMessage("error_email_format", true);
      emailInput.classList.add("has-error");
      nextButton.disabled = true;
    }
  }
}

async function checkEmailExist() {
  const emailInput = document.getElementById("email");
  const email = !!emailInput.value ? emailInput.value : "";
  window.sessionStorage.setItem("idp-user-email", email);
  document.getElementById("email-address").innerText = email;
  document.getElementById("email-for-new-account").value = email;
  try {
    const res = await axios.get(
      `${idpServerUrl}/auth/realms/tmax/user/${email}/exists`
    );
    if (res.data) {
      // MEMO : auth계정이 존재하면 바로 submit -> auth계정과 외부계정 연동페이지로 감
      document.getElementById("kc-identity-provider-form").submit();
    } else {
      // MEMO : auth계정 없으면 auth계정생성하는 다음 단계로 이동
      const step1 = document.getElementById("identity-step1");
      const step2 = document.getElementById("identity-step1-newAccount");
      step1.style.display = "none";
      step2.style.display = "block";
    }
  } catch (error) {
    const step1 = document.getElementById("identity-step1");
    const step2 = document.getElementById("identity-step1-newAccount");
    step1.style.display = "none";
    step2.style.display = "block";
  }
}

function checkNextButtonOfNewAccountPage() {
  const identityStep1NewAccount = document.getElementById(
    "identity-step1-newAccount"
  );
  const step2 = document.getElementById("identity-step2");
  identityStep1NewAccount.style.display = "none";
  step2.style.display = "block";
  getTerms();
}

function getTerms() {
  const language = window.sessionStorage.getItem("language") || "ko";
  idpTermList.forEach((termName) => {
    getTerm(termName, language);
  });
}

async function getTerm(termName, language) {
  try {
    const term = document.getElementById(termName);
    const version = `latest_${language}`;
    const resp = await axios.get(
      `${idpServerUrl}/auth/realms/tmax/agreement/${termName}?version=${version}&realmName=tmax`
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

function validateUserName() {
  const username = document.getElementById("user.attributes.user_name");
  if (!!username.value && username.value != "") {
    username.classList.remove("has-error");
    showErrorMessage("error_username_empty", false);
    document.getElementById(
      "sendVerificationEmailForNewAccountButton"
    ).disabled = false;
    return true;
  } else {
    username.classList.add("has-error");
    showErrorMessage("error_username_empty", true);
    document.getElementById(
      "sendVerificationEmailForNewAccountButton"
    ).disabled = true;
    return false;
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

function clickAgreeBottomButton() {
  const step2 = document.getElementById("identity-step2");
  const step3 = document.getElementById("identity-step3");
  step2.style.display = "none";
  step3.style.display = "block";
}

function sendVerificationEmailForNewAccount() {
  const idpForm = document.getElementById("kc-identity-provider-form");
  const userName = document.createElement("input");
  userName.setAttribute("type", "hidden");
  userName.setAttribute("name", "user_name");
  userName.setAttribute(
    "value",
    document.getElementById("user.attributes.user_name").value
  );

  const overAge14 = document.getElementById("check_1");
  if (!overAge14.checked) {
    const under_14 = document.createElement("input");
    under_14.setAttribute("type", "hidden");
    under_14.setAttribute("id", "under_14");
    under_14.setAttribute("name", "under_14");
    under_14.setAttribute("value", true);
    idpForm.appendChild(under_14);
  }

  const agreeMailOpt = document.createElement("input");
  const checked = document.getElementById("check_7").checked ? "true" : "false";
  agreeMailOpt.setAttribute("type", "hidden");
  agreeMailOpt.setAttribute("name", "agreeMailOpt");
  agreeMailOpt.setAttribute("value", checked);
  idpForm.appendChild(userName);
  idpForm.appendChild(agreeMailOpt);
  document.getElementById(
    "sendVerificationEmailForNewAccountButton"
  ).disabled = true;
  idpForm.submit();
}
