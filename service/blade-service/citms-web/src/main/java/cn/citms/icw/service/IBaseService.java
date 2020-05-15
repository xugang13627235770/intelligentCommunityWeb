package cn.citms.icw.service;

import cn.citms.icw.entity.Community;

import java.util.List;

/**<pre>
 *      基础服务
 * </pre>
 * @author liuyuyang
 */
public interface IBaseService {

    /**
     * 根据小区编码获取社区信息
     * @param code  {@link String} 社区编码参数
     * @return Community {@link Community} 返回值
     */
    Community getCommunityInfo(String code);
}
