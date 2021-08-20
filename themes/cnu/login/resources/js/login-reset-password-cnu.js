async function postVerifiactionEmail(email) {
    const email_button = document.getElementById("send_email");
    const error_wrong_email = document.getElementById("error_wrong_email");
    const email_class = document.getElementById("email").classList;
    if (!!email && email != "") {
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
