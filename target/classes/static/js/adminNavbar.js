function printAdminNavbar() {
    let navbar = document.getElementById("adminNavbar");
    navbar.innerHTML = `    
    <a class="navbar-brand" href="/">Dzienniczek</a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownModerator" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Panel Moderatora
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownModerator">
                    <a class="dropdown-item" href="/moderator/lista-uczniow/0">Lista uczniów
                        (zmiana klasy)</a>
                </div>
            </li>
            <li class="nav-item active dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownAdmin" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Panel Administracyjny
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownAdmin">
                    <a class="dropdown-item" href="/admin/lista-uzytkownikow/0">Lista
                        użytkowników</a>
                    <a class="dropdown-item" href="/admin/dodaj-przedmiot">Dodaj Przedmiot</a>
                    <a class="dropdown-item" href="/admin/utworz-klase">Stwórz klasę</a>
                </div>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/logout">Wyloguj</a>
            </li>
        </ul>
    </div>
    `;
}