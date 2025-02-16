// UTIL
let spinnerEle = document.querySelector(".spinner")
let overlay = document.querySelector(".overlay")
let hidden = document.querySelector(".hidden")

let current_score = 0;

function addOverlay() {
    overlay.classList.remove("hidden")
    spinnerEle.classList.remove("hidden")
}

function removeOverlay() {
    overlay.classList.add("hidden")
    spinnerEle.classList.add("hidden")
}

let highscoreEle = document.querySelector(".highscore")
let highscore = localStorage.getItem("highscore") || 0
highscoreEle.textContent = `High Score: ${highscore}`

let currentScoreEle = document.querySelector(".currentscore")
currentScoreEle.textContent = `Current Score: ${current_score}`

let difficulty = {
    1: "easy",
    2: "medium",
    3: "hard"
}

let diffSectionEle = document.querySelector("#diff-section")
let selectedLvl = difficulty[1] // default : easy

Object.keys(difficulty)
    .map((item) => {
        const levelEle = document.createElement("div")

        levelEle.classList.add("diff-box")
        levelEle.textContent = difficulty[item].toUpperCase()

        if (difficulty[item] === selectedLvl) {
            levelEle.classList.add("selected-lvl")
        }

        levelEle.addEventListener("click", () => {
            document.querySelectorAll(".diff-box")
                .forEach((ele => ele.classList.remove("selected-lvl")))

            levelEle.classList.add("selected-lvl")
            selectedLvl = difficulty[item]
        })

        diffSectionEle.appendChild(levelEle)
    })

let startBtn = document.querySelector("header button")

let apiResponse = null
let questionSection = document.querySelector("#question-section")

function startQuiz() {
    startBtn.classList.add("blue-btn")
    addOverlay()

    fetch(`https://opentdb.com/api.php?amount=10&difficulty=${selectedLvl}`)
        .then((response) => response.json())
        .then((data) => {
            apiResponse = data.results

            apiResponse.map((ques, index) => {
                let { category, question, correct_answer, incorrect_answers } = ques

                let options = [...incorrect_answers, correct_answer]
                options.sort(() => Math.random() - 0.5)

                // creating the options ctn
                let optionCtn = document.createElement("div")
                optionCtn.classList.add("option-ctn")

                let answeredOnce = false;

                options.forEach((opt) => {
                    let optionBox = document.createElement("div");
                    optionBox.classList.add("option-box");
                    optionBox.textContent = opt;

                    optionCtn.appendChild(optionBox);

                    optionBox.addEventListener("click", () => {
                        if (answeredOnce) return;

                        const selectedOption = optionBox.textContent

                        if (selectedOption === correct_answer) {
                            optionBox.classList.add("correct")
                            current_score += 10
                        }
                        else {
                            optionBox.classList.add("wrong")
                        }

                        answeredOnce = true;
                    })
                });

                let qCtn = document.createElement("div")
                qCtn.classList.add("q-ctn")

                qCtn.innerHTML = `
                <div class="q-header">
                    <p>${index + 1}.   ${question}</p>

                    <div>${category}</div>
                </div>
                `

                qCtn.append(optionCtn)

                questionSection.appendChild(qCtn);

                if (index + 1 === 10) {
                    if (current_score > highscore) {
                        localStorage.setItem('highscore', current_score)
                        highscore = current_score
                    }
                }
            })
            removeOverlay()
        })
        .catch((error) => console.error(`Err occurred. Err message: ${error}`))
}