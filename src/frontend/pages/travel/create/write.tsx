import React, { useState, useEffect, useRef } from "react";
import { useRouter } from "next/router";
import axios from 'axios';
import UpdateUI from "./UpdateUI";
import 'react-quill/dist/quill.snow.css';
import dynamic from 'next/dynamic';

const UpdateQuillEditorDynamic = dynamic(
  () => import('../../../src/components/CustomEditor/MyQuillEditor'),
  { ssr: false }
);

export default function Update() {
  const router = useRouter();
  const { id: destinationId, contents: initialContents } = router.query;

  const [formData, setFormData] = useState({
    name: "",
    region: "",
    contents: "", // 기본값을 비어 있는 문자열로 설정
    averageRating: "",
    recommendedSeason: "",
    featuresString: "",
  });

  const [isLoading, setIsLoading] = useState(true);

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
              contents: initialContents || "", // initialContents가 존재하면 사용하고, 아니면 비어 있는 문자열로 설정
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

  const quillRef = useRef(null);

  useEffect(() => {
    if (quillRef.current) {
      const Quill = UpdateQuillEditorDynamic.default || UpdateQuillEditorDynamic;
      const quill = new Quill(quillRef.current, {
        theme: "snow",
        modules: {
          toolbar: [
            ["bold", "italic", "underline"],
            [{ list: "ordered" }, { list: "bullet" }],
            ["image", "video"],
            [{ header: [1, 2, 3, 4, 5, 6, false] }],
            [{ color: [] }, { background: [] }],
            [{ size: ["small", false, "large", "huge"] }],
          ],
        },
      });

      quill.on("text-change", () => {
        setFormData({
          ...formData,
          contents: quill.root.innerHTML,
        });
      });

      if (initialContents) {
        quill.clipboard.dangerouslyPasteHTML(initialContents);
      }
    }
  }, [initialContents, UpdateQuillEditorDynamic, formData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const updateData = {
      name: formData.name,
      region: formData.region,
      contents: formData.contents,
      averageRating: formData.averageRating,
      recommendedSeason: formData.recommendedSeason,
      featuresString: formData.featuresString,
    };

    try {
      const response = await axios.post(`http://localhost:8080/api/destinations/update/${destinationId}`, updateData);

      if (response.status === 200) {
        alert("여행지 정보가 성공적으로 업데이트되었습니다.");
        router.push(`/boards/${destinationId}`);
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
      handleSubmit={handleSubmit}
      MyQuillEditor={UpdateQuillEditorDynamic}
      quillRef={quillRef}
    />
  );
}
