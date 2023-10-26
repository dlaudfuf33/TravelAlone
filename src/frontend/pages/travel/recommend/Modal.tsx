import styles from './styles/recommend.module.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';

type Destination = {
  id: number;
  name: string;
  contents: string;
  imageUrl: string | null;
  features: string[];
  region: string;
  bestSeason: string;
  totalRating: number;
  averageRating: number;
  ratingCount: number;
  createdAt: string;
  updatedAt: string;
};

type ModalProps = {
  destination: Destination;
  onClose: () => void;
};

const Modal: React.FC<ModalProps> = ({ destination, onClose }) => {
  const [isImageZoomed, setImageZoomed] = useState(false);
  const [isImageClicked, setImageClicked] = useState(false);

  useEffect(() => {
    const startTime = Date.now();
    console.log(startTime);

    return () => {
      const closeTime = Date.now();
      sendActivityData(startTime, closeTime);
      console.log(closeTime);
    };
  }, []);

  const handleImageClick = () => {
    setImageClicked(true);
    setImageZoomed(!isImageZoomed);
  };

  useEffect(() => {
    console.log(isImageClicked);
  }, [isImageClicked]);

  const sendActivityData = async (startTime: number, closeTime: number) => {
    const activityData = {
      photoClicked: isImageClicked,
      startTime: startTime,
      endTime: closeTime,
      destination: destination,
    };

    try {
      const response = await axios.post('http://localhost:8080/api/activities/collect', activityData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('authToken')}`,
        },
      });

      if (response.status === 200) {
        console.log('활동 데이터가 성공적으로 저장되었습니다.');
      }
    } catch (error) {
      console.error('활동 데이터 저장 중 오류 발생:', error);
    }
  };

  const handleEditClick = () => {
    window.location.href = `http://localhost:3000/travel/update/update?id=${destination.id}&contents=${encodeURIComponent(destination.contents)}`;
  };

  return (
    <div className={styles['modal-overlay']} onClick={onClose}>
      <div className={styles['modal-content']} onClick={(e) => e.stopPropagation()}>
        <div className={styles['modal-image-container']}>
          <img
            src={destination.imageUrl || ''}
            alt={destination.name}
            className={`${styles['modal-image']} ${isImageZoomed ? styles['zoomed'] : ''}`}
            onClick={handleImageClick}
          />
        </div>
        <div className={styles['modal-details']}>
          <h2 className={styles['destination-name']}>{destination.name}</h2>
          {/* <h2 className={styles['destination-name']}>{destination.contents}</h2> */}
          <div className={styles['modal-description']}>
            {destination.features.map((feature, index) => (
              <span key={index} className={styles['hashtag']}>
                {feature}
              </span>
            ))}
          </div>
          <p className={styles['modal-rating']}>Rating: {destination.averageRating}</p>
        </div>
        <button className={styles['modal-edit-button']} onClick={handleEditClick}>
          수정
        </button>
        <button className={styles['modal-close-button']} onClick={onClose}>
          닫기
        </button>
      </div>
    </div>
  );
};

export default Modal;
