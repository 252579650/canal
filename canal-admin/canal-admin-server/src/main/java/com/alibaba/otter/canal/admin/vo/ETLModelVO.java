package com.alibaba.otter.canal.admin.vo;

import com.alibaba.otter.canal.admin.enums.SinkOriginEnum;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ETLModelVO implements Serializable {

    private Integer id;

    private String name;

    private String description;

    private String sourceDbName;

    private String sourceTableName;

    private String state;

    private SinkOriginEnum sinkOriginEnum;

    private List<SinkModel> sinkModels = Lists.newArrayList();

    @Data
    public static class SinkModel {

        private String title;

        private String ip;

        private String port;

        private SinkOriginEnum sinkOriginEnum;

        private List<DestinationModel> destinationModels = Lists.newArrayList();
    }

    @Data
    public static class DestinationModel {

        private String databaseName;


        private String filterCondition;
        /**
         * 数据库表名
         */
        private String tableName;

        private String userName;

        private String password;

        /**
         * 唯一性条件
         */
        private String uniqueCondition;

        /**
         * kafka topic名字
         */
        private String kafkaTopic;

        /**
         * ws url
         */
        private String wsUrl;

        private List<ModelMapper> ModelMappers = Lists.newArrayList();
    }

    @Data
    public static class ModelMapper {

        private String fieldName;

        /**
         * 字段值表达式
         */
        private String fieldVauleExp;

        /**
         * 类型;0/原值;1/表达式
         */
        private String type;

        /**
         * 是否推送;0/否;1/是
         */
        private String pushFlag;
    }

}
