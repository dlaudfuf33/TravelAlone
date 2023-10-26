// Update.tsx
import React, { useState, useEffect, useRef } from "react";
import { useRouter } from "next/router";
import axios from 'axios';
import UpdateUI from "./UpdateUI";
import 'react-quill/dist/quill.snow.css';
import dynamic from 'next/dynamic';

const UpdateQuillEditorDynamic = dynamic(
  () => import('../../../src/components/CustomEditor/UpdateQuillEditor'),
  { ssr: false }
);

export default function Update() {
  const router = useRouter();
  const { id: destinationId, contents: initialContents } = router.query;

  const [formData, setFormData] = useState({
    name: "",
    region: "",
    contents: initialContents || "",
    averageRating: "",
    recommendedSeason: "",
    featuresString: "",
  });

  const [isLoading, setIsLoading] = useState(true);
  const quillRef = useRef(null);

  useEffect(() => {
    if (destinationId) {
      const fetchDestination = async () => {
        try {
          const response = await axios.get(`http://localhost:8080/api/destinations/view/${destinationId}`);
          if (response.status === 200) {
            const destination = response.data;
            setFormData({
              name: destination.name,
              region: destination.region,
              contents: initialContents || "",
              averageRating: destination.averageRating,
              recommendedSeason: destination.bestSeason,
              featuresString: destination.features.join(', '),
            });
            setIsLoading(false);
          } else {
            alert(`서버에서 오류 응답을 받았습니다. 상태 코드: ${response.status}`);
          }
        } catch (error) {
          alert(`여행지 정보를 가져오는 중 오류가 발생했습니다. 오류 메시지: ${error.message}`);
        }
      };

      fetchDestination();
    }
  }, [destinationId, initialContents]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleEditorChange = (value) => {
    setFormData({
      ...formData,
      contents: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const updateData = {
      updateFields: {
        name: formData.name,
        region: formData.region,
        contents: formData.contents,
        userRating: formData.averageRating,
        recommendedSeason: formData.recommendedSeason,
        featuresString: formData.featuresString,
      },
    };

    try {
      const response = await axios.post(`http://localhost:8080/api/destinations/update/${destinationId}`, updateData);

      if (response.status === 200) {
        alert("여행지 정보가 성공적으로 업데이트되었습니다.");
        router.push(`/travel/recommend/recommend`);
      } else {
        alert("업데이트에 실패하였습니다.");
      }
    } catch (error) {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  };


  if (isLoading) {
    return <div>데이터를 불러오는 중...</div>;
  }

  return (
    <UpdateUI
      formData={formData}
      handleChange={handleChange}
      handleEditorChange={handleEditorChange} // handleEditorChange 함수를 전달
      handleSubmit={handleSubmit}
      MyQuillEditor={UpdateQuillEditorDynamic}
      quillRef={quillRef}
    />
  );
}
