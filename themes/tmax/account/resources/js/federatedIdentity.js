(function lableChange() {
  const div = document.querySelector(".label");
  const label = document.querySelector(".control-label");
  const idforDiv = label.innerHTML.trim();
  div.setAttribute("id", idforDiv);
  label.innerHTML = "";
})();


function openCheckModal() {
  document.getElementById("checkModal").classList.remove("hidden");
}

function closeCheckModal() {
  document.getElementById("checkModal").classList.add("hidden");
}

function submitUnSNS() {
  const unSNSForm = document.getElementById("SNS-form");
  unSNSForm.submit();
}
