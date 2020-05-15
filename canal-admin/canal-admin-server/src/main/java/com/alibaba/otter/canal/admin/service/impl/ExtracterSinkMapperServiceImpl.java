package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.*;
import com.alibaba.otter.canal.admin.service.ExtracterSinkMapperService;
import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.alibaba.otter.canal.admin.vo.QuerySchemaVO;
import com.alibaba.otter.canal.admin.vo.TableRowInfoVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExtracterSinkMapperServiceImpl implements ExtracterSinkMapperService {

    private Map<String, List<String>> cache = Maps.newHashMap();

    public void clearCache() {
        cache.clear();
    }

    public void initCache() throws Exception {
        List<CanalInstanceConfig> canalInstanceConfigs = CanalInstanceConfig.find.all();
        if (CollectionUtils.isEmpty(canalInstanceConfigs)) {
            return;
        }

        for (CanalInstanceConfig config : canalInstanceConfigs) {
            Properties properties = new Properties();
            InputStream is = new ByteArrayInputStream(config.getContent().getBytes());

            HikariDataSource ds = new HikariDataSource();

            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(ds);
            try {
                properties.load(new InputStreamReader(is, "UTF-8"));

                Object o = properties.get("canal.instance.filter.regex");
                if (o != null || !o.toString().startsWith(".*")) {
                    List<String> tableNames = Lists.newArrayList();

                    String[] array = o.toString().split("\\\\");

                    String dbName = array[0];
                    ds.setUsername(properties.get("canal.instance.dbUsername").toString());
                    ds.setPassword(properties.get("canal.instance.dbPassword").toString());
                    ds.setJdbcUrl("jdbc:mysql://" + properties.get("canal.instance.master.address").toString() + "/" + dbName);
                    ds.setDriverClassName("com.mysql.jdbc.Driver");

                    String sql = "select table_name from information_schema.TABLES where table_schema='" + dbName + "'";
                    List rows = jdbcTemplate.queryForList(sql);
                    Iterator it = rows.iterator();
                    while (it.hasNext()) {
                        Map map = (Map) it.next();
                        tableNames.add(map.get("table_name").toString());
                    }

                    if (!CollectionUtils.isEmpty(tableNames)) {
                        cache.put(dbName, tableNames);
                    }
                }
            } finally {
                is.close();
                ds.close();
            }
        }
    }

    public Set<String> querySourceDBNames() throws Exception {
        if (CollectionUtils.isEmpty(cache)) {
            initCache();
        }

        return cache.keySet();
    }

    public List<String> queryTableNames(String key) throws Exception {
        if (CollectionUtils.isEmpty(cache)) {
            initCache();
        }

        return cache.get(key);
    }

    @Override
    public List<ExtracterSinkMapper> queryList(QuerySchemaVO vo) {
        ExtracterSink extracterSink = ExtracterSink.find.query().where().eq("ip", vo.getIp()).eq("port", vo.getPort()).eq("sinkOrigin", "0").findOne();
        if (extracterSink == null) {
            return Lists.newArrayList();
        }

        ExtracterSinkDestination extracterSinkDestination = ExtracterSinkDestination.find.query().where().eq("sinkId", extracterSink.getId()).eq("databaseName", vo.getDbName()).eq("tableName", vo.getTableName()).eq("userName", vo.getUserName()).eq("password", vo.getPassword()).findOne();
        if (extracterSinkDestination == null) {
            return Lists.newArrayList();
        }

        return ExtracterSinkMapper.find.query().where().eq("destinationId", extracterSinkDestination.getId()).findList();
    }

    @Override
    public List<TableRowInfoVO> querySchema(QuerySchemaVO vo) {
        List<ExtracterSinkMapper> extracterSinkMappers = this.queryList(vo);
        Map<String, ExtracterSinkMapper> valueMap = CollectionUtils.isEmpty(extracterSinkMappers) ? Maps.newHashMap() : extracterSinkMappers.stream().collect(Collectors.toMap(ExtracterSinkMapper::getFieldName, g -> g));

        List<TableRowInfoVO> vos = Lists.newArrayList();

        HikariDataSource ds1 = new HikariDataSource();
        HikariDataSource ds = new HikariDataSource();
        try {
            if (vo.getDbName().equalsIgnoreCase("GJK_EFORM")) {
                ds.setUsername("root");
                ds.setPassword("gjkroot");
                ds.setJdbcUrl("jdbc:mysql://192.168.1.222:3306/gjk_sdmp");
                ds.setDriverClassName("com.mysql.jdbc.Driver");
                String sql = "select t1.field_name,t1.field_type,t1.field_code,t2.form_table from sdmp_eform_field t1 inner join sdmp_eform t2 on t2.form_code=t1.fid where t2.form_table='" + vo.getTableName() + "'";

                JdbcTemplate jdbcTemplate = new JdbcTemplate();
                jdbcTemplate.setDataSource(ds);
                List rows = jdbcTemplate.queryForList(sql);
                Iterator it = rows.iterator();

                if (it.hasNext()) {
                    while (it.hasNext()) {
                        Map map = (Map) it.next();
                        ExtracterSinkMapper extracterSinkMapper = valueMap.get(map.get("field_code").toString());

                        TableRowInfoVO tableRowInfoVO = new TableRowInfoVO();
                        tableRowInfoVO.setCheckRule(extracterSinkMapper.getCheckRule());
                        tableRowInfoVO.setInsertFieldType(extracterSinkMapper.getInsertFieldType() != null ? extracterSinkMapper.getInsertFieldType() : "3");
                        tableRowInfoVO.setInsertFieldVaule(extracterSinkMapper.getInsertFieldVaule());
                        tableRowInfoVO.setUpdateFieldType(extracterSinkMapper.getUpdateFieldType() != null ? extracterSinkMapper.getUpdateFieldType() : "3");
                        tableRowInfoVO.setUpdateFieldVaule(extracterSinkMapper.getUpdateFieldVaule());
                        tableRowInfoVO.setFieldComment(map.get("field_name") != null ? map.get("field_name").toString() : null);
                        tableRowInfoVO.setFieldName(map.get("field_code").toString());
                        tableRowInfoVO.setPushFlag(extracterSinkMapper.getPushFlag() != null ? extracterSinkMapper.getPushFlag() : "1");
                        tableRowInfoVO.setTableName(vo.getTableName());
                        vos.add(tableRowInfoVO);
                    }

                    return vos;
                }
            }

            ds1.setUsername(vo.getUserName());
            ds1.setPassword(vo.getPassword());
            ds1.setJdbcUrl("jdbc:mysql://" + vo.getIp() + ":" + vo.getPort() + "/" + vo.getDbName());
            ds1.setDriverClassName("com.mysql.jdbc.Driver");
            JdbcTemplate jdbcTemplate1 = new JdbcTemplate();
            jdbcTemplate1.setDataSource(ds1);

            String sql1 = "select COLUMN_NAME, COLUMN_COMMENT,DATA_TYPE from information_schema.COLUMNS where table_schema='" + vo.getDbName() + "' and table_name='" + vo.getTableName() + "'";
            List rows1 = jdbcTemplate1.queryForList(sql1);

            Iterator it1 = rows1.iterator();

            while (it1.hasNext()) {
                Map map = (Map) it1.next();
                ExtracterSinkMapper extracterSinkMapper = valueMap.get(map.get("COLUMN_NAME").toString());
                extracterSinkMapper = extracterSinkMapper != null ? extracterSinkMapper : new ExtracterSinkMapper();

                TableRowInfoVO tableRowInfoVO = new TableRowInfoVO();
                tableRowInfoVO.setCheckRule(extracterSinkMapper.getCheckRule());
                tableRowInfoVO.setInsertFieldType(extracterSinkMapper.getInsertFieldType() != null ? extracterSinkMapper.getInsertFieldType() : "3");
                tableRowInfoVO.setInsertFieldVaule(extracterSinkMapper.getInsertFieldVaule());
                tableRowInfoVO.setUpdateFieldType(extracterSinkMapper.getUpdateFieldType() != null ? extracterSinkMapper.getUpdateFieldType() : "3");
                tableRowInfoVO.setUpdateFieldVaule(extracterSinkMapper.getUpdateFieldVaule());
                tableRowInfoVO.setFieldComment(map.get("COLUMN_COMMENT") != null ? map.get("COLUMN_COMMENT").toString() : null);
                tableRowInfoVO.setFieldName(map.get("COLUMN_NAME").toString());
                tableRowInfoVO.setPushFlag(extracterSinkMapper.getPushFlag() != null ? extracterSinkMapper.getPushFlag() : "0");
                tableRowInfoVO.setTableName(vo.getTableName());
                vos.add(tableRowInfoVO);
            }

            return vos;
        } finally {
            ds.close();
            ds1.close();
        }
    }


    @Override
    public List<String> queryTables(QuerySchemaVO vo) {
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(vo.getUserName());
        ds.setPassword(vo.getPassword());
        ds.setJdbcUrl("jdbc:mysql://" + vo.getIp() + ":" + vo.getPort() + "/" + vo.getDbName());
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(ds);

            List<String> result = Lists.newArrayList();

            String sql = "select table_name from information_schema.TABLES where table_schema='" + vo.getDbName() + "' and table_name like '%" + vo.getTableName() + "%'";
            List rows = jdbcTemplate.queryForList(sql);
            Iterator it = rows.iterator();
            while (it.hasNext()) {
                Map map = (Map) it.next();
                result.add(map.get("table_name").toString());
            }

            return result;
        } finally {
            ds.close();
        }
    }


    @Override
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
            ExtracterSink.find.query().where().idIn(extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toList())).delete();
            //删除映射信息
            List<ExtracterSinkDestination> destinations = ExtracterSinkDestination.find.query().where().in("sink_id", extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toSet())).findList();
            if (!CollectionUtils.isEmpty(destinations)) {
                ExtracterSinkDestination.find.query().where().in("sink_id", extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toSet())).delete();
                ExtracterSinkMapper.find.query().where().in("destination_id", destinations.stream().map(ExtracterSinkDestination::getId).collect(Collectors.toSet())).delete();
            }
        }
    }

    @Override
    public ETLModelVO query(Long id) {
        ExtracterTask extracterTask = ExtracterTask.find.query().where().idEq(id).findOne();
        if (extracterTask == null) {
            return null;
        }

        ETLModelVO vo = new ETLModelVO();
        vo.setId(extracterTask.getId());
        vo.setDescription(extracterTask.getDescription());
        vo.setName(extracterTask.getName());
        vo.setSourceDbName(extracterTask.getSourceDatabase());
        vo.setSourceTableName(extracterTask.getSourceTable());
        vo.setState(extracterTask.getState());
        vo.setErrorIsRemind(extracterTask.getErrorIsRemind());
        vo.setTopic(extracterTask.getTopic());
        vo.setExcuteType(extracterTask.getExcuteType());
        vo.setRemindType(extracterTask.getRemindType());
        vo.setSuccIsRemind(extracterTask.getSuccIsRemind());
        vo.setType(extracterTask.getType());

        List<ExtracterTaskSink> extracterTaskSinks = ExtracterTaskSink.find.query().where().eq("task_id", id).findList();
        if (!CollectionUtils.isEmpty(extracterTaskSinks)) {
            List<ExtracterSink> extracterSinks = ExtracterSink.find.query().where().idIn(extracterTaskSinks.stream().map(ExtracterTaskSink::getSinkId).collect(Collectors.toList())).findList();
            if (!CollectionUtils.isEmpty(extracterSinks)) {
                List<ETLModelVO.SinkModel> sinkModels = Lists.newArrayList();

                for (ExtracterSink extracterSink : extracterSinks) {
                    ETLModelVO.SinkModel sinkModel = new ETLModelVO.SinkModel();
                    BeanUtils.copyProperties(extracterSink, sinkModel);

                    List<ExtracterSinkDestination> extracterSinkDestinations = ExtracterSinkDestination.find.query().where().eq("sink_id", extracterSink.getId()).findList();
                    if (!CollectionUtils.isEmpty(extracterSinkDestinations)) {
                        List<ETLModelVO.DestinationModel> destinationModels = Lists.newArrayList();

                        for (ExtracterSinkDestination extracterSinkDestination : extracterSinkDestinations) {
                            ETLModelVO.DestinationModel destinationModel = new ETLModelVO.DestinationModel();
                            BeanUtils.copyProperties(extracterSinkDestination, destinationModel);

                            List<ExtracterSinkMapper> extracterSinkMappers = ExtracterSinkMapper.find.query().where().eq("destination_id", extracterSinkDestination.getId()).findList();
                            if (!CollectionUtils.isEmpty(extracterSinkMappers)) {
                                List<ETLModelVO.ModelMapper> modelMappers = Lists.newArrayList();

                                for (ExtracterSinkMapper extracterSinkMapper : extracterSinkMappers) {
                                    ETLModelVO.ModelMapper modelMapper = new ETLModelVO.ModelMapper();
                                    BeanUtils.copyProperties(extracterSinkMapper, modelMapper);
                                    modelMappers.add(modelMapper);
                                }

                                destinationModel.setModelMappers(modelMappers);
                            }

                            destinationModels.add(destinationModel);
                        }

                        sinkModel.setDestinationModels(destinationModels);
                    }

                    sinkModels.add(sinkModel);
                }

                vo.setSinkModels(sinkModels);

            }
        }

        return vo;
    }

    @Override
    public void init(ETLModelVO model) {
        try {
            if ("0".equalsIgnoreCase(model.getType())) {
                if (ExtracterTask.find.query().where().eq("source_database", model.getSourceDbName()).and().eq("source_table", model.getSourceTableName()).findOne() != null) {
                    throw new ServiceException(model.getSourceDbName() + ":" + model.getSourceTableName() + "已存在相关配置");
                }
            }
            //保存计划信息
            ExtracterTask extracterTask = new ExtracterTask();
            extracterTask.setId(model.getId());
            extracterTask.setName(model.getName());
            extracterTask.setExcuteResult("0");
            extracterTask.setExcuteTime(LocalDateTime.now());
            extracterTask.setSourceDatabase(model.getSourceDbName());
            extracterTask.setSourceTable(model.getSourceTableName());
            extracterTask.setState(model.getState());
            extracterTask.setDescription(model.getDescription());
            extracterTask.setCreateTime(LocalDateTime.now());
            extracterTask.setUpdateTime(LocalDateTime.now());
            extracterTask.setErrorIsRemind(model.getErrorIsRemind());
            extracterTask.setTopic(model.getTopic());
            extracterTask.setExcuteType("1");
            extracterTask.setRemindType("1");
            extracterTask.setSuccIsRemind(model.getSuccIsRemind());
            extracterTask.setType(model.getType());
            extracterTask.setGroupId(UUID.randomUUID().toString().replaceAll("-", ""));
            extracterTask.save();

            List<ETLModelVO.SinkModel> sinkModels = model.getSinkModels();
            if (!CollectionUtils.isEmpty(sinkModels)) {
                for (ETLModelVO.SinkModel sink : sinkModels) {
                    //保存sink信息z
                    ExtracterSink extracterSink = new ExtracterSink();
                    extracterSink.setIp(sink.getIp());
                    extracterSink.setPort(sink.getPort());
                    extracterSink.setSinkOrigin(sink.getSinkOrigin());
                    extracterSink.setTitle(sink.getTitle());
                    extracterSink.setCreateTime(LocalDateTime.now());
                    extracterSink.setUpdateTime(LocalDateTime.now());
                    extracterSink.save();
                    //保存关联信息
                    ExtracterTaskSink extracterTaskSink = new ExtracterTaskSink();
                    extracterTaskSink.setSinkId(extracterSink.getId());
                    extracterTaskSink.setTaskId(extracterTask.getId());
                    extracterTaskSink.setCreateTime(LocalDateTime.now());
                    extracterTaskSink.setUpdateTime(LocalDateTime.now());
                    extracterTaskSink.save();

                    List<ETLModelVO.DestinationModel> destinationModels = sink.getDestinationModels();
                    if (!CollectionUtils.isEmpty(destinationModels)) {
                        for (ETLModelVO.DestinationModel d : destinationModels) {
                            //保存Destination信息
                            ExtracterSinkDestination extracterSinkDestination = new ExtracterSinkDestination();
                            BeanUtils.copyProperties(d, extracterSinkDestination);
                            extracterSinkDestination.setSinkId(extracterSink.getId());
                            extracterSinkDestination.setCreateTime(LocalDateTime.now());
                            extracterSinkDestination.setUpdateTime(LocalDateTime.now());
                            extracterSinkDestination.save();

                            List<ETLModelVO.ModelMapper> modelMappers = d.getModelMappers();
                            if (!CollectionUtils.isEmpty(modelMappers)) {
                                List<ExtracterSinkMapper> list = Lists.newArrayList();

                                modelMappers.stream().forEach(mm -> {
                                    ExtracterSinkMapper extracterSinkMapper = new ExtracterSinkMapper();
                                    BeanUtils.copyProperties(mm, extracterSinkMapper);
                                    if (extracterSinkMapper.getInsertFieldType().equalsIgnoreCase("3")) {
                                        extracterSinkMapper.setInsertFieldVaule(null);
                                    }

                                    if (extracterSinkMapper.getUpdateFieldType().equalsIgnoreCase("3")) {
                                        extracterSinkMapper.setUpdateFieldVaule(null);
                                    }

                                    extracterSinkMapper.setPushFlag("1");
                                    extracterSinkMapper.setDestinationId(extracterSinkDestination.getId());
                                    extracterSinkMapper.setCreateTime(LocalDateTime.now());
                                    extracterSinkMapper.setUpdateTime(LocalDateTime.now());
                                    list.add(extracterSinkMapper);
                                });

                                Ebean.saveAll(list);
                            }
                        }
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


    private String typeMach(String type) {
        type = type.toUpperCase();
        if (type.startsWith("BIGINT")) {
            return "Long";
        } else if (type.startsWith("INT") || type.startsWith("SMALLINT") || type.startsWith("TINYINT")) {
            return "Integer";
        } else if (type.startsWith("FLOAT")) {
            return "FLOAT";
        } else if (type.startsWith("DOUBLE")) {
            return "DOUBLE";
        } else if (type.startsWith("DECIMAL")) {
            return "BigDECIMAL";
        } else if (type.startsWith("DATETIME")) {
            return "Date";
        } else {
            return "String";
        }
    }
}
