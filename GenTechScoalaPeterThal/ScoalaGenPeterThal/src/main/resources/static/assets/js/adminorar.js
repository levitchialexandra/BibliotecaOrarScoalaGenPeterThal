function updateOrar(selectElement){
    let materieId = selectElement.value;
    let ora = selectElement.getAttribute("data-ora");
    let zi = selectElement.getAttribute("data-zi");
    let clasa = selectElement.getAttribute("data-clasa");

    fetch('/pages/adminorar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ ora: ora, zi: zi, materieId: materieId, clasa: clasa })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            console.log("Schedule updated successfully!");
        } else {
            alert("Error updating schedule!");
        }
    })
    .catch(error => console.error('Error:', error));
}
function printTable() {
    const table = document.querySelector('table'); 
    const printWindow = window.open('', '', 'height=400,width=600');
    printWindow.document.write('<html><head><title>Printare orar</title></head><body>');
    printWindow.document.write(table.outerHTML);
    printWindow.document.write('</body></html>');
    printWindow.document.close();
    printWindow.print();
}

// Function to preview the table content (optional)
