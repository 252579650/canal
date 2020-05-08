package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.ExtracterConsumerTopic;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.CanalConsumerTopicService;
import io.ebean.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanalConsumerTopicServiceImpl implements CanalConsumerTopicService {

    @Override
    public void insert(ExtracterConsumerTopic extracterConsumerTopic) {
        extracterConsumerTopic.save();
    }

    @Override
    public void update(ExtracterConsumerTopic extracterConsumerTopic) {
        extracterConsumerTopic.update();
    }

    @Override
    public void delete(Long id) {
        ExtracterConsumerTopic.find.query().where().idEq(id).delete();
    }

    @Override
    public Pager<ExtracterConsumerTopic> list(String search, Pager<ExtracterConsumerTopic> pager) {
        Query<ExtracterConsumerTopic> query = ExtracterConsumerTopic.find.query();

        Query<ExtracterConsumerTopic> queryCnt = query.copy();
        pager.setCount((long) queryCnt.findCount());

        if (StringUtils.isNotEmpty(search)) {
            query.where().like("name", "%" + search + "%");
        }

        List<ExtracterConsumerTopic> extracterTask = query.order()
                .desc("creation_date")
                .setFirstRow(pager.getOffset().intValue())
                .setMaxRows(pager.getSize())
                .findList();
        pager.setItems(extracterTask);
        return pager;
    }

}
