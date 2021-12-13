package com.zxqax.nblog.controller;

import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/es")
public class ESController {
    /**
     * Spring IOC 自动注入 loginService,UserService 实例
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
    @PostMapping("/search")
    @CrossOrigin
    public Result<?> search(@RequestParam(value = "content") String content,
                            @RequestParam(value = "type") int type){
        return esService.search(content,type);
    }
}
