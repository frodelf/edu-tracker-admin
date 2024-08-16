var courseId
const snowEditor = new Quill('#goal', {
    bounds: '#goal',
    modules: {
        formula: true,
        toolbar: '#snow-toolbar'
    },
    theme: 'snow'
})

if (course) {
    courseId = course.id
    $("#name").val(course.name)
    $("#maximumMark").val(course.maximumMark)
    var delta = snowEditor.clipboard.convert(course.goal)
    snowEditor.setContents(delta)
    console.log(course)
    $('#image-preview').attr("src", course.image)
}
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('cancel').onclick = function () {
        window.location.href = contextPath + 'course'
    }
    $(".check-image-extension").change(function () {
        var file = this.files[0]
        if (file) {
            var fileName = file.name
            var fileExtension = fileName.split('.').pop().toLowerCase()

            var allowedExtensions = ['png', 'jpeg', 'jpg']

            if (allowedExtensions.indexOf(fileExtension) === -1) {
                toastr.options.progressBar = true;
                toastr.error('Дозволені тільки файли з розширеннями .png, .jpeg або .jpg')
                $(this).val('')
                return
            }
            previewImage("image-preview", "image")
        }
    })
})

function save() {
    showLoader("content-form")
    let formData = new FormData()
    if(courseId)formData.append("id", courseId)
    formData.append("name", $("#name").val())
    if(snowEditor.root.innerHTML !== '<p><br></p>') formData.append("goal", snowEditor.root.innerHTML)
    formData.append("maximumMark", $("#maximumMark").val())
    if($("#image")[0].files[0]){
        formData.append("image", $("#image")[0].files[0])
    }

    $.ajax({
        url: contextPath + 'course/add',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        contentType: false,
        processData: false,
        success: function (request) {
            cleanInputs()
            showToastForSave()
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
function showModalForAddStudentStepFirst() {
    if ($('#ModalForAddStudentStepFirst').html()) $('#ModalForAddStudentStepFirst').remove()

    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForAddStudentStepFirst" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Додати студента на курс</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Група
                        <select id="groupForStudentAdding"></select>
                        Курс
                        <select id="coursesForStudentAdding"></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="searchStudent()">Шукати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#ModalForAddStudentStepFirst').modal('show');
    forSelect2("#coursesForStudentAdding", contextPath + "course/get-for-select")
    forSelect2WithSearchAndPageable("#groupForStudentAdding", contextPath + "student/get-group-for-select")
}

function searchStudent() {
    cleanInputs()
    var valid = true
    if (!($("#groupForStudentAdding").val())) {
        validSelect2($("#groupForStudentAdding"))
        valid = false
    }
    if(!valid)return

    $('#ModalForAddStudentStepFirst').modal('hide');
    showModalForAddStudentStepSecond($("#groupForStudentAdding").val(), )
}

function activateAllCheckbox(checkbox) {
    if (checkbox.checked) {
        console.log("asd")
        $(".for-student-adding").prop('checked', true);
    } else {
        console.log("asdddddd")
        $(".for-student-adding").prop('checked', false);
    }
}
function showModalForAddStudentStepSecond(group, courseId) {
    $.ajax({
        url: contextPath + 'student/get-all-by-group-and-course',
        data: {
            group: group,
            courseId: courseId
        },
        success: function (students) {
            console.log(students)
            if ($('#ModalForAddStudentStepSecond').html()) $('#ModalForAddStudentStepSecond').remove()

            var modalBlock = document.createElement('div');
            modalBlock.innerHTML = `
        <div class="modal fade" id="ModalForAddStudentStepSecond" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Студенти</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="card">
                            <div class="card-header">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped linkedRow"
                                           id="tableForStudentAdd" style="table-layout: fixed;">
                                        <thead>
                                        <tr>
                                        <th>Група</th>
                                        <th>Назва</th>
                                        <th><center><input type="checkbox" onclick="activateAllCheckbox(this)" class="form-check-input"></center></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addStudentToCourse(${courseId})">Додати</button>
                    </div>
                </div>
            </div>
        </div>
    `
            document.body.appendChild(modalBlock)
            $('#ModalForAddStudentStepSecond').modal('show')

            var table = document.getElementById("tableForStudentAdd")
            var tbody = table.querySelector("tbody")
            $('#tableForStudentAdd tbody').empty()
            for (const student of students) {
                var newRow = tbody.insertRow()
                var cell0 = newRow.insertCell(0)
                cell0.innerHTML = `${student.groupName}`

                var cell1 = newRow.insertCell(1)
                cell1.innerHTML = `<a href="${contextPath}student/${student.id}">${student.fullName}</a>`

                var cell2 = newRow.insertCell(2)
                cell2.innerHTML = `<center><input type="checkbox" id="student${student.id}" class="form-check-input for-student-adding" ${student.present == true ? 'checked'  : ''}></center>`
            }
        },
    })
}
function addStudentToCourse(courseId) {
    var formData = new FormData();
    document.querySelectorAll('.for-student-adding').forEach(function(input) {
        var studentId = input.id.replace("student", "");
        formData.append(studentId, input.checked);
    });

    formData.append("courseId", courseId);

    $.ajax({
        url: contextPath + 'student/add-to-course',
        type: 'POST',
        headers: {'X-XSRF-TOKEN': csrf_token},
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            showToastForSave()
            $('#ModalForAddStudentStepSecond').modal('hide')
            getPageWithFilter(page)
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}