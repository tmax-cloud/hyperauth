this.isResendButtonClicked = false;

function openModal() {
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
  // 현재 OTP부분은 페이지 refresh시키면 메일 재전송 돼서 또 reload 해줄 필요 없어서 이부분 주석처리함
  // location.reload();
}

function openTimeExpiredModal() {
  document.querySelector(".time_expired_modal").classList.remove("hidden");
}

function closeTimeExpiredModal() {
  document.querySelector(".time_expired_modal").classList.add("hidden");
}

function clickResendEmailButton(resendLink) {
  this.isResendButtonClicked = true;
  document.getElementById("resend-link").classList.add("isDisabled");
  axios.post(resendLink).then(() => {
    openModal();
    document.getElementById("error-section").classList.remove("alert-error");
    document.getElementById("error-section").innerText = "";
    document.getElementById("resend-link").classList.remove("isDisabled");
    restartTimer(this.timeLimit);
  });
}

function startTimer(expiredTime, fixedValidTimeLimit) {
  this.timeLimit = fixedValidTimeLimit; //정해진시간
  var time =
    Math.floor(expiredTime / 1000) - Math.floor(new Date().getTime() / 1000);
  if (time > 0) {
    var min = "";
    var sec = "";
    var timerContainer = document.getElementById("timer-input-container");
    var x = setInterval(function () {
      if (this.isResendButtonClicked) {
        clearInterval(x);
      } else {
        min = parseInt(time / 60);
        sec = time % 60;
        if (sec.toString().length == 1) {
          sec = "0" + sec;
        }
        timerContainer.setAttribute("data-value", min + ":" + sec);
        time--;
        if (time < 0) {
          clearInterval(x);
          // document.getElementById("error-section").classList.add("alert-error");
          // document.getElementById("error-section").innerText =
          //   "시간이 만료되었습니다.";
          openTimeExpiredModal();
          document.getElementById("login-otp").disabled = true;
        }
      }
    }, 1000);
  } else {
    var timerContainer = document.getElementById("timer-input-container");
    timerContainer.setAttribute("data-value", "00:00");
    // document.getElementById("error-section").classList.add("alert-error");
    // document.getElementById("error-section").innerText =
    //   "시간이 만료되었습니다.";
    openTimeExpiredModal();
    document.getElementById("login-otp").disabled = true;
  }
}

function restartTimer(time) {
  var time = time;
  var min = "";
  var sec = "";
  var timerContainer = document.getElementById("timer-input-container");
  var x = setInterval(function () {
    min = parseInt(time / 60);
    sec = time % 60;
    if (sec.toString().length == 1) {
      sec = "0" + sec;
    }
    timerContainer.setAttribute("data-value", min + ":" + sec);
    time--;
    if (time < 0) {
      clearInterval(x);
      // document.getElementById("error-section").classList.add("alert-error");
      // document.getElementById("error-section").innerText =
      //   "시간이 만료되었습니다.";
      openTimeExpiredModal();
      document.getElementById("login-otp").disabled = true;
    }
  }, 1000);
}
