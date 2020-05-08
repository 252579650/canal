package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.ExtracterConsumerTopic;
import com.alibaba.otter.canal.admin.model.Pager;

public interface CanalConsumerTopicService {

    void insert(ExtracterConsumerTopic extracterConsumerTopic);

    Pager<ExtracterConsumerTopic> list(String search, Pager<ExtracterConsumerTopic> pager);

    void update(ExtracterConsumerTopic extracterConsumerTopic);

    void delete(Long id);
}
