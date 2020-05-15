/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package cn.citms.icw.vo;

import cn.citms.icw.entity.Unit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 单元信息视图实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UnitVO对象", description = "单元信息")
public class UnitVO extends Unit {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区名称")
	private String sqmc;
	@ApiModelProperty(value = "楼栋名称")
	private String ldmc;

    @ApiModelProperty(value = "楼栋-单元名称")
	private String buildUnitName;

	@ApiModelProperty(value = "附件集合")
	private List<AttachmentVO> files;

	@ApiModelProperty(value = "页码数")
	private Integer pagesize;
	@ApiModelProperty(value = "第几页")
	private Integer pageindex;
}
