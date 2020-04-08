package com.alibaba.otter.canal.admin.vo;

import com.alibaba.otter.canal.admin.enums.SinkOriginEnum;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETLModelVO implements Serializable {

    private Integer id;

    @ApiModelProperty("任务名")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("起始数据源")
    private String sourceDbName;

    @ApiModelProperty("起始表")
    private String sourceTableName;

    @ApiModelProperty("状态;0/未启用;1/启用")
    private String state;

    @ApiModelProperty("目标源列表")
    private List<SinkModel> sinkModels = Lists.newArrayList();

    @Data
    public static class SinkModel {

        @ApiModelProperty("目标源标题")
        private String title;

        @ApiModelProperty("ip地址")
        private String ip;

        @ApiModelProperty("端口")
        private String port;

        @ApiModelProperty("目标源;0/mysql;1/kafka;2/websocket;3/hbase")
        private String sinkOrigin;

        @ApiModelProperty("目标列表")
        private List<DestinationModel> destinationModels = Lists.newArrayList();
    }

    @Data
    public static class DestinationModel {

        @ApiModelProperty("数据库名称")
        private String databaseName;

        @ApiModelProperty("过滤条件")
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

        @ApiModelProperty("字段名称")
        private String fieldName;

        /**
         * 字段值表达式
         */
        @ApiModelProperty("字段值表达式")
        private String fieldVauleExp;

        /**
         * 类型;0/原值;1/表达式
         */
        @ApiModelProperty("类型;0/原值;1/表达式")
        private String type;

        /**
         * 是否推送;0/否;1/是
         */
        @ApiModelProperty("是否推送;0/否;1/是")
        private String pushFlag;
    }

}
