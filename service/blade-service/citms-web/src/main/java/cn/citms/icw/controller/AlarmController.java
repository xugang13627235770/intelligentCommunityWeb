package cn.citms.icw.controller;

import cn.citms.icw.dao.PersonTrafficFlowDao;
import cn.citms.icw.service.IAlarmService;
import cn.citms.icw.vo.EsAlarmSearchVO;
import cn.citms.icw.vo.EsAlarmVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 *      报警数据 控制器
 * </pre>
 * @author liuyuyang
 */
@RestController
@RequestMapping("/Alarm")
@Api(value = "预警信息", tags = "预警信息接口")
public class AlarmController {

    private final IAlarmService alarmService;
    private final PersonTrafficFlowDao personTrafficFlowDao;
    @Autowired
    public AlarmController(IAlarmService alarmService,PersonTrafficFlowDao personTrafficFlowDao){
        this.alarmService=alarmService;
        this.personTrafficFlowDao=personTrafficFlowDao;
    }

    @PostMapping("/Query")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询接口", notes = "传入EsAlarmSearchVO")
    public R<IPage<EsAlarmVO>> query (@RequestBody EsAlarmSearchVO vo){
        return  R.data(alarmService.queryAlarm(vo));
    }


    @GetMapping("/FindById")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据id查询接口", notes = "传入id")
    public R<EsAlarmVO> findById(@RequestParam("id") String id){
        return alarmService.findById(id);
    }
    @GetMapping("/test")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "测试接口", notes = "传入id")
    public R test(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<>();
        return R.data(personTrafficFlowDao.queryPersonTrafficflowStatisticsByCount(map));
    }



}
