package com.alibaba.otter.canal.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableRowInfoVO implements Serializable {

    private String fieldName;

    private String fieldComment;

    private String pushFlag;

    private String tableName;

    /**
     * 新增字段值表达式
     */
    private String insertFieldVaule;

    /**
     * 类型;0/原值;1/表达式
     */
    private String insertFieldType;

    /**
     * 类型;0/原值;1/表达式
     */
    private String updateFieldType;

    /**
     * 新增字段值表达式
     */
    private String updateFieldVaule;

    /**
     * 校验规则
     */
    private String checkRule;
}
