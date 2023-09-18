import React, { useState } from "react";
import { useNavigate } from "react-router-dom"
import axios from "axios";


function SignupForm() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    profileImageUrl: "",
    nickname: "",
    dateOfBirth: "",
    gender: "",
    introduction: "",
  });

  const [isSignupSuccess, setIsSignupSuccess] = useState(false);

  const navigate = useNavigate();


  // 입력 필드 값이 변경될 때 호출되는 함수
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  // 폼 제출 시 실행되는 함수
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // 회원가입 데이터를 서버로 전송
      const response = await axios.post("/api/signup", formData);
  
      if (response.data === "가입 successfully!") {
        // 회원가입 성공시 로그 메시지 출력 및 상태 변경
        console.log("회원가입 성공:", response.data);
        setIsSignupSuccess(true);
        navigate("/"); // 성공 후 원하는 경로로 이동
      } else {
        // 응답 내용이 "가입 successfully!"가 아닌 경우 오류 처리
        alert("회원가입 실패!\n"+response.data);
        navigate("/signup"); // 회원가입 페이지로 다시 이동
      }
    } catch (error) {
      // 서버 에러 또는 네트워크 에러 발생시 처리
      console.error("회원가입 오류:", error);
      alert("회원가입 실패!");
      navigate("/signup"); // 회원가입 페이지로 다시 이동
    }
  };

  const handleChangeemail = (e) => {
    const { name, value } = e.target;

    if (name === "emailPrefix" || name === "emailDomain") {
      const newEmail = `${formData.emailPrefix}${formData.emailDomain}`;
      setFormData(prevState => ({ ...prevState, email: newEmail }));
    }

    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>사용자 아이디:</label>
        <input
          type="text"
          name="login_id"
          value={formData.username}
          onChange={handleChange}
        />
      </div>
      <div>
        <label>비밀번호:</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />
      </div>
      <div>
        <label>이메일:</label>
        <input
          type="text"
          name="emailPrefix"
          value={formData.emailPrefix}
          onChange={handleChangeemail}
          placeholder="Your email"
        />
        <select
          name="emailDomain"
          value={formData.emailDomain}
          onChange={handleChangeemail}
        >
          <option value="@example.com">@example.com</option>
          <option value="@company.com">@company.com</option>
          <option value="@mail.com">@mail.com</option>
          {/* 여기에 다른 도메인 옵션을 추가할 수 있습니다. */}
        </select>
      </div>
      <div>
        <label>닉네임:</label>
        <input
          type="text"
          name="nickname"
          value={formData.nickname}
          onChange={handleChange}
        />
      </div>
      <div>
        <label>생년월일:</label>
        <input
          type="date"
          name="dateOfBirth"
          value={formData.dateOfBirth}
          onChange={handleChange}
        />
      </div>
      <div>
        <label>성별:</label>
        <label>
          <input
            type="radio"
            name="gender"
            value="남성"
            checked={formData.gender === "남성"}
            onChange={handleChange}
          />
          남성
        </label>
        <label>
          <input
            type="radio"
            name="gender"
            value="여성"
            checked={formData.gender === "여성"}
            onChange={handleChange}
          />
          여성
        </label>
      </div>
      <div>
        <label>소개:</label>
        <textarea
          name="introduction"
          value={formData.introduction}
          onChange={handleChange}
        ></textarea>
      </div>
      <button type="submit">Sign Up</button>
    </form>
  );
}

export default SignupForm;
