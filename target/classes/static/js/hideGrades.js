function hideGrades() {
    let selectedCategory = $("#category :selected").text();
    let ratingGroup = document.getElementById("ratingGroup");
    let ratingWeightGroup = document.getElementById("ratingWeightGroup");
    let ratingSelect = document.getElementById("ratingSelect");
    let ratingWeightSelect = document.getElementById("ratingWeightSelect");
    let table = [ratingGroup, ratingWeightGroup, ratingSelect, ratingWeightSelect]
    if (selectedCategory === 'bz' || selectedCategory === 'zw' || selectedCategory === 'np') {
        table.forEach(hide);
    } else {
        table.forEach(show);
    }

    function hide(item) {
        item.hidden = true;
        item.disabled = true;
    }

    function show(item) {
        item.hidden = false;
        item.disabled = false;
    }

}