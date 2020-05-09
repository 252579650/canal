package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class ExtracterConsumerTopic extends Model {

    private static final long serialVersionUID = 1L;

    public static final ExtracterConsumerTopic.ExtracterConsumerTopicFinder find = new ExtracterConsumerTopic.ExtracterConsumerTopicFinder();

    public static class ExtracterConsumerTopicFinder extends Finder<Integer, ExtracterConsumerTopic> {

        /**
         * Construct using the default EbeanServer.
         */
        public ExtracterConsumerTopicFinder() {
            super(ExtracterConsumerTopic.class);
        }
    }

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * topic名称
     */
    @ApiModelProperty("topic名称")
    private String topic;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

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

    /**
     * error_is_remind
     */
    @ApiModelProperty("错误是否提醒;0/否;1/是")
    private String errorIsRemind;

    /**
     * 运行方式;0/单线程;1/多线程
     */
    @ApiModelProperty("运行方式;0/单线程;1/多线程")
    private String excuteType;

    /**
     * 提醒方式;0/默认;1/邮件;2/短信
     */
    @ApiModelProperty("提醒方式;0/默认;1/邮件;2/短信")
    private String remindType;

    /**
     * creation_date
     */
    @ApiModelProperty("创建时间")
    private Date creationDate;
}
