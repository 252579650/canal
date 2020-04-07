package com.alibaba.otter.canal.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SinkOriginEnum {

    MYSQL("0", "mysql"), KAFKA("1", "kafka"), WEB_SOCKET("2", "websocket");

    private String code;

    private String message;
}
