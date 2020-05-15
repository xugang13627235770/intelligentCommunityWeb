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
package cn.citms.icw.dto;

import cn.citms.icw.entity.Personcheckin;
import cn.citms.icw.utils.IntergerSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 住户信息数据传输对象实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersoncheckinDTO extends Personcheckin {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("社区名称")
	private String sqmc;

	@ApiModelProperty("社区类型")
	private String sqlx;

	@ApiModelProperty("楼栋名称")
	private String ldmc;

	@ApiModelProperty("单元名称")
	private String dymc;

	@ApiModelProperty("门户ID")
	private String houseId;

	@ApiModelProperty("门户名称")
	private String mhmc;

	@ApiModelProperty("人员类型")
	private String dic_Rylx;

	@ApiModelProperty("附件信息")
	private List<AttachmentsDto> files;

	@ApiModelProperty("民族")
	private String dic_Mz;

	@ApiModelProperty("性别")
	private String dic_Xb;

	@ApiModelProperty("证件类型")
	private String dic_Zjlx;

	@ApiModelProperty("用户标识")
	private String dic_Yhbs;

	@ApiModelProperty("出生日期")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date dic_Csrq;

	@ApiModelProperty("房屋状态")
	private String fwZt;

	@ApiModelProperty("文化程度")
	private String dic_Whcd;

	@ApiModelProperty("婚姻状况")
	private String dic_Hyzk;

	@ApiModelProperty("居住事由")
	private String dic_Jzsy;

	@ApiModelProperty("职业类别")
	private String dic_Zylb;

	@ApiModelProperty("暂住处所")
	private String dic_Zzcs;

	@ApiModelProperty("服务站人数")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer fwzrs;

	@ApiModelProperty("警务室人数")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer jwsrs;

}
