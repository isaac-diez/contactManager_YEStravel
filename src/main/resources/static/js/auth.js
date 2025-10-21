
export async function fetchWithAuth(url, options = {}) {

  const response = await fetch(url, options);

  if (response.status === 401 || response.status === 403) {

    window.location.href = "/login";
  }
  return response;
}

