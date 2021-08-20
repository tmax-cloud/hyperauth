window.onload = () => {
  // const element = document.querySelector("input#password");
  var filter = "win16|win32|win64|mac";
  if (navigator.platform) {
    if (0 > filter.indexOf(navigator.platform.toLowerCase())) {
      document.querySelector("div.checkbox #rememberMe").checked = true;
    }
  }
};
