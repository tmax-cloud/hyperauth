//hyperauth theme do not have agreement page
const step1 = document.getElementById("step1");
const step2 = document.getElementById("step2");
step1.style.display = "none";
step2.style.display = "block";


// agreeMailOpt, under_14 Register Call에서 빼버리기
function validateInputFields() {
    validateEmailExists().then((result) => {
        if (
            result &&
            validateEmail() &&
            validatePassword() &&
            validatePasswordConfirm() &&
            validateUserName()
        ) {
            document.getElementById("kc-register-form").submit();
            document.getElementById("register_button").disabled = true;
        } else {
            return false;
        }
    });
}