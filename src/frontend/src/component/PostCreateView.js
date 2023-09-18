import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function PostCreateView({ onPostAdded }) {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [author, setAuthor] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    const postData = {
      title: title,
      content: content,
      author: author,
    };

    axios
      .post("/api/posts", postData)
      .then((response) => {
        alert("게시물이 성공적으로 작성되었습니다.");
        // 페이지 이동
        navigate("/");
      })
      .catch((error) => {
        alert("게시물 작성 중 오류가 발생했습니다.");
      });
  };

  return (
    <div className="instagram-style-container">
      <h1 className="instagram-style-heading">게시물 작성</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>제목</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <label>내용</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
          ></textarea>
        </div>
        <div>
          <label>작성자</label>
          <input
            type="text"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
          />
        </div>
        <button type="submit">게시물 작성</button>
      </form>
      <button className="instagram-button" onClick={() => navigate("/")}>
        게시글 목록
      </button>
    </div>
  );
}

export default PostCreateView;
