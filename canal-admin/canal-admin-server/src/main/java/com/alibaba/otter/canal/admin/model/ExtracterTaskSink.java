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
 * 任务输出关联表
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
public class ExtracterTaskSink extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    /**
     * 任务调度id
     */
    private Integer taskId;

    /**
     * 输出资源表Id
     */
    private Integer sinkId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
