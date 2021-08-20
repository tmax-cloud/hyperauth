window.onload = () => {
  // const element = document.querySelector("input#password");
  var filter = "win16|win32|win64|mac";
  if (navigator.platform) {
    if (0 > filter.indexOf(navigator.platform.toLowerCase())) {
      document.querySelector("div.checkbox #rememberMe").checked = true;
    }
  }

  // document
  //   .querySelector(".hyperspace-password")
  //   .addEventListener("click", () => {
  //     if (element.type === "password") {
  //       document
  //         .querySelector(".hyperspace-password.hide-password")
  //         .classList.add("show-password");
  //       document
  //         .querySelector(".hyperspace-password.hide-password")
  //         .classList.remove("hide-password");
  //       element.type = "text";
  //     } else {
  //       document
  //         .querySelector(".hyperspace-password.show-password")
  //         .classList.add("hide-password");
  //       document
  //         .querySelector(".hyperspace-password.show-password")
  //         .classList.remove("show-password");
  //       element.type = "password";
  //     }
  //   });
};
