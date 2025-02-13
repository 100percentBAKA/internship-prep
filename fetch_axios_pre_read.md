# HTTP Requests in JavaScript

## 1. Using Fetch API

The Fetch API provides a modern, promise-based way to make HTTP requests. It is built into JavaScript and allows for flexible handling of requests and responses.

### Making a GET Request
```javascript
fetch('https://jsonplaceholder.typicode.com/posts/1')
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
  })
  .then(data => console.log(data))
  .catch(error => console.error('Fetch error:', error));
```

### Making a POST Request
```javascript
fetch('https://jsonplaceholder.typicode.com/posts', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ title: 'New Post', body: 'This is the content', userId: 1 })
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Fetch error:', error));
```

### Handling Errors in Fetch
Since Fetch does not reject on HTTP errors (like 404 or 500), always check `response.ok` before processing the response.

```javascript
fetch('https://jsonplaceholder.typicode.com/invalid-url')
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP Error! Status: ${response.status}`);
    }
    return response.json();
  })
  .catch(error => console.error('Error fetching data:', error));
```

---

## 2. Using Axios

Axios is a popular JavaScript library for making HTTP requests. It provides a more concise syntax and built-in error handling.

### Installing Axios
Axios needs to be installed via npm or included via CDN.

#### Install via npm:
```sh
npm install axios
```

#### Include via CDN:
```html
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
```

### Making a GET Request
```javascript
axios.get('https://jsonplaceholder.typicode.com/posts/1')
  .then(response => console.log(response.data))
  .catch(error => console.error('Axios error:', error));
```

### Making a POST Request
```javascript
axios.post('https://jsonplaceholder.typicode.com/posts', {
  title: 'New Post',
  body: 'This is the content',
  userId: 1
})
  .then(response => console.log(response.data))
  .catch(error => console.error('Axios error:', error));
```

### Handling Errors in Axios
Axios automatically rejects the promise for HTTP errors, making error handling simpler.

```javascript
axios.get('https://jsonplaceholder.typicode.com/invalid-url')
  .catch(error => {
    if (error.response) {
      console.error('Error Response:', error.response.status, error.response.data);
    } else if (error.request) {
      console.error('No response received:', error.request);
    } else {
      console.error('Request error:', error.message);
    }
  });
```

---

## 3. Best Practices for Making HTTP Requests

### ✅ Use Async/Await for Readability
```javascript
async function fetchData() {
  try {
    let response = await fetch('https://jsonplaceholder.typicode.com/posts/1');
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    let data = await response.json();
    console.log(data);
  } catch (error) {
    console.error('Fetch error:', error);
  }
}
fetchData();
```

### ✅ Use Global Error Handling for Axios
```javascript
axios.interceptors.response.use(
  response => response,
  error => {
    console.error('Global Axios Error:', error.response?.status, error.message);
    return Promise.reject(error);
  }
);
```

### ✅ Set Default Headers for API Calls
```javascript
axios.defaults.headers.common['Authorization'] = 'Bearer your-token';
axios.defaults.headers.post['Content-Type'] = 'application/json';
```

### ✅ Handle Timeouts
```javascript
axios.get('https://jsonplaceholder.typicode.com/posts', { timeout: 5000 })
  .then(response => console.log(response.data))
  .catch(error => console.error('Request timeout:', error));
```

### ✅ Use a Base URL for API Requests
```javascript
const apiClient = axios.create({
  baseURL: 'https://jsonplaceholder.typicode.com',
  timeout: 5000,
});

apiClient.get('/posts/1')
  .then(response => console.log(response.data))
  .catch(error => console.error('API Client Error:', error));
```

---

This guide covers the basics of using Fetch API and Axios for HTTP requests in JavaScript, along with best practices to improve performance and maintainability.
