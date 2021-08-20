// const url = document.location.origin;
// const serverUrl = "https://172.22.6.11";
const serverUrl = document.location.origin;

const TERM_ACCOUNT_PRIVACY_DUTY = "account_privacy_duty_terms";
const TERM_ACCOUNT = "account_terms";
const TERM_SERVICE = "service_terms";
const TERM_PRIVACY = "privacy_terms";
const TERM_THIRD_PRIVACY = "third_privacy_terms";
const oneAccountTermList = [
  TERM_ACCOUNT_PRIVACY_DUTY,
  TERM_ACCOUNT,
  TERM_SERVICE,
  TERM_PRIVACY,
  TERM_THIRD_PRIVACY,
];

const WAPL_SERVICE = "wapl_service_terms";
const WAPL_PRIVACY = "wapl_privacy_terms";
const WAPL_PRIVACY_OPTIONAL = "wapl_privacy_optional_terms";
const waplTermList = [WAPL_SERVICE, WAPL_PRIVACY, WAPL_PRIVACY_OPTIONAL];

const PORTAL_SERVICE = "portal_service_terms";
const PORTAL_PRIVACY = "portal_privacy_terms";
const portalTermList = [PORTAL_SERVICE, PORTAL_PRIVACY];

const HYPERMEETING_SERVICE = "hypermeeting_service_terms";
const HYPERMEETING_PRIVACY = "hypermeeting_privacy_terms";
const hypermeetingTermList = [HYPERMEETING_SERVICE, HYPERMEETING_PRIVACY];

const WAPLCLASS_SERVICE = "waplclass_service_terms";
const WAPLCLASS_PRIVACY = "waplclass_privacy_terms";
const WAPLCLASS_TEENAGER = "waplclass_teenager_terms";
const WAPLCLASS_CHILDREN = "waplclass_children_terms";
const waplclassTermList = [
  WAPLCLASS_SERVICE,
  WAPLCLASS_PRIVACY,
  WAPLCLASS_TEENAGER,
  WAPLCLASS_CHILDREN,
];

const WAPLMATH_SERVICE = "waplmath_service_terms";
const WAPLMATH_PRIVACY = "waplmath_privacy_terms";
const WAPLMATH_TEENAGER = "waplmath_teenager_terms";
const WAPLMATH_CHILDREN = "waplmath_children_terms";
const waplmathTermList = [
  WAPLMATH_SERVICE,
  WAPLMATH_PRIVACY,
  WAPLMATH_TEENAGER,
  WAPLMATH_CHILDREN,
];

function getTerms(clientName, language) {
  termList = eval(clientName + "TermList");
  termList.forEach((termName) => {
    getTerm(termName, language);
  });
}

async function getTerm(termName, language) {
  try {
    const term = document.getElementById(termName);
    const version = `latest_${language}`;
    const resp = await axios.get(
      `${serverUrl}/auth/realms/tmax/agreement/${termName}?version=${version}&realmName=tmax`
    );
    term.innerHTML = resp.data;
  } catch (e) {
    console.error(e);
  }
}

function clickArrow(e) {
  const arrow = document.getElementById(e);
  const term = document.getElementById(e.split("-")[0]);
  if (arrow.classList.contains("up")) {
    arrow.classList.remove("up");
    term.style.display = "none";
  } else {
    arrow.classList.add("up");
    term.style.display = "block";
  }
}

function submitAgreement() {
  const agreeForm = document.getElementById("agreementUrl");
  let checkbox = document.querySelectorAll("input[type='checkbox']");
  for (let i = 0; i < checkbox.length; i++) {
    checkbox[i].value = checkbox[i].checked;
    if (checkbox[i].checked == false) {
      let falseCheckbox = document.createElement("input");
      falseCheckbox.setAttribute("type", "hidden");
      falseCheckbox.setAttribute("name", checkbox[i].name);
      falseCheckbox.setAttribute("value", checkbox[i].value);
      agreeForm.appendChild(falseCheckbox);
    }
  }
  agreeForm.submit();
}

function openCancelModal() {
  document.getElementById("cancelModal").classList.remove("hidden");
}

function cancelChangeAgreement() {
  location.href = document.location;
}

function activateSaveButton() {
  document
    .getElementById("agreement-submit-button")
    .removeAttribute("disabled");
}