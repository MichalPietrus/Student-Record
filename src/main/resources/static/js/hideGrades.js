function hideGrades() {
    let selectedCategory = $("#category :selected").text();
    console.log(selectedCategory)
    let ratingSelect = document.getElementById("ratingSelect");
    let ratingWeightSelect = document.getElementById("ratingWeightSelect");
    let ratingLabel = document.getElementById("ratingLabel");
    let ratingWeightLabel = document.getElementById("ratingWeightLabel");
    let ratingBr = document.getElementById("ratingBr");
    let ratingWeightBr = document.getElementById("ratingWeightBr");
    let table = [ratingSelect, ratingWeightSelect, ratingLabel, ratingWeightLabel, ratingBr, ratingWeightBr]
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