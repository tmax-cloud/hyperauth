(function lableChange() {
  const div = document.querySelectorAll(".label");

  div.forEach(function(divItem){
    const lable = divItem.childNodes[1]
    const idforDiv =lable.innerHTML.trim();
    divItem.appendChild(document.createElement('div') )
    const snsTitle = divItem.lastChild;
    snsTitle.setAttribute("id", "snsTitle");
      if(idforDiv=="kakao"){
        snsTitle.innerHTML ="카카오"
      }else if(idforDiv=="naver"){
        snsTitle.innerHTML ="네이버"
      }else{
        snsTitle.innerHTML =idforDiv
      }
    divItem.setAttribute("id", idforDiv);
    lable.innerHTML = "";


  })
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
