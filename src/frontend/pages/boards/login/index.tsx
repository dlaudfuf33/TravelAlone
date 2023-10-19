import React, { useState } from "react";
import type { ChangeEvent } from "react";
import axios from "axios";
import styled from "@emotion/styled";

// ... 나머지 import 구문 ...

const Body = styled.div`
  overflow: hidden;
  height: 1000px;
  background: url("https://blog.kakaocdn.net/dn/sZ7ux/btqDSzwx1NN/c0FYKBDM9WacVK61NpYar1/img.jpg");
  margin: 0px;
  font-family: "Ubuntu", sans-serif;
  background-size: 100% 110%;

  h1,
  h2,
  h3,
  h4,
  h5,
  h6,
  a {
    margin: 0;
    padding: 0;
  }
`;

const Login = styled.div`
  // 원하는 스타일을 적용하세요.
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  max-width: 500px;
  height: 90vh;
`;

const LoginHeader = styled.div`
  // 원하는 스타일을 적용하세요.
  color: #fff;
  text-align: center;
  font-size: 300%;
`;

const LoginForm = styled.div`
  // 원하는 스타일을 적용하세요.
  border: 0.5px solid #fff;
  background: #722ed117;
  border-radius: 10px;
  box-shadow: 0px 0px 10px #000;
  box-sizing:border-box;
  padding-top:15px;
  padding-bottom:10%;
  margin:5% auto;
  text-align:center;
  width: 350px;
  height: 350px;

  h3 {
    text-align: left;
    margin-left: 40px;
    color: #fff;

    @media only screen and (min-width : 500px) and (max-width : 600px){
      h3 {
        text-align:center;
        margin:0;
      }
      .sign-up, .no-access {
        margin:10px 0;
      }
      .login-button {
        margin-bottom:10px;
      }
    }
`;

const Input = styled.input`
  max-width: 400px;
  width: 80%;
  line-height: 3em;
  font-family: "Ubuntu", sans-serif;
  margin: 1em 2em;
  border-radius: 5px;
  border: 2px solid #f2f2f2;
  outline: none;
  padding-left: 10px;

  &[type="password"],
  &[type="text"] {
    outline: none;
    padding-left: 10px;
  }
`;

const LoginButton = styled.input`
  // 원하는 스타일을 적용하세요.
  height: 30px;
  width: 100px;
  background: #fff;
  border: 1px solid #f2f2f2;
  border-radius: 20px;
  color: slategrey;
  text-transform: uppercase;
  font-family: "Ubuntu", sans-serif;
  cursor: pointer;

  &[type="button"] {
    cursor: pointer;
  }
`;

const SignUpLink = styled.a`
  color: #f2f2f2;
  margin-left: -70%;
  cursor: pointer;
  text-decoration: underline;
`;

const NoAccessLink = styled.h6`
  // 원하는 스타일을 적용하세요.
  color: #e86850;
  margin: 20px 0px 20px -57%;
  text-decoration: underline;
  cursor: pointer;
`;

// const ErrorPage = styled.div`
//   // 원하는 스타일을 적용하세요.
//   color: #f2f2f2;
//   text-decoration: underline;
//   cursor: pointer;
// `;

export default function LoginPage(): JSX.Element {
  const [userid, setUserid] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const handleUsernameChange = (e: ChangeEvent<HTMLInputElement>): void => {
    setUserid(e.target.value);
  };

  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>): void => {
    setPassword(e.target.value);
  };

  const handleSubmit = async (): Promise<void> => {
    try {
      // 서버에 로그인 요청
      const response = await axios.post("http://localhost:8080/api/users/login",
        {
          userid: userid, // 사용자명과 비밀번호를 요청 본문에 담아 보냅니다.
          password: password,
        }
      );
  
      const token: string | undefined = response.data.token;
  
      if (token !== undefined && token !== "") {
        localStorage.setItem("token", token);
        console.log("로그인 성공. 토큰이 저장되었습니다.");
      } else {
        throw new Error("토큰이 존재하지 않습니다.");
      }
    } catch (error) {
      console.error("로그인 실패", error);
    }
  };

  return (
    <Body>
      <Login>
        <LoginHeader>
          <h1>Login</h1>
        </LoginHeader>
        <LoginForm>
          <h3>Username:</h3>
          <Input
            type="text"
            placeholder="Username"
            onChange={handleUsernameChange}
          />

          <h3>Password:</h3>
          <Input
            type="password"
            placeholder="Password"
            onChange={handlePasswordChange}
          />
          <br />
          {/* LoginButton의 클릭 이벤트 핸들러로 handleSubmit 함수를 설정 */}
          <LoginButton type="button" value="Login" onClick={handleSubmit} />
          <br />
          <SignUpLink>Sign Up!</SignUpLink>
          <br />
          <NoAccessLink> Can't access your account?</NoAccessLink>
        </LoginForm>

        {/* You can add the ErrorPage component here when you need it */}
      </Login>
    </Body>
  );
}
