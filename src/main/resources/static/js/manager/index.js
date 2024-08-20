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
        url: contextPath + 'manager/get-all',
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
                cell1.innerHTML = `${object.email || ''}`;

                var tg = object.telegram ? `<a href="https://t.me/${object.telegram.replace("@", "")}">${object.telegram}</a>` : ''
                var cell2 = newRow.insertCell(2);
                cell2.innerHTML = tg

                var cell3 = newRow.insertCell(3);
                cell3.innerHTML = `${object.phone || ''}`;
            }
            $('#pagination_container').empty();
            if (objects.totalPages > 1) updatePagination(page, objects.totalPages, 'pagination_container')
        },
        complete: function (xhr, status) {
            hideLoader(tableId)
        }
    })
}
function showModalForAddManager(managerId){
    var text = managerId ? "Редагувати менеджера" : "Додати менеджера"
    if ($('#modal-for-add-manager').html()) $('#modal-for-add-manager').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modal-for-add-manager" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-xl" role="document">
                <div id="content-form" class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        <h5 class="modal-title">${text}</h5>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-lg-6 col-sm-12">
                                Прізвище
                                <input class="form-control" id="lastName">
                            </div>
                            <div class="col-lg-6 col-sm-12">
                                email
                                <input class="form-control" id="email">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6 col-sm-12">
                                Ім'я
                                <input class="form-control" id="name">
                            </div>
                            <div class="col-lg-6 col-sm-12">
                                Telegram
                                <input class="form-control" id="telegram">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6 col-sm-12">
                                По батькові
                                <input class="form-control" id="middleName">
                            </div>
                            <div class="col-lg-6 col-sm-12">
                                Телефон
                                <input class="form-control" id="phone">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addManager(${managerId})">Додати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#modal-for-add-manager').modal('show');

    if(managerId){
        $.ajax({
            type: "Get",
            url: contextPath + 'literature/get-by-id',
            data: {
                id: literatureId
            },
            success: function (object) {
                $("#name").val(object.name)
                $("#link").val(object.link)
                forSelect2("#course", contextPath + "course/get-for-select", object.courseId, object.courseName)
            }
        })
    }
}
function addManager(managerId){
    showLoader("content-form")
    let formData = new FormData()
    if(managerId)formData.append('id', managerId)
    formData.append('lastName', $("#lastName").val())
    formData.append('name', $("#name").val())
    formData.append('middleName', $("#middleName").val())
    formData.append('email', $("#email").val())
    formData.append('telegram', $("#telegram").val())
    formData.append('phone', $("#phone").val())
    $.ajax({
        url: contextPath + 'manager/add',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function () {
            cleanInputs()
            showToastForSave()
            getPageWithFilter(page)
            $('#modal-for-add-manager').modal('hide')
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON)
            } else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        },
        complete: function (xhr, status) {
            hideLoader("content-form")
        }
    })
}