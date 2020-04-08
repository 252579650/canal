package com.alibaba.otter.canal.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySchemaVO {
    @ApiModelProperty("ip地址")
    private String ip;
    @ApiModelProperty("端口")
    private String port;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("数据库名")
    private String dbName;
}
