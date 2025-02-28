var cols = [{
	"data": "title"
}, {
	"data": "author"
}, {
	"data": "genre.name"
}, {
	"data": "publicationYear"
}, {
	"data": "availability",
	"orderable": false
}];
var extraCols = [{ "data": "actions", "orderable": false, "searchable": false }];
$(document).ready(function () {
	var currentUrl = window.location.href;
	var isAdmin = currentUrl.toLowerCase().indexOf("addbook") >= 0;
	cols = isAdmin ? cols.concat(extraCols) : cols;
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
			"emptyTable": "No records available",
			"info": "Showing _START_ to _END_ of _TOTAL_ entries",
			"infoFiltered": "(filtered from _MAX_ total entries)",
			"lengthMenu": "Show _MENU_ entries",
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

				if (isAdmin) {
					json.data.forEach(function (item) {
						item.actions = '<button class="btn btn-danger btn-sm delete-btn" data-id="' + item.id + '">Șterge</button>';
						if (item.availability === "Disponibil") {
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
			}
		},

		"columns": cols,

	});

	$('#titleSearch, #authorSearch,#genreSearch,#yearSearch').on('keyup change', function () {
		table.ajax.reload();
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
	/*$('#booksTable').on('click', '.borrow-btn', function () {
		var bookId = $(this).data('id');
		var confirmation = confirm("Sigur doriți să împrumutați această carte?");
		if (confirmation) {

			$.ajax({
				url: '/borrowBook/' + bookId,
				type: 'POST',
				success: function (response) {

					table.ajax.reload();
					alert('Cartea a fost împrumutată cu succes!');
				},
				error: function (error) {
					alert('A apărut o eroare la împrumutarea cărții.');
				}
			});
		}
	});*/
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
function adjustTable() {
	var table = $('#booksTable').DataTable();
	var isMobile = $(window).width() < 768;
	table.columns([1, 2, 3, 4]).visible(!isMobile);
}
function adjustTableHeight() {

	const windowHeight = $(window).height();
	const tableContainer = document.querySelector('.table-responsive');

	tableContainer.style.maxHeight = `${windowHeight * 0.8}px`;
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