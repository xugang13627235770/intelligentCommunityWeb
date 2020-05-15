package cn.citms.icw.controller;

import cn.citms.icw.service.IPersonPassService;
import cn.citms.icw.vo.EsPersonVO;
import cn.citms.icw.vo.PersonSearchVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *      通行行人 控制器
 * </pre>
 * @author liuyuyang
 */
@RestController
@RequestMapping("/PersonPass")
@Api(value = "通行行人", tags = "通行行人接口")
public class PersonPassController {
    private final IPersonPassService personPassService;
    @Autowired
    public PersonPassController(IPersonPassService personPassService){
        this.personPassService=personPassService;
    }

    @PostMapping("/Query")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询通行行人接口", notes = "传入PersonSearchVO")
    public R<IPage<EsPersonVO>> query (@RequestBody PersonSearchVO vo){
        return R.data(personPassService.queryPerson(vo));
    }
}
