package com.alibaba.otter.canal.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableRowInfoVO implements Serializable {

    private Integer id;

    private String filedName;

    private String filedComment;

    private String type;

    private String fieldVauleExp;

    private String pushFlag;

    private String defaultValue;

    private String tableName;
}
