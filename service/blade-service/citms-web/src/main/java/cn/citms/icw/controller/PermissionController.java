package cn.citms.icw.controller;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.dto.CommunityDTO;
import cn.citms.icw.entity.Community;
import cn.citms.icw.service.ICommunityService;
import cn.citms.icw.vo.CommunityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.UserContextHolder;
import org.springblade.common.vo.UserRightViewModel;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Permission")
@Api(value = "权限", tags = "权限接口")
public class PermissionController {

    @Value("${dgaRpc.getChildDepartmentIds}")
    private String getChildDepartmentIdsUrl;

    @Resource
    private ICommunityService communityService;

    /**
     * 分页 社区信息
     */
    @GetMapping("/GetCommunity")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取当前用户有权限查看的社区集合", notes = "传入community")
    public R<List<Community>> list() {
        List<Community> communityIds = new ArrayList<>();
        UserRightViewModel userInfo = UserContextHolder.getUserInfo();
        if(userInfo != null && StringUtils.isNotBlank(userInfo.getDepartmentId())){
            List<String> departmentIds = CommonUtils.getChildDepartmentIds(getChildDepartmentIdsUrl, userInfo.getDepartmentId());
            if(!CollectionUtils.isEmpty(departmentIds)){
                communityIds = communityService.selectCommunityInfoByDeptIds(departmentIds);
            }
        }
        return R.data(communityIds);
    }

}
