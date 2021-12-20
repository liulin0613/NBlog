package com.nblog.controller;

import com.nblog.annotation.PassToken;
import com.nblog.dto.Result;
import com.nblog.service.ESService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/es")
@Api(tags = "搜索模块")
public class ESController {
    /**
     * Spring IOC 自动注入 esService 实例
     * 使用 Setter的方式注入，更加具有可变性
     */
    private ESService esService;

    @Autowired
    public void setService(ESService esService) {
        this.esService = esService;
    }

    /**
     * full search
     * @param content 搜索内容
     * @param type 搜索类型
     * @return 是否存在
     */
    @GetMapping("/search")
    @ApiOperation(value = "条件搜索")
    @PassToken
    public Result<List<Map<String,Object>>> search(@RequestParam(value = "content") String content,
                                                   @RequestParam(value = "type") int type){
        return esService.search(content,type);
    }
}
