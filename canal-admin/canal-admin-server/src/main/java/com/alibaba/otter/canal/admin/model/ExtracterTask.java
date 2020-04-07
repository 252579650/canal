package com.alibaba.otter.canal.admin.model;

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
public class ExtracterTask extends Model{

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /**
     * 任务名
     */
    private String name;

    /**
     * 起始数据源
     */
    private String sourceDatabase;

    /**
     * 起始表
     */
    private String sourceTable;

    /**
     * 执行结果;0/未开始;1/成功;2/失败;
     */
    private String excuteResult;

    /**
     * 最后一次执行时间
     */
    private LocalDateTime excuteTime;

    /**
     * 状态;0/未启用;1/启用
     */
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
