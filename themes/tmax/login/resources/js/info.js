async function finishIdpSuccess(baseUrl, actionUri) {
  const formData = new URLSearchParams();
  formData.append("isBrokerLogin", true);
  try {
    const options = {
      method: "POST",
      data: formData,
      url: actionUri,
    };
    await axios(options);
    location.href = baseUrl;
  } catch (e) {
    console.error(e);
  }
}
