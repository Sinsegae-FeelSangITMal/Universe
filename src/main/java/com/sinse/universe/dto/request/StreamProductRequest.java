package com.sinse.universe.dto.request;

import lombok.Data;

@Data
public class StreamProductRequest {

    private Integer id;       // SR_PD_ID (수정 시 필요, 등록 시는 null)
    private int streamId;     // 연결할 Stream의 ID
    private int productId;    // 연결할 Product의 ID
}
