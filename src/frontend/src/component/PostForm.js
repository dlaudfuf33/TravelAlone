import React, { useState } from "react";
import axios from "axios";

function PostForm({ onPostAdded }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [author, setAuthor] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    const test = {
      title: test,
    };

    axios
      .post("/api/data", test)
      .then((response) => {
        alert("게시물이 성공적으로 작성되었습니다.");
        // 부모 컴포넌트로 콜백을 호출하여 게시글이 추가되었음을 알립니다.
        onPostAdded();
        // 필요한 경우, 추가적인 로직 (예: 페이지 새로고침 또는 목록 페이지로 리다이렉션)을 여기에 추가합니다.
      })
      .catch((error) => {
        alert("게시물 작성 중 오류가 발생했습니다.");
      });
  };

  return (
    <div>
      <h2>게시물 작성</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>제목</label>
          <input
            type="text"
            value={test}
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
    </div>
  );
}

export default PostForm;
