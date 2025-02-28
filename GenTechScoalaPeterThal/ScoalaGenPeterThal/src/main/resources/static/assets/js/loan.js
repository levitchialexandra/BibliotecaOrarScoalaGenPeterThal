
$(document).ready(function () {
    $('#studentSearch').on('input', function () {
        let query = $(this).val();
        if (query.length > 1) {
            $.ajax({
                url: '/studentSearch',
                type: 'GET',
                data: { query: query },
                success: function (data) {
                    let results = $('#studentResults');
                    results.empty();
                    data.forEach(function (student) {
                        results.append('<li class="list-group-item student-item" data-id="' + student.id + '">' + student.name + '</li>');
                    });
                }
            });
        }
    });
    $("#btn-close").on("click", function(){
        $('#loanModal input').val("");
    });
    $('#loanForm').submit(function (e) {
        e.preventDefault();

        let bookId = $('#bookId').val();
        let studentId = $('#studentId').val();
        let studentName = $('#studentSearch').val();
        let studentEmail = $('#studentEmail').val();
        let studentAddress = $('#studentAddress').val();
        let studentPhone = $('#studentPhone').val();
        let returnDate = $('#returnDate').val();

        $.ajax({
            url: '/createLoan',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                bookId: bookId,
                studentName: studentName,
                studentEmail: studentEmail,
                studentAddress: studentAddress,
                studentPhone: studentPhone,
                returnDate: returnDate
            }),
            success: function (response) {
                alert("Împrumut salvat cu succes!");
                $('#loanModal').modal('hide');
                $('#loanModal input').val("");
                $('#booksTable').DataTable().ajax.reload();
            },
            error: function () {
                alert("Eroare la salvarea împrumutului!");
            }
        });
    });
});

$(document).on('click', '.student-item', function () {
    let studentId = $(this).data('id');
    $('#studentId').val(studentId);
    $('#studentSearch').val($(this).text());
    $('#studentResults').empty();


    $.ajax({
        url: '/students/' + studentId,
        type: 'GET',
        success: function (student) {
            $('#studentEmail').val(student.email);
            $('#studentAddress').val(student.address);
            $('#studentPhone').val(student.phone);
        }
    });
});
$(document).on('click', '.borrow-btn', function () {
    var bookId = $(this).data('id');
    var bookTitle = $(this).data('title');

    $('#bookId').val(bookId);
    $('#loanModalLabel').text('Împrumut carte: ' + bookTitle);
    $('#loanModal').modal('show');
});


