const carouselElement = document.getElementById("contentCarousel");
const carousel = new bootstrap.Carousel(carouselElement);
const paginationLinks = document.querySelectorAll(".page-link");

function updateActivePage(index) {
  paginationLinks.forEach((link, i) => {
    link.parentElement.classList.toggle("active", i === index);
  });
}

function changeSlide(index) {
  carousel.to(index);
}

carouselElement.addEventListener("slid.bs.carousel", function (event) {
  updateActivePage(event.to);
});

// Initialize first page as active
updateActivePage(0);
