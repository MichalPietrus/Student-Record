function printStudentNavbar() {
    let navbar = document.getElementById("studentNavbar");
    navbar.innerHTML = `    
    <a class="navbar-brand" href="/">Dzienniczek</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownStudent" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Panel Ucznia
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownStudent">
                    <a class="dropdown-item" href="/uczen/oceny">Oceny</a>
                    <a class="dropdown-item" href="/uczen/szczegoly-uzytkownika">Szczegóły Użytkownika</a>
                </div>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/logout">Wyloguj</a>
            </li>
        </ul>
    </div>
    `;
}