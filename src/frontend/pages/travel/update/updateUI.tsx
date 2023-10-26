import React, { useEffect, useRef } from "react";
import dynamic from 'next/dynamic';

const MyQuillEditorDynamic = dynamic(
  () => import('../../../src/components/CustomEditor/UpdateQuillEditor'),
  { ssr: false }
);

interface UpdateUIProps {
  formData: any;
  handleChange: any;
  handleEditorChange: (value: string) => void;
  handleSubmit: any;
  MyQuillEditor: any;
  quillRef: React.MutableRefObject<null>;
}

const UpdateUI: React.FC<UpdateUIProps> = ({
  formData,
  handleChange,
  handleEditorChange,
  handleSubmit,
  MyQuillEditor,
  quillRef,
}) => {

  useEffect(() => {
    if (!quillRef.current) return;

    const Quill = MyQuillEditor.default || MyQuillEditor;
    if (!Quill) return;

    if (quillRef.current.querySelector(".ql-container")) return;

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
      handleEditorChange(quill.root.innerHTML);
    });

    return () => {
      quill.off("text-change");
    };
  }, [quillRef, MyQuillEditor]);

  useEffect(() => {
    if (quillRef.current && quillRef.current.querySelector(".ql-container")) {
      const quill = quillRef.current.querySelector(".ql-container").Quill;
      if (quill && quill.root.innerHTML !== formData.contents) {
        quill.clipboard.dangerouslyPasteHTML(formData.contents);
      }
    }
  }, [formData.contents, quillRef]);

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>이름:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>지역:</label>
          <input
            type="text"
            name="region"
            value={formData.region}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>내용:</label>
          <MyQuillEditor
            value={formData.contents}
            onChange={handleEditorChange}
            quillRef={quillRef}
          />
        </div>
        <div>
          <label>평균 평점:</label>
          <input
            type="text"
            name="averageRating"
            value={formData.averageRating}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>추천 시기:</label>
          <input
            type="text"
            name="recommendedSeason"
            value={formData.recommendedSeason}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>특징:</label>
          <input
            type="text"
            name="featuresString"
            value={formData.featuresString}
            onChange={handleChange}
          />
        </div>
        <button type="submit">업데이트</button>
      </form>
    </div>
  );
};

export default UpdateUI;
