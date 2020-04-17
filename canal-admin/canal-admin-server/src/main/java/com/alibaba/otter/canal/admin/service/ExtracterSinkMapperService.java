package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.ExtracterSinkMapper;
import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.alibaba.otter.canal.admin.vo.QuerySchemaVO;
import com.alibaba.otter.canal.admin.vo.TableRowInfoVO;

import java.util.List;
import java.util.Set;

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

    List<String> queryTableNames(String key);

    Set<String> querySourceDBNames();

    List<ExtracterSinkMapper> queryList(QuerySchemaVO vo);

    List<String> queryTables(QuerySchemaVO vo);

    List<TableRowInfoVO> querySchema(QuerySchemaVO vo);

    void delete(ETLModelVO model);

    void init(ETLModelVO model);

    ETLModelVO query(Long id);
}
