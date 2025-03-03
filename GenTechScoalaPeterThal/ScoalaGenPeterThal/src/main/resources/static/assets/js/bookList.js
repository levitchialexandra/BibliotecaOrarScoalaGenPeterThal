var cols = [{
	"data": "title"
}, {
	"data": "author"
}, {
	"data": "genre.name"
}, {
	"data": "publicationYear"
}, {
	"data": "bookStatus",
	"orderable": false
}];

var extraCols = [{ "data": "actions", "orderable": false, "searchable": false }];
var extraColsDetails = [{
	"data": null,
	"orderable": false,
	"className": "dt-control",
	"defaultContent": '',
	"searchable": false
}];
var isAdmin = window.location.href.toLowerCase().indexOf("adminbiblioteca") >= 0;
$(document).ready(function () {


	cols = isAdmin ? cols.concat(extraCols) : extraColsDetails.concat(cols);
	var table = $('#booksTable').DataTable({
		"order": [],
		"paging": false,
		"lengthChange": true,
		"pageLength": 10,
		"searching": false,
		"ordering": true,
		"orderCellsTop": true,
		"serverSide": true,
		"pagingType": "full_numbers",
		"processing": true,
		"language": {
			"emptyTable": "Nu sunt înregistrări disponibile",
			"info": "Showing _START_ to _END_ of _TOTAL_ entries",
			"infoFiltered": "(filtered from _MAX_ total entries)",
			"lengthMenu": "Show _MENU_ entries",
			"processing": "Se cauta...",
			"zeroRecords": "Nu s-au găsit înregistrări corespunzătoare"
		},
		"dom": '<"H"lfr>t<"F"ip>',
		"ajax": {
			"url": "/booksAjaxWithAvailability",
			//"/booksAjax",
			"type": "GET",
			"data": function (d) {

				d.title = $('#titleSearch').val();
				d.author = $('#authorSearch').val() || "";
				d.publicationYear = $('#yearSearch').val() || "";
				d.genre = $('#genreSearch').val() || "";
				d.availability = $('#availabilityFilter').val() || "";
			},
			"dataSrc": function (json) {
				console.log("Received Data:", json);
				if (isAdmin) {
					json.data.forEach(function (item) {
						item.actions = '<button class="btn btn-danger btn-sm delete-btn" data-id="' + item.id + '">Șterge</button>';
						if (item.bookStatus === "Disponibil") {
							item.actions += '<button class="btn btn-success btn-sm borrow-btn" data-id="' + item.id + '" data-title="' + item.title + '">Împrumută</button>';
						} else {
							item.actions += '<button class="btn btn-secondary btn-sm return-btn " data-id="' + item.id + '">Restituie</button>';
						}
					});

				}

				$("#booksTable_length").hide();
				$("#booksTable_filter").hide();
				$("#booksTable_info").hide();
				return json.data;
			}, "initComplete": function (settings, json) {
				$("#loadingSpinner").hide(); // Hide the loading spinner when data is completely loaded
			},
			"preDrawCallback": function (settings) {
				$("#loadingSpinner").show(); // Show the loading spinner before the data is drawn
			},
			"drawCallback": function (settings) {
				$("#loadingSpinner").hide(); // Hide the loading spinner once data is drawn
			}
		},

		"columns": cols,

	});

	$('#titleSearch, #authorSearch,#genreSearch,#yearSearch').on('keyup change', function () {
		table.ajax.reload();
	});
	$('#booksTable tbody').on('click', '.dt-control', function () {
		var tr = $(this).closest('tr');
		var row = $('#booksTable').DataTable().row(tr);

		if (row.child.isShown()) {
			row.child.hide();

		} else {

			row.child(formatDetails(row.data())).show();
			var td = tr.next().find('td:first');
			td.attr('colspan', 2);

		}
	});

	$('#searchForm').on('submit', function (e) {
		e.preventDefault();
		table.ajax.reload();
	});

	$('#availabilityFilter').on('change', function () {

		table.ajax.reload();
	});

	$("#booksTable_length").hide();
	$("#booksTable_filter").hide();
	$('#booksTable').on('click', '.delete-btn', function () {
		var bookId = $(this).data('id');
		var confirmation = confirm("Sigur doriți să ștergeți această carte?");
		if (confirmation) {

			$.ajax({
				url: '/deleteBook/' + bookId,
				type: 'DELETE',
				success: function (response) {

					table.ajax.reload();
					alert('Cartea a fost ștearsă cu succes!');
				},
				error: function (error) {
					alert('A apărut o eroare la ștergerea cărții.');
				}
			});
		}
	});

	$('#booksTable').on('click', '.return-btn', function () {
		var bookId = $(this).data('id');
		var confirmation = confirm("Sigur doriți să restituiți această carte?");
		if (confirmation) {

			$.ajax({
				url: '/returnBook/' + bookId,
				type: 'POST',
				success: function (response) {

					table.ajax.reload();
					alert('Cartea a fost restituită cu succes!');
				},
				error: function (error) {
					alert('A apărut o eroare la restituirea cărții.');
				}
			});
		}
	});
	adjustTableHeight();
	adjustTable();
});
$(window).resize(function () {
	adjustTable();
	adjustTableHeight()

});
function formatDetails(data) {
	return `
		 
                <strong>Autor:</strong> ${data.author} <br>
                <strong>Gen:</strong> ${data.genre.name} <br>
                <strong>Anul publicării:</strong> ${data.publicationYear} <br>
                <strong>Disponibilitate:</strong> ${data.availability}
           
	`;
}
function adjustTable() {
	var table = $('#booksTable').DataTable();
	var isMobile = $(window).width() < 768;
	if (!isMobile && !isAdmin) {
		table.columns([0]).visible(false);
		$("#thExtraDetails").hide();
		$("#tdExtraDetails").hide();
		return;
	}
	
		table.columns([2, 3, 4, 5]).visible(!isMobile);
	
}
function adjustTableHeight() {

	const windowHeight = $(window).height();
	const tableContainer = document.querySelector('.table-responsive');

	//tableContainer.style.maxHeight = `${windowHeight * 0.8}px`;
	if ($(window).width() < 768) {
		var tbWrrapper = $('#booksTable_wrapper');


		var bW = tbWrrapper.width() - 25;

		$("#booksTable").width(bW);
		$(".sorting").hide().css("width", bW + " !important");
		$("#booksTable td").width(bW);



		$("#titleSearch").attr("placeholder", "🔍 Căutare");
	}
	else {
		$(".sorting").show();
	}
}