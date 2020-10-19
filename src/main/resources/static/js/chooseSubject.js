function chooseSubject() {
    let chooseSubject = document.getElementById("chooseSubject");
    let choosedSchoolClassName = $("#schoolClass :selected").text();
    if (choosedSchoolClassName !== '--') {
        $.ajax({
            url: "/subject/" + choosedSchoolClassName,
            method: "POST",
            contentType: "application/json",
            data: choosedSchoolClassName,
            dataType: 'json',
        }).done(function (data) {
            console.log(data);

            chooseSubject.innerHTML = `            
            <label for="chooseSubjectSelect">Wybierz przedmiot</label>
                <select id="chooseSubjectSelect" name="subject.name" class="form-control" required>              
                </select>
            `;

            let chooseSubjectSelect = document.getElementById("chooseSubjectSelect");

            chooseSubjectSelect.innerHTML = `<option value="">--</option>`;

            $.each(data, function (index) {
                console.log(data[index])

                let row = `
            <option value="${data[index]}">${data[index]}</option>`;

                chooseSubjectSelect.innerHTML += row;
            });
        }).fail(function (data) {
            console.log(data);
        });
    } else chooseSubject.innerHTML = ` `;
}