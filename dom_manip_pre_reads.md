# DOM Manipulation Notes

## **1. Basics of the DOM**
- What is the DOM?
- Understanding how the DOM represents HTML as a tree structure
- Types of nodes in the DOM (Elements, Text, Comments, Attributes, etc.)
- Traversing the DOM (Parent, Child, Sibling relationships)

## **2. Selecting Elements in the DOM**
- `document.getElementById()`
- `document.getElementsByClassName()`
- `document.getElementsByTagName()`
- `document.querySelector()` and `document.querySelectorAll()`
- Difference between `querySelectorAll()` and `getElementsByClassName()`

## **3. Modifying HTML Content & Attributes**
- Changing text content (`innerText`, `textContent`, `innerHTML`)
- Modifying attributes (`setAttribute()`, `getAttribute()`, `removeAttribute()`)
- Manipulating classes (`classList.add()`, `classList.remove()`, `classList.toggle()`)
- Working with styles (`style.property`, `getComputedStyle()`)

## **4. Creating, Cloning, and Removing Elements**
- `document.createElement()`
- `document.createTextNode()`
- `element.appendChild()`
- `element.insertBefore()`
- `element.removeChild()`
- `element.replaceChild()`
- `element.cloneNode()`

## **5. Handling Events**
- What are Events in JavaScript?
- Event Listeners (`addEventListener()`, `removeEventListener()`)
- Mouse Events (`click`, `dblclick`, `mouseenter`, `mouseleave`, `mousemove`)
- Keyboard Events (`keydown`, `keyup`, `keypress`)
- Form Events (`submit`, `change`, `input`, `focus`, `blur`)
- Event Object (`event.target`, `event.preventDefault()`, `event.stopPropagation()`)
- Event Delegation (Efficient Event Handling)
- Bubbling vs. Capturing

## **6. Working with Forms & User Input**
- Accessing form fields (`value`, `checked`, `selected`, `files`)
- Validating user input (`pattern`, `required`, `custom validation`)
- Form submission (`submit event`, `preventDefault()`)
- Handling real-time user input (`input` and `change` events`)

## **7. Traversing the DOM**
- `parentNode`, `childNodes`, `firstChild`, `lastChild`
- `children`, `firstElementChild`, `lastElementChild`
- `nextSibling`, `previousSibling`
- `nextElementSibling`, `previousElementSibling`

## **8. Asynchronous DOM Manipulation**
- `setTimeout()`, `setInterval()`
- Fetching data and updating the DOM dynamically (AJAX & Fetch API)
- Working with Promises & `async/await`
- Using `MutationObserver` for detecting DOM changes

## **9. Animations & Effects**
- Adding and removing CSS classes dynamically
- Using JavaScript for simple animations (`setInterval()`, `requestAnimationFrame()`)
- Animating elements with the Web Animations API
- Using CSS transitions and animations with JavaScript
- Scroll-based animations (`scroll`, `IntersectionObserver`)

## **10. Optimizing DOM Manipulation for Performance**
- Minimizing reflows and repaints
- Document Fragments for bulk DOM updates
- Efficient event handling with delegation
- Throttling & Debouncing events

## **11. Working with Local Storage, Session Storage & Cookies**
- Storing & retrieving data using `localStorage`
- `sessionStorage` and its use cases
- Managing cookies using JavaScript

## **12. Manipulating CSS with JavaScript**
- Changing styles dynamically (`element.style.property`)
- Adding & removing CSS classes (`classList`)
- Managing media queries (`matchMedia` API)

## **13. Third-Party Libraries for DOM Manipulation**
- jQuery vs Vanilla JS for DOM manipulation
- GSAP (for animations)
- Intersection Observer API for efficient element observation

## **14. Advanced Topics**
- Shadow DOM and Web Components
- Virtual DOM concept (React, Vue)
- Server-Side Rendering (SSR) and its effect on DOM manipulation
