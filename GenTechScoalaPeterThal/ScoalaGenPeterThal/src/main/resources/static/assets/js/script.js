
document.addEventListener("DOMContentLoaded", function () {
    fetch('/components/navbar.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById("navbar-container").innerHTML = data;
        })
        .catch(error => console.error('Eroare la încărcarea header-ului:', error));
   
        setTimeout(() => {
            const currentPath = window.location.pathname.toLowerCase();
            document.querySelectorAll('.nav-item .nav-link.active').forEach(el => el.classList.remove('active'));
            const menuLinks = document.querySelectorAll(".dropdown-menu .dropdown-item");
            (document.querySelectorAll(".dropdown-toggle"))[0].classList.remove("active");
            if (currentPath.indexOf("orar") > -1 || currentPath.indexOf("biblioteca") > -1 || currentPath.indexOf("ghidul") > -1){
                menuLinks.forEach(link => {
                    (document.querySelectorAll(".dropdown-toggle"))[0].classList.add("active");
                    const linkHref = link.getAttribute("href").toLowerCase();
    
                    // Check if the current path matches any menu item's href
                    if (currentPath.includes("orar") && linkHref.includes("orar")) {
                        link.classList.add("hover-movActive");
                    } else if (currentPath.includes("biblioteca") && linkHref.includes("biblioteca")) {
                        link.classList.add("hover-galbenActive");
                    } else if (currentPath.includes("ghidul") && linkHref.includes("ghidul")) {
                        link.classList.add("hover-rozActive");
                    }
    
                });
        }
    
    
            const activeLink = document.querySelector(`.nav-item .nav-link[href="${currentPath}"]`);
        if (activeLink) {
            activeLink.classList.add('active');
        }
    
    }, 200);
    
});

document.addEventListener("DOMContentLoaded", function () {
    fetch('/components/footer.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById("footer-container").innerHTML = data;
        })
        .catch(error => console.error('Eroare la încărcarea header-ului:', error));
});

document.querySelectorAll('.flip-card').forEach((card) => {
    card.addEventListener('click', () => {
        card.classList.toggle('flipped');
    });
});

// copiere date contact
function copyToClipboard(element) {
    const textElement = element.querySelector(".text-to-copy");
    const originalText = textElement.innerText;
    const textCopied = textElement.getAttribute("data-value");

    element.onclick = null;

    navigator.clipboard.writeText(originalText)
        .then(() => {
            textElement.innerText = textCopied;

            setTimeout(() => {
                textElement.innerHTML = originalText;
                element.onclick = function () {
                    copyToClipboard(element);
                };
            }, 1500);
        })
        .catch(err => {
            console.error("Error copying text: ", err);
            element.onclick = function () {
                copyToClipboard(element);
            };
        });
}

// cookies function
window.addEventListener("load", function () {
    function getCookie(name) {
        return document.cookie.split('; ').find(row => row.startsWith(name + '='))?.split('=')[1];
    }

    function setCookie(name, value, days) {
        let expires = "";
        if (days) {
            let date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + value + "; path=/" + expires;
    }

    function initCookieBanner() {
        const cookieBanner = document.getElementById("cookie-banner");

        if (!cookieBanner) {
            console.error("❌ Cookie banner nu a fost găsit. Verifică dacă este inclus corect!");
            return;
        }

        if (getCookie("accept_cookies") === "true" || getCookie("essential_cookies") === "true") {
            cookieBanner.style.display = "none";
            return;
        }

        cookieBanner.style.display = "block";

        document.getElementById("accept-cookies").addEventListener("click", function () {
            setCookie("accept_cookies", "true", 30);
            cookieBanner.style.display = "none";
            console.log("✅ Toate cookie-urile acceptate.");
        });

        document.getElementById("essential-cookies").addEventListener("click", function () {
            setCookie("essential_cookies", "true", 30);
            cookieBanner.style.display = "none";
            console.log("✅ Doar cookie-urile esențiale acceptate.");
        });
    }

    initCookieBanner();
});


