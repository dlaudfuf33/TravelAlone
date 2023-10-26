// BoardWriteUI.js

import React from "react";
import * as S from "../../../src/components/units/board/write/BoardWrite.styles";
import type { IBoardWriteUIProps } from "../../../src/components/units/board/write/BoardWrite.types"; // 여기에 따옴표 추가
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import dynamic from 'next/dynamic';

const MyQuillEditor = dynamic(
  () => import('../../../src/components/CustomEditor/MyQuillEditor'),
  { ssr: false }
);

export default function BoardWriteUI({
  formData, errors, handleChange, handleSubmit, handleEditorChange
}: IBoardWriteUIProps): JSX.Element {

  return (
    <S.Wrapper>
      <S.Title>게시글 등록</S.Title>
      <form onSubmit={handleSubmit}>
        <S.InputWrapper>
          <S.Label>여행지 이름</S.Label>
          <S.Writer
            type="text"
            name="name"
            placeholder="여행지 이름을 입력하세요."
            value={formData.name}
            onChange={handleChange}
          />
          <S.Error>{errors.nameError}</S.Error>
        </S.InputWrapper>
        <S.InputWrapper>
          <S.Label>지역</S.Label>
          <S.Subject
            type="text"
            name="region"
            placeholder="여행지 지역을 입력하세요."
            value={formData.region}
            onChange={handleChange}
          />
          <S.Error>{errors.regionError}</S.Error>
        </S.InputWrapper>
        <S.InputWrapper>
          <S.Label>내용</S.Label>
          <MyQuillEditor onChange={handleEditorChange} value={formData.contents} />
          <S.Error>{errors.contentsError}</S.Error>
        </S.InputWrapper>
        <S.InputWrapper>
          <S.Label>평점</S.Label>
          <S.Subject
            type="number"
            name="userRating"
            placeholder="평점을 입력하세요 (0-5 사이)."
            value={formData.userRating}
            onChange={handleChange}
          />
          <S.Error>{errors.userRatingError}</S.Error>
        </S.InputWrapper>
        <S.InputWrapper>
          <S.Label>추천 계절</S.Label>
          <S.Subject
            type="text"
            name="recommendedSeason"
            placeholder="추천 계절을 입력하세요."
            value={formData.recommendedSeason}
            onChange={handleChange}
          />
          <S.Error>{errors.recommendedSeasonError}</S.Error>
        </S.InputWrapper>
        <S.InputWrapper>
          <S.Label>특징</S.Label>
          <S.Subject
            type="text"
            name="featuresString"
            placeholder="여행지의 특징을 입력하세요."
            value={formData.featuresString}
            onChange={handleChange}
          />
          <S.Error>{errors.featuresStringError}</S.Error>
        </S.InputWrapper>
        <S.ButtonWrapper>
          <S.SubmitButton type="submit">
            등록하기
          </S.SubmitButton>
        </S.ButtonWrapper>
      </form>
    </S.Wrapper>
  );
}
