package cn.citms.icw.controller;

import cn.citms.icw.entity.EsVehicle;
import cn.citms.icw.service.IPersonPassService;
import cn.citms.icw.service.IVehiclePassService;
import cn.citms.icw.vo.EsPersonVO;
import cn.citms.icw.vo.EsVehicleVO;
import cn.citms.icw.vo.PersonSearchVO;
import cn.citms.icw.vo.VehicleSearchVO;
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
@RequestMapping("/VehiclePass")
@Api(value = "通行行人", tags = "通行行人接口")
public class VehiclePassController {
    private final IVehiclePassService vehiclePassService;
    @Autowired
    public VehiclePassController(IVehiclePassService vehiclePassService){
        this.vehiclePassService=vehiclePassService;
    }

    @PostMapping("/Query")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询车辆接口", notes = "传入VehicleSearchVO")
    public R<IPage<EsVehicleVO>> query (@RequestBody VehicleSearchVO vo){
        IPage<EsVehicleVO> ie=vehiclePassService.queryVehicle(vo);
        return  R.data(ie);
    }

}
