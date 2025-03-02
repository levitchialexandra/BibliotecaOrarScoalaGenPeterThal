// Varianta parolă low-security
document.addEventListener('DOMContentLoaded', function () {
  const protectedContent = document.querySelector('#protected-content');
  const passwordForm = document.querySelector('#password-form');
  const errorMessage = document.querySelector('#error-message');
  const passwordInput = document.querySelector('#password');
 
  try{
  protectedContent.style.display = 'none';
 
  //btnChcekOrarPassword
  const submitBtn = document.getElementById('btnChcekOrarPassword');

  submitBtn.addEventListener('click', checkPassword);

  async function checkPassword() {
    const passwordValue = passwordInput.value;

    const response = await fetch("/check-password", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ password: passwordValue })
    });

    const result = await response.json();

    if (result.success) {
      protectedContent.style.display = 'block';
      passwordForm.style.display = 'none';
      passwordInput.value = '';

      const expirationTime = Date.now() + 30 * 60 * 1000; // 30 minutes
      localStorage.setItem('hasAccess', expirationTime);

      setTimeout(removeAccess, 30 * 60 * 1000); // Auto-remove after 30 minutes

    } else {
      errorMessage.style.visibility = 'visible';
    }
  }

  function removeAccess() {
    localStorage.removeItem('hasAccess');
    protectedContent.style.display = 'none';
    passwordForm.style.display = 'block';
    errorMessage.style.visibility = 'hidden';
  }

  function checkAccess() {
    const savedExpiration = localStorage.getItem('hasAccess');
    if (savedExpiration && Date.now() < savedExpiration) {
      protectedContent.style.display = 'block';
      passwordForm.style.display = 'none';
      setTimeout(removeAccess, savedExpiration - Date.now());
    } else {
      removeAccess();
    }
  }

  checkAccess(); // Check access on page load

  document
    .querySelector('#password-form button')
    .addEventListener('click', checkPassword);
  }
  catch(e){}
});




// Varianta parolă low-security
// document.addEventListener('DOMContentLoaded', function () {
//   const protectedContent = document.querySelector('#protected-content');
//   const passwordForm = document.querySelector('#password-form');
//   const errorMessage = document.querySelector('#error-message');
//   const passwordInput = document.querySelector('#password');

//   protectedContent.style.display = 'none';

//   function checkPassword() {
//     const passwordValue = passwordInput.value;
//     const correctPassword = 'secret123';
//     passwordInput.value = '';

//     if (passwordValue === correctPassword) {
//       protectedContent.style.display = 'block';
//       passwordForm.style.display = 'none';

//       const expirationTime = Date.now() + 30 * 60 * 1000; // 30 minutes
//       localStorage.setItem('hasAccess', expirationTime);

//       setTimeout(removeAccess, 30 * 60 * 1000); // Auto-remove after 30 minutes
//     } else {
//       errorMessage.style.visibility = 'visible';
//     }
//   }

//   function removeAccess() {
//     localStorage.removeItem('hasAccess');
//     protectedContent.style.display = 'none';
//     passwordForm.style.display = 'block';
//     errorMessage.style.visibility = 'hidden';
//   }

//   function checkAccess() {
//     const savedExpiration = localStorage.getItem('hasAccess');
//     if (savedExpiration && Date.now() < savedExpiration) {
//       protectedContent.style.display = 'block';
//       passwordForm.style.display = 'none';
//       setTimeout(removeAccess, savedExpiration - Date.now());
//     } else {
//       removeAccess();
//     }
//   }

//   checkAccess(); // Check access on page load

//   document
//     .querySelector('#password-form button')
//     .addEventListener('click', checkPassword);
// });
