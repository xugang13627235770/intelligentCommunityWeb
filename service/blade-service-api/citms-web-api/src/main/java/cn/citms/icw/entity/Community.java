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
package cn.citms.icw.entity;

import cn.citms.icw.utils.IntergerSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.frameworkset.orm.annotation.Column;
import org.springblade.core.mp.base.BaseEntity;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 社区信息实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_community")
@ApiModel(value = "Community对象", description = "社区信息")
public class Community  {

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
		@ApiModelProperty(value = "ID")
		private String id;
	/**
	* 小区名称
	*/
		@ApiModelProperty(value = "小区名称")
		private String xqmc;
	/**
	* 小区编码
	*/
		@ApiModelProperty(value = "小区编码")
		private String xqbm;
	/**
	* 小区标识
	*/
		@ApiModelProperty(value = "小区标识")
		@TableField("xq_bs")
		private String xq_Bs;
	/**
	* 小区地址
	*/
		@ApiModelProperty(value = "小区地址")
		private String xqdz;
	/**
	* 街路巷名称
	*/
		@ApiModelProperty(value = "街路巷名称")
		private String jlxmc;
	/**
	* 社区类型
	*/
		@ApiModelProperty(value = "社区类型")
		private String sqlx;
	/**
	* 建成年份
	*/
		@ApiModelProperty(value = "建成年份")
		private String jcnf;
	/**
	* 创建时间
	*/
		@ApiModelProperty(value = "创建时间")
		@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
		private Date cjsj;
	/**
	* 占地面积
	*/
	    @JsonSerialize(nullsUsing = IntergerSerializer.class)
		@ApiModelProperty(value = "占地面积")
		private Double zdmj;
	/**
	* 村社区代码
	*/
		@ApiModelProperty(value = "村社区代码")
		private String csqdm;
	/**
	* 村社区名称
	*/
		@ApiModelProperty(value = "村社区名称")
		@TableField("csq_mc")
		private String csq_Mc;
	/**
	* 社区/居（村）委会名称
	*/
		@ApiModelProperty(value = "社区/居（村）委会名称")
		private String sqjcwhmc;
	/**
	* 省代码
	*/
		@ApiModelProperty(value = "省代码")
		private String sdm;
	/**
	* 省份名称
	*/
		@ApiModelProperty(value = "省份名称")
		@TableField("sf_mc")
		private String sf_Mc;
	/**
	* 区县代码
	*/
		@ApiModelProperty(value = "区县代码")
		private String qxdm;
	/**
	* 区县名称
	*/
		@ApiModelProperty(value = "区县名称")
		private String qxmc;
	/**
	* 市代码
	*/
		@ApiModelProperty(value = "市代码")
		private String shdm;
	/**
	* 市名称
	*/
		@ApiModelProperty(value = "市名称")
		private String smc;
	/**
	* 乡镇（街道）代码
	*/
		@ApiModelProperty(value = "乡镇（街道）代码")
		private String xzjddm;
	/**
	* 乡镇（街道）名称
	*/
		@ApiModelProperty(value = "乡镇（街道）名称")
		private String xzjdmc;
	/**
	* 所属辖区
	*/
		@ApiModelProperty(value = "所属辖区")
		private String ssxq;
	/**
	* 执勤民警
	*/
		@ApiModelProperty(value = "执勤民警")
		private String zqmj;
	/**
	* 警务室人数
	*/
		@ApiModelProperty(value = "警务室人数")
		@JsonSerialize(nullsUsing = IntergerSerializer.class)
		private Double jwsrs;
	/**
	* 所属责任区代码
	*/
		@ApiModelProperty(value = "所属责任区代码")
		private String sszrqdm;
	/**
	* 服务处所
	*/
		@ApiModelProperty(value = "服务处所")
		private String fwcs;
	/**
	* 地球经度
	*/
		@ApiModelProperty(value = "地球经度")
		@JsonSerialize(nullsUsing = IntergerSerializer.class)
		private Double dqjd;
	/**
	* 地球纬度
	*/
		@ApiModelProperty(value = "地球纬度")
		@JsonSerialize(nullsUsing = IntergerSerializer.class)
		private Double dqwd;
	/**
	* 物业公司名称
	*/
		@ApiModelProperty(value = "物业公司名称")
		private String wymc;
	/**
	* 物业负责人
	*/
		@ApiModelProperty(value = "物业负责人")
		private String wyfzr;
	/**
	* 卫生服务站
	*/
		@ApiModelProperty(value = "卫生服务站")
		private String fwzmc;
	/**
	* 服务站人数
	*/
		@ApiModelProperty(value = "服务站人数")
		@JsonSerialize(nullsUsing = IntergerSerializer.class)
		private Double fwzrs;


}
