package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.*;
import com.alibaba.otter.canal.admin.service.ExtracterSinkMapperService;
import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.google.common.collect.Lists;
import io.ebean.Ebean;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExtracterSinkMapperServiceImpl implements ExtracterSinkMapperService {

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

        }

    }
}
