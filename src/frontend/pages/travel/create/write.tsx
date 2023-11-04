import React, { useState } from "react";
import { useRouter } from "next/router";
import axios from 'axios';
import WriteUI from "./writeUI"; // WriteUI 컴포넌트를 import

export default function Write() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    name: "",
    region: "",
    contents: "",
    userRating: 0,
    recommendedSeason: "",
    featuresString: "",
  });

  const [errors, setErrors] = useState({
    nameError: "",
    regionError: "",
    contentsError: "",
    userRatingError: "",
    recommendedSeasonError: "",
    featuresStringError: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validateForm = () => {
    let isValid = true;
    let newErrors = {
      nameError: "",
      regionError: "",
      contentsError: "",
      userRatingError: "",
      recommendedSeasonError: "",
      featuresStringError: "",
    };

    if (!formData.name) {
      isValid = false;
      newErrors.nameError = "여행지 이름을 입력해주세요.";
    }
    if (!formData.region) {
      isValid = false;
      newErrors.regionError = "지역을 입력해주세요.";
    }
    if (!formData.contents) {
      isValid = false;
      newErrors.contentsError = "내용을 입력해주세요.";
    }
    if (formData.userRating < 0 || formData.userRating > 5) {
      isValid = false;
      newErrors.userRatingError = "평점은 0에서 5 사이의 숫자여야 합니다.";
    }
    if (!formData.recommendedSeason) {
      isValid = false;
      newErrors.recommendedSeasonError = "추천 계절을 입력해주세요.";
    }
    if (!formData.featuresString) {
      isValid = false;
      newErrors.featuresStringError = "특징을 입력해주세요.";
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleEditorChange = (value) => {
    // Quill 에디터 내용 업데이트
    setFormData((prevData) => ({
      ...prevData,
      contents: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    const destinationData = {
      name: formData.name,
      region: formData.region,
      contents: formData.contents,
      userRating: formData.userRating,
      recommendedSeason: formData.recommendedSeason,
      featuresString: formData.featuresString,
    };

    // 여행지 생성 API 엔드포인트로 요청 보내기
    try {
      const response = await axios.post('/api/destinations/create', destinationData);

      if (response.data.id) {
        alert("여행지가 성공적으로 생성되었습니다.");
        router.push(`/travel/recommend/recommend`);
      } else {
        alert("여행지 생성에 실패하였습니다.");
      }
    } catch (error) {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <WriteUI
      formData={formData}
      errors={errors}
      handleChange={handleChange}
      handleSubmit={handleSubmit}
      handleEditorChange={handleEditorChange}
    />
  );
}
