function checkNextButtonOfNewAccountPage() {
  const identityStep1NewAccount = document.getElementById(
      "identity-step1-newAccount"
  );
  const step3 = document.getElementById("identity-step3");
  identityStep1NewAccount.style.display = "none";
  step3.style.display = "block";
}

// agreeMailOpt, under_14 Register Call에서 빼버리기
function sendVerificationEmailForNewAccount() {
  const idpForm = document.getElementById("kc-identity-provider-form");
  const userName = document.createElement("input");
  userName.setAttribute("type", "hidden");
  userName.setAttribute("name", "user_name");
  userName.setAttribute(
      "value",
      document.getElementById("user.attributes.user_name").value
  );

  idpForm.appendChild(userName);
  document.getElementById(
      "sendVerificationEmailForNewAccountButton"
  ).disabled = true;
  idpForm.submit();
}