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
 * 输出类型表
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "extracter_sink")
@Entity
public class ExtracterSink extends Model {

    private static final long serialVersionUID = 1L;

    public static final ExtracterSink.ExtracterSinkFinder find = new ExtracterSink.ExtracterSinkFinder();

    public static class ExtracterSinkFinder extends Finder<Integer, ExtracterSink> {

        /**
         * Construct using the default EbeanServer.
         */
        public ExtracterSinkFinder() {
            super(ExtracterSink.class);
        }
    }

    @Id
    private Integer id;

    /**
     * sink名字
     */
    private String title;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 目标源;0/mysql;1/kafka;2/websocket;3/hbase
     */
    private String sinkOrigin;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
