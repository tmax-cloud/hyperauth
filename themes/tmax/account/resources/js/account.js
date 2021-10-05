const serverUrl = document.location.origin;
const MAX_NAME_LANGTH = 50;
const realmName = document.getElementById("realmName").dataset.value;
const getAccessToken = function () {
  return sessionStorage.getItem("accessToken");
};
let pictureImporting = false;
let pictureDeleting = false;
let fileUploadError = false;

const validationStates = {
  email: false,
  password: false,
  passwordConfirm: false,
  username: false,
};

function validateUserName() {
  const username = document.getElementById("userNameAttr");
  let result1 = false;
  let result2 = false;
  let result3 = false;

  if (username.value.length < MAX_NAME_LANGTH) {
    //username.classList.add("has_error");
    showErrorMessage("error_username_over", false);
    validationStates.username = true;
    checkAllInputStates();
    //return true;
    result1 = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_username_over", true);
    validationStates.username = false;
    checkAllInputStates();
    //return false;
    result1 = false;
  }

  if (!!username.value && username.value != "") {
    //username.classList.remove("has_error");
    showErrorMessage("error_username_empty", false);
    validationStates.username = true;
    checkAllInputStates();
    //return true;
    result2 = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_username_empty", true);
    validationStates.username = false;
    checkAllInputStates();
    //return false;
    result2 = false;
  }
  const formatReg = /^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s]+$/;

  //email.classList.remove("has-error");
  if (
    formatReg.test(username.value) ||
    !(!!username.value && username.value != "")
  ) {
    showErrorMessage("error_username_format", false);
    //username.classList.remove("has_error");
    validationStates.email = true;
    checkAllInputStates();
    //return true;
    result3 = true;
  } else {
    showErrorMessage("error_username_format", true);
    //username.classList.add("has_error");
    validationStates.email = false;
    checkAllInputStates();
    //return false;
    result3 = false;
  }
  let result = false;
  if (result1 && result2 && result3) {
    username.classList.remove("has_error");
    result = true;
  } else {
    username.classList.add("has_error");
    result = false;
  }

  return result;
}
function showErrorMessage(errorId, visible) {
  const error = document.getElementById(errorId);
  error.style.display = visible ? "block" : "none";
}

function checkAllInputStates() {
  const registerButton = document.getElementById("account-save-button");
  for (let value of Object.values(validationStates)) {
    if (!value) {
      registerButton.disabled = true;
      return;
    }
  }

  registerButton.disabled = false;
}

function buttonAbled() {
  //error check
  let nameTestResult = validateUserName();
  let result = true;
  const username = document.getElementById("userNameAttr");
  const originName = document.getElementById("originName");

  if (!nameTestResult) {
    result = false;
  } else {
    result = true;
    if (username.value == originName.value) {
      result = false;
    } else {
      result = true;
    }
  }
  if (result) {
    document.getElementById("account-save-button").removeAttribute("disabled");
  } else {
    document
      .getElementById("account-save-button")
      .setAttribute("disabled", "disabled");
  }
}

function isEmptyPassword() {
  const password = document.getElementById("password");
  let result = false;

  if (!!password.value && password.value != "") {
    //username.classList.remove("has_error");
    showErrorMessage("error_password_empty", false);
    validationStates.password = true;
    checkAllInputStates();
    //return true;
    result = true;
  } else {
    //username.classList.add("has_error");
    showErrorMessage("error_password_empty", true);
    validationStates.password = false;
    checkAllInputStates();
    //return false;
    result = false;
  }
  password.classList.remove("has_error");
  showErrorMessage("error_password_wrong", false);
  if (!result) {
    password.classList.add("has_error");
  }
  return result;
}

function withdrawalSubmitButtonAbled() {
  if (isEmptyPassword()) {
    document
      .getElementById("withdrawal-submit-button")
      .removeAttribute("disabled");
  } else {
    document
      .getElementById("withdrawal-submit-button")
      .setAttribute("disabled", "disabled");
  }
}

function clickEye(e) {
  if (e.classList.contains("activate")) {
    e.classList.remove("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "password";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "password";
    }
  } else {
    e.classList.add("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "text";
    } else if (e.id === "eye-password-confirm") {
      document.getElementById("password-confirm").type = "text";
    }
  }
}

function openModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".modal").classList.remove("hidden");
}

function closeModal() {
  document.querySelector(".modal").classList.add("hidden");
}

function openSaveModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".saveModal").classList.remove("hidden");
}

function closeSaveModal() {
  document.querySelector(".saveModal").classList.add("hidden");
}

function openCancelModal() {
  document
    .getElementById("button-ok")
    .classList.remove("modal_button_disabled");
  document.querySelector(".cancelModal").classList.remove("hidden");
}
function closeAccountCancelModal() {
  document.querySelector(".cancelModal").classList.add("hidden");
}

function changeName() {

  if (pictureDeleting){
    deleteImageFile();
    pictureDeleting = false;
  }else if (pictureImporting){
    ImportImageFile();
    pictureImporting = false;
  }

  const saveForm = document.getElementById("account-update-form");

  const email = document.createElement("input");
  email.setAttribute("type", "hidden");
  email.setAttribute("name", "email");
  email.setAttribute("value", document.getElementById("email").value);
  saveForm.appendChild(email);

  const submitAction = document.createElement("input");
  submitAction.setAttribute("type", "hidden");
  submitAction.setAttribute("name", "submitAction");
  submitAction.setAttribute("value", "Save");
  saveForm.appendChild(submitAction);

  saveForm.submit();
  openSaveModal();
}

function cancelChangeName() {
  const cancelForm = document.getElementById("account-update-form");

  const email = document.createElement("input");
  email.setAttribute("type", "hidden");
  email.setAttribute("id", "email");
  email.setAttribute("name", "email");
  email.setAttribute("value", document.getElementById("email").value);
  cancelForm.appendChild(email);

  const submitAction = document.createElement("input");
  submitAction.setAttribute("type", "hidden");
  submitAction.setAttribute("id", "submitAction");
  submitAction.setAttribute("name", "submitAction");
  submitAction.setAttribute("value", "Cancel");
  cancelForm.appendChild(submitAction);

  cancelForm.submit();
}

function openWithdrawalPage() {
  document.getElementById("account-update").classList.add("hidden");
  document.getElementById("withdrawal-step1").classList.remove("hidden");

  // withdrawal-step1 페이지 노출시에 약관 가져오는 것으로 변경 (private에서 아예 콜을 안하게 하기위함)
  getTerms()
}

function closeWithdrawalPage() {
  document.getElementById("withdrawal-form").reset();
  document.getElementById("withdrawal-step1").classList.add("hidden");
  document.getElementById("withdrawal-step2").classList.add("hidden");
  document.getElementById("withdrawal-success").classList.add("hidden");
  document.getElementById("withdrawal-failure").classList.add("hidden");
  document.getElementById("account-update").classList.remove("hidden");
  document
  .getElementById("withdrawal-submit-button")
  .setAttribute("disabled", "disabled");
}

function nextWithdrawalPage() {
  document.getElementById("withdrawal-step1").classList.add("hidden");
  document.getElementById("withdrawal-step2").classList.remove("hidden");
}

async function submitWithdrawal() {
  const withdrawalForm = document.getElementById("withdrawal-form");
  const withdrawalUrl = withdrawalForm.getAttribute("action");
  const stateChecker = document.getElementById("stateChecker-withdrawal").value;

  document
    .getElementById("withdrawal-submit-button")
    .setAttribute("disabled", "disabled");
  document
    .getElementById("withdrawal-cancel-button")
    .setAttribute("disabled", "disabled");

  let withForm = new FormData();
  withForm.append("stateChecker", stateChecker);
  let rejectService = "";

  let passwordResult = false;
  let withdrawalResult = false;

  // vendor에서 인증 받지 않은 경우만 패스워드 검증 실행
  if (
    !document
      .getElementById("withdrawal-submit-button")
      .getAttribute("data-vendor")
  ) {
    try {
      const email = document.getElementById("email-withdrawal").value;
      const password = document.getElementById("password").value;

      const params = new URLSearchParams();
      params.append("password", password);
      let passwordUrl = `${serverUrl}/auth/realms/` + realmName + `/password`;

      const passwordResp = await axios.patch(passwordUrl, params, {
        params: {
          userId: email,
        },
      });
      if (passwordResp.data == "Password is Correct") {
        passwordResult = true;
      }
    } catch (e) {
      console.error(e);
      showErrorMessage("error_password_wrong", true);
      validationStates.password = false;
      document.getElementById("password").classList.add("has_error");
      document
        .getElementById("withdrawal-submit-button")
        .setAttribute("disabled", "disabled");
      return passwordResult;
    }
  }

  try {
    const withdrawalResp = await axios.post(withdrawalUrl, withForm, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    let outString = withdrawalResp.data;
    let serviceList = [
      "wapl",
      "tmaxcloud",
      "hypermeeting",
      "waplclass",
      "waplmath",
    ];

    if (serviceList.includes(outString)) {
      rejectService = outString;
      withdrawalResult = false;
    }

    withdrawalResult = true;
  } catch (e) {
    rejectService = e.response.data;
    withdrawalResult = false;
    console.error(e);
  }

  if (withdrawalResult) {
    document.getElementById("withdrawal-step2").classList.add("hidden");
    document.getElementById("withdrawal-success").classList.remove("hidden");
    return withdrawalResult;
  } else {
    //test service
    //let testService = "테스트 서비스"
    const MSG = document.getElementById("disableService");
    //MSG.innerHTML = testService + "의 "+ MSG.innerHTML;
    MSG.innerHTML = rejectService + MSG.innerHTML;
    document.getElementById("withdrawal-step2").classList.add("hidden");
    document.getElementById("withdrawal-failure").classList.remove("hidden");
    return withdrawalResult;
  }
}

function openAgreementModal(agr_number) {
  //let agreementContent = '';
  if (agr_number == "1") {
    document
      .getElementById("account_terms_modal_title")
      .classList.remove("hidden");
    document.getElementById("account_terms_title").classList.remove("hidden");
    document.getElementById("account_terms").classList.remove("hidden");
    //agreementContent = "OneAccount Agreement"
  } else {
    document
      .getElementById("service_terms_modal_title")
      .classList.remove("hidden");
    document.getElementById("service_terms_title").classList.remove("hidden");
    document.getElementById("service_terms").classList.remove("hidden");
    //agreementContent = "Tmax Agreement"
  }

  //document.getElementById("Agreement_content").innerHTML = agreementContent;

  document.querySelector(".agreementModal").classList.remove("hidden");
}
function cloaseAgreementModal() {
  document.getElementById("account_terms_modal_title").classList.add("hidden");
  document.getElementById("account_terms_title").classList.add("hidden");
  document.getElementById("account_terms").classList.add("hidden");
  document.getElementById("service_terms_modal_title").classList.add("hidden");
  document.getElementById("service_terms_title").classList.add("hidden");
  document.getElementById("service_terms").classList.add("hidden");
  document.querySelector(".agreementModal").classList.add("hidden");
}

function getTerms() {
  getTerm("account_terms");
  getTerm("service_terms");
};

async function getTerm(company) {
  try {
    const term = document.getElementById(company);
    const version = term.classList.contains("en") ? "latest_en" : "latest_ko";
    const resp = await axios.get(
      `${serverUrl}/auth/realms/`+ realmName + `/agreement/${company}?version=${version}&realmName=` + realmName
    );
    term.innerHTML = resp.data;
  } catch (e) {
    console.error(e);
  }
}

function clickEye(e) {
  if (e.classList.contains("activate")) {
    e.classList.remove("activate");
    if (e.id === "eye-password") {
      document.getElementById("password").type = "password";
    }
  } else {
    e.classList.add("activate");
    if (e.id === "eye-password") {

      document.getElementById("password").type = "text";
    }
  }
}

const elImage = document.querySelector("#profilePicture");
let importPicture = null;
elImage.addEventListener("change", (evt) => {

  importPicture = evt.target.files[0];

  chk(importPicture);
  if(!fileUploadError){
    pictureImporting = true;
    pictureDeleting = false;

    document.getElementById("picture").src = window.URL.createObjectURL(importPicture);
    document.getElementById("picture").style.display = "block";
    document.getElementById("account-save-button").disabled = false;
    document.getElementById("userProfileImg-delete-button").style.display="flex";
  
    document.getElementById('userProfileImg-message').style.display = "inline-block"
    document.getElementById('userProfileImg-message-error').style.display = "none"
    document.getElementById("userProfileImg-delete-button").disabled = false;
  }
  else{
    if(!pictureImporting){
      document.getElementById("userProfileImg-delete-button").disabled = true;
    }
  }
 
  // let reader = new FileReader();
  // reader.readAsDataURL(picture);
  // reader.onload = function () {
  //     // console.log(reader.result);
  //     document.getElementById("picture").src = reader.result;
  //     document.getElementById("picture").style.display = "block";
  //     pictureImporting = true;
  //     document.getElementById("account-save-button").disabled = false;
  //     document.getElementById("userProfileImg-delete-button").style.display="block";
  // }

});

function chk(obj) {
  

  if (/(\.gif|\.jpg|\.jpeg|\.png|\.bmp)$/i.test(obj.name) == false) {
    fileUploadError = true;
    throw new Error('Unable to parse IMG file.');
  }
  else if (obj.size > 512000){
    fileUploadError = true;
    document.getElementById('userProfileImg-message').style.display = "none"
    document.getElementById('userProfileImg-message-error').style.display = "inline-flex"
    document.getElementById("account-save-button").disabled = true;
    if(!pictureImporting){
      document.getElementById("userProfileImg-delete-button").disabled = true;
    }
 
    throw new Error('Cannot Upload IMG file larger than 500KB.');
    
  }
  else fileUploadError =false;

  return;
}

getPrevUserPicture()
function getPrevUserPicture() {
  try {
    const email =  document.getElementById("email").value;
    axios.get(
      `${serverUrl}/auth/realms/`+ realmName + `/picture/` + email
    ).then((response) => {
      console.log(response);
      let prevPicture = response.data.imagePath;
      if (prevPicture != null && prevPicture.length > 0){
        document.getElementById("picture").style.display="block";
        document.getElementById("picture").src = `${serverUrl}/` + prevPicture;
        document.getElementById("userProfileImg-delete-button").disabled=false;
        pictureImporting = true;
      } else {
        document.getElementById("picture").style.display="none";
        pictureImporting = false;
      }
    });
  } catch (e) {
    console.error(e);
  }
};

function deleteImageFileCheck(){
 
  if(pictureImporting){
    document.getElementById("account-save-button").disabled = false;
  }
  pictureImporting = false;
  pictureDeleting = true;
  
  document.getElementById("picture").style.display="none"; 
  document.getElementById('userProfileImg-message').style.display = "inline-block"
  document.getElementById('userProfileImg-message-error').style.display = "none"
   
}

function deleteImageFile(){
  try {
    const email =  document.getElementById("email").value;
    axios.delete(
      `${serverUrl}/auth/realms/`+ realmName + `/picture/` + email
    ).then((response) => {
      console.log(response);
    });
  } catch (e) {
    console.error(e);   
  }
}

function ImportImageFile(){


  try {
    const email =  document.getElementById("email").value;
    let fd = new FormData();
    fd.append('imageFile', importPicture)
    fd.append('imageName', importPicture.name)
    // data = { 'userName': email, 'base64EncodeImage': document.getElementById("picture").src };
    axios.post(
      `${serverUrl}/auth/realms/`+ realmName + `/picture/` + email, fd
      ).then((response) => {
        console.log('response : ', JSON.stringify(response, null, 2))
        document.getElementById("userProfileImg-delete-button").disabled = false;
      }).catch( error => {
        console.log('failed to import image file', error)
      })
    } catch (e) {
      console.error(e);
  }
}

// function ImportImageFile(){
//   const email =  document.getElementById("email").value;
//   let fd = new FormData();
//   fd.append('imageFile', importPicture)
//   fd.append('imageName', importPicture.name)
//
//   fetch(`${serverUrl}/auth/realms/`+ realmName + `/picture/` + email, {
//     method: "POST",
//     body: fd
//   })
//       .then((response) => console.log(response));
// }
