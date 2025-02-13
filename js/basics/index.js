let clicked = false;
let initialTitle = document.getElementsByClassName("header-title")[0].textContent

function changeTitle() {
    if (!clicked) {
        clicked = true;

        document.getElementsByClassName("header-title")[0].textContent = "Hello, DOM!"
    }
    else {
        clicked = false

        document.getElementsByClassName("header-title")[0].textContent = initialTitle
    }
}

let initialImage = document.querySelector("#image-section img").getAttribute("src")

let imgButtonClicked = false;

function changeImg() {
    if (imgButtonClicked) {
        imgButtonClicked = false

        document.querySelector("#image-section img").setAttribute("src", "https://picsum.photos/200")
    }
    else {
        imgButtonClicked = true

        document.querySelector("#image-section img").setAttribute("src", initialImage)
    }
}

let themeClicked = false;

function toggleTheme() {
    if (!themeClicked) {
        themeClicked = true

        document.querySelector("body").style.backgroundColor = "gray"
    }
    else {
        themeClicked = false;

        document.querySelector("body").style.backgroundColor = "white"
    }
}

function createList() {
    const enteredText = document.querySelector("#list-section input").value.trim()

    if (enteredText !== "") {
        const ul = document.querySelector("#list-section #list-ul")

        const list = document.createElement("li")
        list.innerText = enteredText

        ul.appendChild(list)
    }
}