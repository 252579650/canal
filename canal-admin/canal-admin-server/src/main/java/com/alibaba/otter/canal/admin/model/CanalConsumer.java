package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "canal_consumer")
@Entity
public class CanalConsumer  extends Model {


    public static final CanalConsumer.CanalConsumerFinder find = new CanalConsumer.CanalConsumerFinder();

    public static class CanalConsumerFinder extends Finder<Integer, CanalConsumer> {

        /**
         * Construct using the default EbeanServer.
         */
        public CanalConsumerFinder() {
            super(CanalConsumer.class);
        }
    }

    @Id
    private Long id;

    /**
     * ip
     */
    private String ip;

    /**
     * group_id
     */
    private String groupId;

    /**
     * name
     */
    private String name;

    /**
     * creation_date
     */
    private Date creationDate;
}
