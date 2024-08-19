var page = 0
$(document).ready(function () {
    getPageWithFilter(page)
})

function getPageWithFilter(page) {
    var tableId = 'content-table'
    showLoader(tableId)
    this.page = page
    var filterElements = $('.for-filter');
    $.ajax({
        type: "Get",
        url: contextPath + 'student/get-all',
        data: {
            page: page,
            pageSize: pageSize,
            fullName: filterElements[0].value,
            email: filterElements[1].value,
            telegram: filterElements[2].value,
            phone: filterElements[3].value,
        },
        success: function (objects) {
            var table = document.getElementById(tableId);
            var tbody = table.querySelector("tbody");
            $('#' + tableId + ' tbody').empty();
            if ($("#message-about-empty")) $("#message-about-empty").remove()
            if (objects.content.length == 0) {
                table.insertAdjacentHTML('afterend', '<center><h1 id="message-about-empty">Немає даних для відображення</h1></center>')
                $('#pagination_container').empty()
                return
            }
            for (var object of objects.content) {
                var newRow = tbody.insertRow();
                var cell0 = newRow.insertCell(0);
                cell0.innerHTML = `${object.fullName}`;

                var cell1 = newRow.insertCell(1);
                cell1.innerHTML = `${object.email}`;

                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = `<a href="https://t.me/${object.telegram.replace("@", "")}">${object.telegram}</a>`;

                var cell3 = newRow.insertCell(3);
                cell3.innerHTML = `${object.phone}`;
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader(tableId)
        }
    })
}