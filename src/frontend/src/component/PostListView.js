import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom"; // useNavigate 불러오기
import "../App.css";


function PostListView() {
  const [posts, setPosts] = useState([]);
  const navigate = useNavigate(); // useNavigate 사용

  useEffect(() => {
    axios.get('/api/posts')
      .then(res => setPosts(res.data))
      .catch(err => {
        console.error("게시물 목록을 불러오는 중 오류 발생:", err);
        // 오류 발생 시 사용자에게 알림 또는 다른 처리를 추가할 수 있음
      });
  }, []);

  const handleCreateClick = () => {
    // 버튼 클릭 시 페이지 이동
    navigate('/create');
  };
  return (
    <div>
      <h1>게시물 목록</h1>
      <ul>
        {posts.length > 0 ? (
          posts.map(post => (
            <li key={post.id}>
              <Link to={`/post/${post.id}`}>{post.title}</Link>
            </li>
          ))
        ) : (
          <li>No posts available</li>
        )}
      </ul>
      <button className="instagram-button" onClick={handleCreateClick}>
        게시글 작성하기
      </button>
    </div>
  );
}


export default PostListView;
