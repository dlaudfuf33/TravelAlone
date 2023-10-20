import React, { useState, useEffect } from 'react';
import axios from 'axios';

type Destination = {
  id: number;
  features: string;
  description: string;
  imageUrl: string;
  name: string;
  averageRating: number;
};

function Destinations() {
  const [destinations, setDestinations] = useState<Destination[]>([]);

  useEffect(() => {
    // 백엔드 API에서 여행지 목록을 가져옵니다.
    axios.get('/api/destinations') // API 엔드포인트를 적절하게 수정해야 합니다.
      .then(response => {
        setDestinations(response.data);
      })
      .catch(error => {
        console.error("여행지 목록을 가져오는 중 오류 발생:", error);
      });
  }, []);

  const handleImageClick = (destinationId: number) => {
    console.log(`여행지 ${destinationId} 클릭됨!`);

    // 백엔드 API를 호출하여 사용자 활동을 저장합니다.
    axios.post('/api/userActivity', { destinationId }) // API 엔드포인트와 데이터 형식을 적절하게 수정해야 합니다.
      .then(response => {
        console.log("사용자 활동 저장 성공:", response.data);
      })
      .catch(error => {
        console.error("사용자 활동 저장 중 오류 발생:", error);
      });
  };

  return (
    <div>
      {destinations.map(destination => (
        <div key={destination.id}>
          <h2>{destination.name}</h2>
          <p>{destination.description}</p>
          <p>{destination.features}</p>
          <img
            src={destination.imageUrl}
            alt={destination.name}
            onClick={() => handleImageClick(destination.id)}
          />
          <p>Rating: {destination.averageRating}</p>
        </div>
      ))}
    </div>
  );
}

export default Destinations;
