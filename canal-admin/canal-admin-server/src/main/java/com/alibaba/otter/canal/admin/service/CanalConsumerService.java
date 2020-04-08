package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalConsumer;
import com.alibaba.otter.canal.admin.model.Pager;

public interface CanalConsumerService {

    void insert(CanalConsumer canalConsumer);

    Pager<CanalConsumer> list(String search, Pager<CanalConsumer> pager);

    void delete(Long id);
}
