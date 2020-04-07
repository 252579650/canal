package com.alibaba.otter.canal.admin.vo;

import lombok.Data;

@Data
public class QuerySchemaVO {
    private String ip;
    private String port;
    private String password;
    private String userName;
    private String tableName;
    private String dbName;
}
