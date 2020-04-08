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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/canal/api/")
@Api(tags = "数据抽取api")
public class SumscopeController {

    @Autowired
    private CanalConsumerService canalConsumerService;

    @Autowired
    private ExtracterTaskService extracterTaskService;

    @Autowired
    private ExtracterSinkMapperService extracterSinkMapperService;

    @GetMapping(value = "/queryTaskList")
    @ApiOperation(value = "查询任务列表")
    public BaseModel<Pager<ExtracterTask>> queryTaskList(String search, Pager<ExtracterTask> pager) {
        return BaseModel.getInstance(extracterTaskService.findList(search, pager));
    }

    @GetMapping(value = "/querySchema")
    @ApiOperation(value = "查询表Schema")
    public BaseModel<List<Map<String, String>>> querySchema(QuerySchemaVO vo) {
        return BaseModel.getInstance(extracterSinkMapperService.querySchema(vo));
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
