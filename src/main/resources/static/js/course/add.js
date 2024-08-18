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
    var image = course.image || contextPath+"img/default.png"

    $("#add-student-button").remove()
    courseId = course.id
    $("#name").val(course.name)
    $("#maximumMark").val(course.maximumMark)
    var delta = snowEditor.clipboard.convert(course.goal)
    snowEditor.setContents(delta)
    $('#image-preview').attr("src", image)
    forSelect2WithSearchAndPageable("#professorId", contextPath + "professor/get-for-select", course.professorId, course.professorName)
}
else {
    forSelect2WithSearchAndPageable("#professorId", contextPath + "professor/get-for-select")
}
document.addEventListener('DOMContentLoaded', function () {
    $('#maximumMark').on('input', function() {
        var value = parseInt($(this).val(), 10);

        if (value < 1) {
            $(this).val(1);
        } else if (value > 100) {
            $(this).val(100);
        }
    });

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

    if((!($("#groupForStudentAdding").val()) || $("#groupForStudentAdding").val().length==0) && !course) {
            showErrorToast("Виберіть групу")
            return
    }
    showLoader("content-form")
    let formData = new FormData()
    if(courseId)formData.append("id", courseId)
    formData.append("name", $("#name").val())
    if(snowEditor.root.innerHTML !== '<p><br></p>') formData.append("goal", snowEditor.root.innerHTML)
    formData.append("maximumMark", $("#maximumMark").val())
    if($("#image")[0].files[0]){
        formData.append("image", $("#image")[0].files[0])
    }
    if(!course)formData.append("groups", $("#groupForStudentAdding").val())
    formData.append("professorId", $("#professorId").val())
    formData.append("isForChoosing", $('#isForChoosing').is(':checked'))

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
                        <select id="groupForStudentAdding" multiple></select>
                    </div>
                    <div class="modal-footer">
                        <button class="float-end btn btn-primary" onclick="addGroupsToCourse()">Додати</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modalBlock);
    $('#ModalForAddStudentStepFirst').modal('show');
    forSelect2WithSearchAndPageable("#groupForStudentAdding", contextPath + "student/get-group-for-select")
}
function addGroupsToCourse(){
    cleanInputs()
    if ($("#groupForStudentAdding").val().length==0) {
        validSelect2($("#groupForStudentAdding"))
        return
    }
    $('#ModalForAddStudentStepFirst').modal('hide');
}