package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.CanalConsumer;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.CanalConsumerService;
import io.ebean.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CanalConsumerServiceImpl implements CanalConsumerService {

    @Override
    public void insert(CanalConsumer canalConsumer) {
        //生成groupid
        canalConsumer.setGroupId(canalConsumer.getIp().hashCode() + UUID.randomUUID().toString().replaceAll("-", ""));
        canalConsumer.setCreationDate(new Date());
        canalConsumer.save();
    }

    @Override
    public void delete(Long id) {
        CanalConsumer.find.query().where().idEq(id).delete();
    }

    @Override
    public Pager<CanalConsumer> list(String search, Pager<CanalConsumer> pager) {
        Query<CanalConsumer> query = CanalConsumer.find.query();

        Query<CanalConsumer> queryCnt = query.copy();
        pager.setCount((long) queryCnt.findCount());

        if (StringUtils.isNotEmpty(search)) {
            query.where().like("name", "%" + search + "%");
        }

        List<CanalConsumer> extracterTask = query.order()
                .desc("creation_date")
                .setFirstRow(pager.getOffset().intValue())
                .setMaxRows(pager.getSize())
                .findList();
        pager.setItems(extracterTask);
        return pager;
    }

}
