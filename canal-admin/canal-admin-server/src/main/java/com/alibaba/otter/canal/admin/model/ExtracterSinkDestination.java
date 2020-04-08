package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * <p>
 * 输出目标表
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "extracter_sink_destination")
@Entity
public class ExtracterSinkDestination extends Model {

    private static final long serialVersionUID = 1L;

    public static final ExtracterSinkDestination.ExtracterSinkDestinationFinder find = new ExtracterSinkDestination.ExtracterSinkDestinationFinder();

    public static class ExtracterSinkDestinationFinder extends Finder<Integer, ExtracterSinkDestination> {

        /**
         * Construct using the default EbeanServer.
         */
        public ExtracterSinkDestinationFinder() {
            super(ExtracterSinkDestination.class);
        }
    }

    @Id
    private Integer id;

    /**
     * sink Id
     */
    private Integer sinkId;

    /**
     * 数据库名称
     */
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
