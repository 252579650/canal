package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.ExtracterTask;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.ExtracterTaskService;
import io.ebean.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtracterTaskServiceImpl implements ExtracterTaskService {

    @Override
    public Pager<ExtracterTask> findList(String search, Pager<ExtracterTask> pager) {
        Query<ExtracterTask> query = ExtracterTask.find.query();

        Query<ExtracterTask> queryCnt = query.copy();
        pager.setCount((long) queryCnt.findCount());

        if (StringUtils.isNotEmpty(search)) {
            query.where().like("name", "%" + search + "%");
        }

        List<ExtracterTask> extracterTask = query.order()
                .desc("create_time")
                .setFirstRow(pager.getOffset().intValue())
                .setMaxRows(pager.getSize())
                .findList();
        pager.setItems(extracterTask);
        return pager;
    }
}
