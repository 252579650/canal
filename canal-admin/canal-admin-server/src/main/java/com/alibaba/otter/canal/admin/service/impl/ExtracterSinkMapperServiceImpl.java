package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.*;
import com.alibaba.otter.canal.admin.service.ExtracterSinkMapperService;
import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.alibaba.otter.canal.admin.vo.QuerySchemaVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExtracterSinkMapperServiceImpl implements ExtracterSinkMapperService {

    public List<Map<String, String>> querySchema(QuerySchemaVO vo) {
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(vo.getUserName());
        ds.setPassword(vo.getPassword());
        ds.setJdbcUrl("jdbc:mysql://" + vo.getIp() + ":" + vo.getPort() + "/" + vo.getDbName());
        ds.setDriverClassName("com.mysql.jdbc.Driver");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);

        List<Map<String, String>> result = Lists.newArrayList();

        String sql = "select COLUMN_NAME, COLUMN_COMMENT from information_schema.COLUMNS where table_schema='" + vo.getDbName() + "' and table_name='" + vo.getTableName() + "'";
        List rows = jdbcTemplate.queryForList(sql);
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            Map map = (Map) it.next();

            Map<String, String> r = Maps.newHashMap();
            r.put(map.get("COLUMN_NAME").toString(), map.get("COLUMN_COMMENT").toString());
            result.add(r);
        }

        return result;
    }

    public void delete(ETLModelVO model) {
        ExtracterTask extracterTask = ExtracterTask.find.query().where().idEq(model.getId()).findOne();
        if (extracterTask == null) {
            throw new ServiceException(model.getId() + "未查询到相关配置");
        }
        //删除主任务
        extracterTask.delete();

        List<ExtracterTaskSink> extracterTaskSinks = ExtracterTaskSink.find.query().where().eq("task_id", model.getId()).findList();
        if (!CollectionUtils.isEmpty(extracterTaskSinks)) {
            //删除关联信息
            ExtracterTaskSink.find.query().where().eq("task_id", model.getId()).delete();
            //删除映射信息
            List<ExtracterSinkDestination> destinations = ExtracterSinkDestination.find.query().where().in("sink_id", extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toSet())).findList();
            if (!CollectionUtils.isEmpty(destinations)) {
                ExtracterSinkDestination.find.query().where().in("sink_id", extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toSet())).delete();
                ExtracterSinkMapper.find.query().where().in("destination_id", destinations.stream().map(ExtracterSinkDestination::getId).collect(Collectors.toSet())).delete();
            }
        }
    }

    public void init(ETLModelVO model) {
        try {
            ExtracterTask extracterTask = ExtracterTask.find.query().where().eq("source_database", model.getSourceDbName()).and().eq("source_table", model.getSourceTableName()).findOne();
            if (extracterTask != null) {
                throw new ServiceException(model.getSourceDbName() + ":" + model.getSourceTableName() + "已存在相关配置");
            }
            //保存计划信息
            extracterTask = new ExtracterTask();
            extracterTask.setName(model.getName());
            extracterTask.setExcuteResult("0");
            extracterTask.setExcuteTime(LocalDateTime.now());
            extracterTask.setSourceDatabase(model.getSourceDbName());
            extracterTask.setSourceTable(model.getSourceTableName());
            extracterTask.setState(model.getState());
            extracterTask.setDescription(model.getDescription());
            extracterTask.save();

            List<ETLModelVO.SinkModel> sinkModels = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(sinkModels)) {
                for (ETLModelVO.SinkModel sink : sinkModels) {
                    //保存sink信息
                    ExtracterSink extracterSink = new ExtracterSink();
                    extracterSink.setIp(sink.getIp());
                    extracterSink.setPort(sink.getPort());
                    extracterSink.setSinkOrigin(sink.getSinkOriginEnum().getCode());
                    extracterSink.setTitle(sink.getTitle());
                    extracterSink.save();
                    //保存关联信息
                    ExtracterTaskSink extracterTaskSink = new ExtracterTaskSink();
                    extracterTaskSink.setSinkId(extracterSink.getId());
                    extracterTaskSink.setTaskId(extracterTask.getId());
                    extracterTaskSink.save();

                    List<ETLModelVO.DestinationModel> destinationModels = sink.getDestinationModels();
                    if (!CollectionUtils.isEmpty(destinationModels)) {
                        destinationModels.stream().forEach(d -> {
                            //保存Destination信息
                            ExtracterSinkDestination extracterSinkDestination = new ExtracterSinkDestination();
                            BeanUtils.copyProperties(d, extracterSinkDestination);
                            extracterSinkDestination.setSinkId(extracterSink.getId());
                            extracterSinkDestination.save();

                            List<ETLModelVO.ModelMapper> modelMappers = d.getModelMappers();
                            if (!CollectionUtils.isEmpty(modelMappers)) {
                                List<ExtracterSinkMapper> list = Lists.newArrayList();

                                modelMappers.stream().forEach(mm -> {
                                    ExtracterSinkMapper extracterSinkMapper = new ExtracterSinkMapper();
                                    BeanUtils.copyProperties(mm, extracterSinkMapper);
                                    extracterSinkMapper.setDestinationId(extracterSinkDestination.getId());
                                    list.add(extracterSinkMapper);
                                });

                                Ebean.saveAll(list);
                            }
                        });
                    }
                }
            }
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("入库失败", e);
            throw e;
        }

    }
}
