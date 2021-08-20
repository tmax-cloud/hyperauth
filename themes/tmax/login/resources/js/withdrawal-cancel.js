function goToLoginHome(baseUrl) {
  if (!!baseUrl) {
    location.href = baseUrl;
  } else {
    location.href = "/";
  }
}

function openModal() {
  document.getElementById("button-ok").classList.remove("modal_button_disabled");
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
  // document.getElementById("withdrawal-cancel-form").submit();
}

function cancelWithdrawal() {
  // axios.post(cancelLink).then(() => {
  //   openModal();
  // });
  document.getElementById("button-cancel").classList.add("modal_button_disabled");
  document.getElementById("button-ok").classList.add("modal_button_disabled");
  document.getElementById("withdrawal-cancel-form").submit();
}
