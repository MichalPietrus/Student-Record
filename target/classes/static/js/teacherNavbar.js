function printTeacherNavbar() {
    let navbar = document.getElementById("teacherNavbar");
    navbar.innerHTML = `    
    <a class="navbar-brand" href="/">Dzienniczek</a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownTeacher" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Panel Nauczyciela
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownTeacher">
                    <a class="dropdown-item" href="/nauczyciel/wybierz-klase">Dodaj oceny</a>
                    <a class="dropdown-item" href="/nauczyciel/szczegoly-uzytkownika">Szczegóły Użytkownika</a>
                </div>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/logout">Wyloguj</a>
            </li>
        </ul>
    </div>
    `;
}