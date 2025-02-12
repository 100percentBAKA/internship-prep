import axios from "axios";

axios.get("https://jsonplaceholder.typicode.com/todos")
    .then((response) => {
        if (!response.ok) {
            throw new Error("request failed")
        }

        return response
    })
    .then((data) => console.log(data))
    .catch((err) => console.error(`Error occurred, err message: ${err}`))