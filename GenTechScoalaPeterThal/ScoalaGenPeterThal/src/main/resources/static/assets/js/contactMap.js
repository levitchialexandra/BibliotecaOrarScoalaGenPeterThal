let map;
let userMarker;


const schoolLocation = { lat: 45.596806, lng: 25.468380 }; // Coordonatele Scoala gen Peter Thal
document.addEventListener("DOMContentLoaded", function () {
    setTimeout(() => {
        initMap();
    }, 300);


});
function initMap() {

    map = new google.maps.Map(document.getElementById("map"), {
        center: schoolLocation,
        zoom: 14,
    });


    new google.maps.Marker({
        position: schoolLocation,
        map: map,
        title: "Școala generala Peter Thal"
    });
    let checkbox = document.getElementById("cbTraseu");
    if (!checkbox.checked) return;
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const userLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            // Adaugă marker pentru locația utilizatorului
            userMarker = new google.maps.Marker({
                position: userLocation,
                map: map,
                title: "Locația mea"
            });


            map.setCenter(userLocation);


            const directionsService = new google.maps.DirectionsService();
            const directionsRenderer = new google.maps.DirectionsRenderer();
            directionsRenderer.setMap(map);

            const request = {
                origin: userLocation,
                destination: schoolLocation,
                travelMode: google.maps.TravelMode.DRIVING,
            };

            directionsService.route(request, function (result, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsRenderer.setDirections(result);
                } else {
                    alert("Nu s-a putut genera traseul.");
                }
            });
        }, function () {
            alert("Nu am reușit să localizăm utilizatorul.");
        });
    } else {
        alert("Geolocația nu este suportată de acest browser.");
    }
}