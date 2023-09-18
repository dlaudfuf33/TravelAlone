// StringComponent.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';


function StringComponent() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    axios.get('/api/test') // Spring Boot 백엔드의 엔드포인트
      .then(response => {
        setMessage(response.data);
      })
      .catch(error => {
        console.error('API 호출 중 오류:', error);
      });
  }, []);

  return (
    <div>
      <h1>프론트엔드에서 받은 문자열:</h1>
      <p>{message}</p>
    </div>
  );
}

export default StringComponent;
