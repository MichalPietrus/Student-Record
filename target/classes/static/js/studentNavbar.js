function printStudentNavbar() {
    let navbar = document.getElementById("studentNavbar");
    navbar.innerHTML = `    
    <a class="navbar-brand" href="/">Dzienniczek</a>
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