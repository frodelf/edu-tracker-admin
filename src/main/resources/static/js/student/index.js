var page = 0
$(document).ready(function () {
    document.getElementById("uploadButton").addEventListener("click", function() {
        document.getElementById("fileInput").click()
    })

    document.getElementById("fileInput").addEventListener("change", function(event) {
        const file = event.target.files[0]
        const validExtensions = ["xlsx", "xls"]
        const fileExtension = file.name.split('.').pop().toLowerCase()

        if (!validExtensions.includes(fileExtension)) {
            showErrorToast("Файл повинен мати формат xlsx, xls")
            document.getElementById('fileInput').value = ""
            return
        }

        showLoader("content-table")
        let formData = new FormData()
        formData.append('file', $("#fileInput")[0].files[0])

        $.ajax({
            url: contextPath + 'student/add-all-from-file',
            type: 'POST',
            headers: {'X-XSRF-TOKEN': csrf_token},
            data: formData,
            contentType: false,
            processData: false,
            success: function () {
                getPageWithFilter(page)
            },
            error: function (xhr, status, error) {
                if (xhr.status === 400) {
                    cleanInputs()
                    validDataFromResponse(xhr.responseJSON)
                } else {
                    console.error('Помилка відправки файлів на сервер:', error);
                }
            },
            complete: function (xhr, status) {
                hideLoader("content-table")
            }
        })
    })

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