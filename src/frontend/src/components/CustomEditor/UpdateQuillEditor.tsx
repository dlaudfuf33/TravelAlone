import React, { useEffect, useRef } from "react";
import Quill from "quill";
import "quill/dist/quill.snow.css";

interface UpdateQuillEditorProps {
  value: string;
  onChange: (value: string) => void;
}

const UpdateQuillEditor: React.FC<UpdateQuillEditorProps> = ({ value, onChange }) => {
  const quillRef = useRef(null);
  const quillInstanceRef = useRef<Quill | null>(null);

  useEffect(() => {
    if (!quillRef.current || quillInstanceRef.current) return;

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
      onChange(quill.root.innerHTML);
    });

    quillInstanceRef.current = quill;

    return () => {
      quill.off("text-change");
    };
  }, []);

  useEffect(() => {
    const quill = quillInstanceRef.current;
    if (quill && quill.root.innerHTML !== value) {
      quill.clipboard.dangerouslyPasteHTML(value);
    }
  }, [value]);

  return <div ref={quillRef} style={{ height: "300px" }} />;
};

export default UpdateQuillEditor;
