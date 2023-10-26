package com.example.demo.entity;


import java.util.Map;

public class UpdateDestinationRequest {
    private Map<String, String> updateFields; // 업데이트할 필드와 값을 매핑하는 맵

    public Map<String, String> getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(Map<String, String> updateFields) {
        this.updateFields = updateFields;
    }

    @Override
    public String toString() {
        return "UpdateDestinationRequest{" +
                "updateFields=" + updateFields +
                // 다른 필드를 필요에 따라 포함
                '}';
    }
}
