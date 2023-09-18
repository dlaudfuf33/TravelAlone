import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom"; // useNavigate로 수정

function PostEditView() {
  const { id } = useParams();
  const navigate = useNavigate(); // useNavigate로 수정
  const [post, setPost] = useState(null);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [author, setAuthor] = useState("");

  useEffect(() => {
    axios.get(`/api/posts/list/${id}`)
      .then(res => {
        const postData = res.data;
        setPost(postData);
        setTitle(postData.title);
        setContent(postData.content);
        setAuthor(postData.author);
      })
      .catch(err => console.log(err))
  }, [id]);

  const handleUpdateClick = () => {
    const updatedPostData = {
      title: title,
      content: content,
      author: author,
    };

    axios.put(`/api/posts/view/${id}`, updatedPostData)
      .then(() => {
        alert('게시물이 수정되었습니다.');
        // 수정 후 해당 게시물의 상세 페이지로 이동
        navigate(`/post/${id}`); // navigate로 수정
      })
      .catch(err => console.log(err));
  };

  return (
    <div>
      <h1>게시물 수정</h1>
      {post ? (
        <div>
          <div>
            <label>제목</label>
            <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
          </div>
          <div>
            <label>내용</label>
            <textarea value={content} onChange={(e) => setContent(e.target.value)}></textarea>
          </div>
          <div>
            <label>작성자</label>
            <input type="text" value={author} onChange={(e) => setAuthor(e.target.value)} />
          </div>
          <button onClick={handleUpdateClick}>수정 완료</button>
        </div>
      ) : (
        <p>게시물을 불러오는 중...</p>
      )}
    </div>
  );
}

export default PostEditView;
