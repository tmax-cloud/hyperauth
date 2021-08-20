this.submitted = false;

function validInput(input, event) {
  event.preventDefault();
  input.classList.remove("has-error");
  let userName = document.getElementById("username").value;
  let password = document.getElementById("password").value;
  if (event.keyCode === 13 && userName != "" && password != "") {
    if (this.submitted) {
      return false;
    } else {
      this.submitted = true;
      document.getElementById("kc-login").disabled = true;
      document.getElementById("kc-form-login").submit();
      return false;
    }
  } else {
    if (userName != "" && password != "") {
      document.getElementById("kc-login").disabled = false;
    } else {
      document.getElementById("kc-login").disabled = true;
    }
  }
}

function removeDuplicatedMessage(summary) {
  const messages = summary.split("&lt;br /&gt;");
  const uniqueMessages = [...new Set(messages)];
  document.getElementById(
    "result-message-section"
  ).innerHTML = uniqueMessages.join("<br />");
}

function submitClick() {
  document.getElementById("kc-login").disabled = true;
  document.getElementById("kc-form-login").submit();
  return false;
}

