import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link, useParams, useNavigate } from "react-router-dom"; // useNavigate로 변경

function PostDetailView() {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const navigate = useNavigate(); // useNavigate 사용

  useEffect(() => {
    axios.get(`/api/posts/list/${id}`)
      .then(res => setPost(res.data))
      .catch(err => console.log(err))
  }, [id]);

  const handleDeleteClick = () => {
    axios.delete(`/api/posts/delete/${id}`)
      .then(() => {
        alert('게시물이 삭제되었습니다.');
        // 삭제 후 게시물 목록 페이지로 이동
        navigate('/'); // useNavigate로 이동
      })
      .catch(err => console.log(err));
  };

  return (
    <div>
      <h1>게시물 상세 보기</h1>
      {post ? (
        <div>
          <h2>{post.title}</h2>
          <p>{post.content}</p>
          <p>작성자: {post.author}</p>
          <button onClick={handleDeleteClick}>삭제</button>
          <Link to={`/post/${id}/edit`}>수정</Link>
        </div>
      ) : (
        <p>게시물을 불러오는 중...</p>
      )}
    </div>
  );
}

export default PostDetailView;
