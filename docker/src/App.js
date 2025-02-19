import React from "react";
import { useState } from "react";

function App() {
  const [click, setClick] = useState(false);
  return (
    <div>
      <p>Click for some magic bro</p>
      <button onClick={() => setClick((prev) => !prev)}>Click madi</button>

      {click && <p>{process.env.REACT_APP_SECRET}</p>}
    </div>
  );
}

export default App;
