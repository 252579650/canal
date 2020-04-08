package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "extracter_task")
@Entity
public class ExtracterTask extends Model {

    private static final long serialVersionUID = 1L;

    public static final ExtracterTask.ExtracterTaskFinder find = new ExtracterTask.ExtracterTaskFinder();

    public static class ExtracterTaskFinder extends Finder<Long, ExtracterTask> {

        /**
         * Construct using the default EbeanServer.
         */
        public ExtracterTaskFinder() {
            super(ExtracterTask.class);
        }

    }

    @Id
    private Integer id;

    /**
     * 任务名
     */
    @ApiModelProperty("任务名")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    /**
     * 起始数据源
     */
    @ApiModelProperty("起始数据源")
    private String sourceDatabase;

    /**
     * 起始表
     */
    @ApiModelProperty("起始表")
    private String sourceTable;



    /**
     * 执行结果;0/未开始;1/成功;2/失败;
     */
    @ApiModelProperty("执行结果;0/未开始;1/成功;2/失败")
    private String excuteResult;

    /**
     * 最后一次执行时间
     */
    @ApiModelProperty("最后一次执行时间")
    private LocalDateTime excuteTime;

    /**
     * 状态;0/未启用;1/启用
     */
    @ApiModelProperty("状态;0/未启用;1/启用")
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
