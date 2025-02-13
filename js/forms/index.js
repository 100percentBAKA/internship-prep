let heading = document.querySelector(".header-title")
const headingTitle = heading.textContent

function changeHeader() {
    let input = document.querySelector("#input-heading")

    heading.textContent = input.value || headingTitle;
}

const displaySection = document.querySelector("#display-section")

function handleSubmit(event) {
    event.preventDefault()

    displaySection.innerHTML = ""

    let name = document.querySelector("#form-section form #input-name").value

    let age = document.querySelector("#form-section form #input-age").value

    const para = document.createElement("p")
    para.textContent = `Name is: ${name}, Age is: ${age}`

    displaySection.appendChild(para)
}