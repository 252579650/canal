package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalConsumer;
import com.alibaba.otter.canal.admin.model.ExtracterTask;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.CanalConsumerService;
import com.alibaba.otter.canal.admin.service.ExtracterSinkMapperService;
import com.alibaba.otter.canal.admin.service.ExtracterTaskService;
import com.alibaba.otter.canal.admin.vo.ETLModelVO;
import com.alibaba.otter.canal.admin.vo.QuerySchemaVO;
import com.alibaba.otter.canal.admin.vo.TableRowInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/jeecg-boot/canal/api/")
@Api(tags = "数据抽取api")
public class SumscopeController {

    @Autowired
    private CanalConsumerService canalConsumerService;

    @Autowired
    private ExtracterTaskService extracterTaskService;

    @Autowired
    private ExtracterSinkMapperService extracterSinkMapperService;

    @GetMapping(value = "/querySourceDBNames")
    @ApiOperation(value = "查询来源数据库")
    public BaseModel<Set<String>> querySourceDBNames() {
        return BaseModel.getInstance(extracterSinkMapperService.querySourceDBNames());
    }

    @GetMapping(value = "/queryTableNames")
    @ApiOperation(value = "查询来源表集合")
    public BaseModel<List<String>> queryTableNames(String key) {
        return BaseModel.getInstance(extracterSinkMapperService.queryTableNames(key));
    }

    @GetMapping(value = "/queryTaskList")
    @ApiOperation(value = "查询任务列表")
    public BaseModel<Pager<ExtracterTask>> queryTaskList(String search, Pager<ExtracterTask> pager) {
        return BaseModel.getInstance(extracterTaskService.findList(search, pager));
    }

    @GetMapping(value = "/querySchema")
    @ApiOperation(value = "查询表行数据")
    public BaseModel<List<TableRowInfoVO>> querySchema(@RequestParam("ip") String ip, @RequestParam("port") String port, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tableName") String tableName, @RequestParam("dbName") String dbName) {
        QuerySchemaVO vo = new QuerySchemaVO();
        vo.setTableName(tableName);
        vo.setDbName(dbName);
        vo.setUserName(userName);
        vo.setPort(port);
        vo.setPassword(password);
        vo.setIp(ip);
        return BaseModel.getInstance(extracterSinkMapperService.querySchema(vo));
    }

    @GetMapping(value = "/queryTables")
    @ApiOperation(value = "查询表数据")
    public BaseModel<List<String>> queryTables(@RequestParam("ip") String ip, @RequestParam("port") String port, @RequestParam("password") String password, @RequestParam("userName") String userName, @RequestParam("tableName") String tableName, @RequestParam("dbName") String dbName) {
        QuerySchemaVO vo = new QuerySchemaVO();
        vo.setTableName(tableName);
        vo.setDbName(dbName);
        vo.setUserName(userName);
        vo.setPort(port);
        vo.setPassword(password);
        vo.setIp(ip);
        return BaseModel.getInstance(extracterSinkMapperService.queryTables(vo));
    }

    @PostMapping(value = "/initModel")
    @ApiOperation(value = "新增任务模型")
    public BaseModel<String> initModel(@RequestBody ETLModelVO model) {
        extracterSinkMapperService.init(model);
        return BaseModel.getInstance("success");
    }

    @PutMapping(value = "/updateModel")
    @ApiOperation(value = "更新任务模型")
    public BaseModel<String> updateModel(@RequestBody ETLModelVO model) {
        extracterSinkMapperService.delete(model);
        extracterSinkMapperService.init(model);
        return BaseModel.getInstance("success");
    }

    @DeleteMapping(value = "/deleteModel/{id}")
    @ApiOperation(value = "删除任务模型")
    public BaseModel<String> deleteModel(@PathVariable Integer id) {
        ETLModelVO model = new ETLModelVO();
        model.setId(id);
        extracterSinkMapperService.delete(model);
        return BaseModel.getInstance("success");
    }

    @GetMapping(value = "/getModel/{id}")
    @ApiOperation(value = "查询任务模型")
    public BaseModel<ETLModelVO> getModel(@PathVariable Long id) {
        return BaseModel.getInstance(extracterSinkMapperService.query(id));
    }

    @PostMapping(value = "/insertConsumer")
    @ApiOperation(value = "新增消费者")
    public BaseModel<String> insertConsumer(@RequestBody CanalConsumer canalConsumer) {
        canalConsumerService.insert(canalConsumer);
        return BaseModel.getInstance("success");
    }

    @DeleteMapping(value = "/deleteConsumer/{id}")
    @ApiOperation(value = "删除消费者")
    public BaseModel<String> deleteConsumer(@PathVariable Long id) {
        canalConsumerService.delete(id);
        return BaseModel.getInstance("success");
    }

    @GetMapping(value = "/queryConsumerList")
    @ApiOperation(value = "查询消费者列表")
    public BaseModel<Pager<CanalConsumer>> queryConsumerList(String search, Pager<CanalConsumer> pager) {
        return BaseModel.getInstance(canalConsumerService.list(search, pager));
    }
}
