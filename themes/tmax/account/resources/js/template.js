function closeAlertModal() {
  document.getElementById("alertModal").classList.add("hidden");
  location.href = document.location.href.replace("/console/", "/account/").replace("/emailOtp", "");
}

function closeCancelModal() {
  document.getElementById("cancelModal").classList.add("hidden");
}
