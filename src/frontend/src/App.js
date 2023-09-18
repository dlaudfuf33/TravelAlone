// App.js
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom"; // Routes로 수정
import PostListView from "./component/PostListView";
import PostDetailView from "./component/PostDetailView";
import PostEditView from "./component/PostEditView";
import PostCreateView from "./component/PostCreateView";
import SignupForm from "./component/SignupForm";
import StringComponent from "./component/StringComponent";
import Mainhome from "./component/Mainhome";


function App() {
  return (
    <Router>
      <Routes> {/* Switch 대신 Routes를 사용 */}
        <Route path="/" element={<Mainhome />} />
        <Route path="/list" element={<PostListView />} />
        <Route path="/helloworld" element={<StringComponent />} />
        <Route path="/signup" element={<SignupForm />} />
        <Route path="/create" element={<PostCreateView />} />
        <Route path="/post/:id" element={<PostDetailView />} />
        <Route path="/post/:id/edit" element={<PostEditView />} />
      </Routes>
    </Router>
  );
}

export default App;
