package cn.citms.icw.controller;

import cn.citms.icw.service.IVillageService;
import cn.citms.icw.vo.CommunityDetailVO;
import cn.citms.icw.vo.VillageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 智慧社区基本信息查询控制器
 * @author cyh
 */
@RestController
@AllArgsConstructor
@RequestMapping("/Village")
@Api(value = "智慧社区基本信息", tags = "智慧社区基本信息查询接口")
public class VillageDalController {

    private IVillageService villageService;

    @GetMapping("/GetCommunityDataList")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取社区数据")
    public R<List<VillageVO>> listAll() {
        return R.data(villageService.getVillageCount());
    }

    /**
     * 获取智慧社区详情数据
     */
    @GetMapping("/GetCommunityDetail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取智慧社区详情数据", notes = "智慧社区id")
    public R<CommunityDetailVO> detail(@RequestParam String id) {
        CommunityDetailVO detail = villageService.selectCommunityDetail(id);
        return R.data(detail);
    }
}
