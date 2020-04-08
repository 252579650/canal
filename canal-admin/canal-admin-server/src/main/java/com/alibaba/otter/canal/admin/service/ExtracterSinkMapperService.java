package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.alibaba.otter.canal.admin.vo.QuerySchemaVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务目标模型映射
 * 表 服务类
 * </p>
 *
 * @author xugm
 * @since 2020-04-03
 */
public interface ExtracterSinkMapperService {

    List<Map<String, String>> querySchema(QuerySchemaVO vo);

    void delete(ETLModelVO model);

    void init(ETLModelVO model);
}
