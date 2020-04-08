package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.ExtracterTask;
import com.alibaba.otter.canal.admin.model.Pager;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
public interface ExtracterTaskService {
    Pager<ExtracterTask> findList(String search, Pager<ExtracterTask> pager);
}
