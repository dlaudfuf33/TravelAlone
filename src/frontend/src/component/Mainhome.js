// MainHome.js
import React from "react";
import { Link } from "react-router-dom";

function MainHome() {
    return (
        <div>
            <h1>Main Home</h1>
            <ul>
                <li><Link to="/">메인홈</Link></li>
                <li><Link to="/list">게시글 목록 보기</Link></li>
                <li><Link to="/helloworld">헬로 월드</Link></li>
                <li><Link to="/signup">회원 가입</Link></li>
                <li><Link to="/create">새 게시글 작성</Link></li>
                {/* Note: 이 링크는 특정 게시물의 ID에 따라 동작하므로 예시 링크로 추가했습니다. */}
                <li><Link to="/post/1">게시글 상세 보기 (예시: ID 1)</Link></li>
                <li><Link to="/post/1/edit">게시글 수정 (예시: ID 1)</Link></li>
            </ul>
        </div>
    );
}

export default MainHome;
