const contactInfo = document.querySelectorAll(".contact-info");
contactInfo.forEach( i => {
    i.style.display = "none"
})

function openWithdrawalPage() {
    document.getElementById("account-update").classList.add("hidden");
    document.getElementById("withdrawal-step2").classList.remove("hidden");
}


