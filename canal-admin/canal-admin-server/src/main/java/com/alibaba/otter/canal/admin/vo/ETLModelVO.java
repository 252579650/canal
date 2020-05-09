package com.alibaba.otter.canal.admin.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETLModelVO implements Serializable {

    private Integer id;

    @ApiModelProperty("topic名称")
    private String topic;

    /**
     * 类型;0/canal;1/爬虫
     */
    @ApiModelProperty("类型;0/canal;1/爬虫")
    private String type;

    /**
     * succ_is_remind
     */
    @ApiModelProperty("成功是否提醒;0/否;1/是")
    private String succIsRemind;

    @ApiModelProperty("运行方式;0/单线程;1/多线程")
    private String excuteType;

    /**
     * 提醒方式;0/默认;1/邮件;2/短信
     */
    @ApiModelProperty("提醒方式;0/默认;1/邮件;2/短信")
    private String remindType;
    /**
     * error_is_remind
     */
    @ApiModelProperty("错误是否提醒;0/否;1/是")
    private String errorIsRemind;

    @ApiModelProperty("模型名称 *")
    private String name;

    @ApiModelProperty("模型描述")
    private String description;

    @ApiModelProperty("来源数据库 *")
    private String sourceDbName;

    @ApiModelProperty("来源数据表 *")
    private String sourceTableName;

    @ApiModelProperty("状态;0/未启用;1/启用 *")
    private String state;

    @ApiModelProperty("目标源列表")
    private List<SinkModel> sinkModels = Lists.newArrayList();

    @Data
    public static class SinkModel {

        @ApiModelProperty("目标源标题 *")
        private String title;

        @ApiModelProperty("ip地址 *")
        private String ip;

        @ApiModelProperty("端口 *")
        private String port;

        @ApiModelProperty("目标源;0/mysql;1/kafka;2/websocket;3/hbase *")
        private String sinkOrigin;

        @ApiModelProperty("更新方式")
        private String updateType;

        @ApiModelProperty("优先级别")
        private String priority;

        @ApiModelProperty("目标列表")
        private List<DestinationModel> destinationModels = Lists.newArrayList();
    }

    @Data
    public static class DestinationModel {

        @ApiModelProperty("数据库名称")
        private String databaseName;

        @ApiModelProperty("数据范围")
        private String filterCondition;
        /**
         * 数据库表名
         */
        @ApiModelProperty("数据库表名")
        private String tableName;

        @ApiModelProperty("用户名")
        private String userName;

        @ApiModelProperty("密码")
        private String password;

        /**
         * 唯一性条件
         */
        @ApiModelProperty("唯一性条件")
        private String uniqueCondition;

        /**
         * kafka topic名字
         */
        @ApiModelProperty("kafka topic名字")
        private String kafkaTopic;

        /**
         * ws url
         */
        @ApiModelProperty("ws url")
        private String wsUrl;

        @ApiModelProperty("映射模型列表")
        private List<ModelMapper> ModelMappers = Lists.newArrayList();
    }

    @Data
    public static class ModelMapper {

        @ApiModelProperty("字段名  *")
        private String fieldName;

        /**
         * 字段值表达式
         */
        /**
         * 新增字段值表达式
         */
        @ApiModelProperty("新增")
        private String insertFieldVaule;

        /**
         * 类型;0/原值;1/表达式
         */
        @ApiModelProperty("类型;0/原值;1/表达式;2/默认值  *")
        private String insertFieldType;

        /**
         * 类型;0/原值;1/表达式
         */
        @ApiModelProperty("类型;0/原值;1/表达式;2/默认值  *")
        private String updateFieldType;

        /**
         * 新增字段值表达式
         */
        @ApiModelProperty("更新值")
        private String updateFieldVaule;

        /**
         * 校验规则
         */
        @ApiModelProperty("校验规则")
        private String checkRule;

        /**
         * 是否推送;0/否;1/是
         */
        @ApiModelProperty("是否推送;0/否;1/是  *")
        private String pushFlag;
    }

}
