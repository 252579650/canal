package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 任务目标模型映射
 * 表
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "extracter_sink_mapper")
@Entity
public class ExtracterSinkMapper extends Model {

    private static final long serialVersionUID = 1L;

    public static final ExtracterSinkMapper.ExtracterSinkMapperFinder find = new ExtracterSinkMapper.ExtracterSinkMapperFinder();

    public static class ExtracterSinkMapperFinder extends Finder<Integer, ExtracterSinkMapper> {

        /**
         * Construct using the default EbeanServer.
         */
        public ExtracterSinkMapperFinder() {
            super(ExtracterSinkMapper.class);
        }
    }

    @Id
    private Integer id;

    /**
     * 目标ID
     */
    private Integer destinationId;

    /**
     * 字段名
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
