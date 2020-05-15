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
package org.springblade.common.constant;

/**
 * 通用常量
 *
 * @author Chill
 */
public interface CitmsAppConstant {

	/**
	 * DefaultToken:
	 */
	String AUTHORIZATION = "Basic 0FA7WB2KP3R317519OBPM05";

	/**
	 * 人脸聚类
	 */
	String APPLICATION_MBD_NAME = "icw";

	/**
	 * 当前登录用户token
	 */
	String WEBTOKEN = "WebToken:";

	/**
	 * 默认用户token
	 */
	String DEFAULTTOKEN = "DefaultToken:";


	/**
	 * 用户信息
	 */
	String USERINFO = "UserInfo:";


    /**
     * es报警 所用key
     */
    String ALARM_CLUSTER_ES_INDEX = "xai.aip.std.common.alarm";
    String ALARM_CLUSTER_ES_INDEX_SEARCH = "xai.aip.std.common.alarm/_search";
    String ES_SEARCH_ALARM_PATH="esmapper/EsAlarmXML.xml";
    String SEARCH_ALARM = "getAlarmInfo";
    String SEARCH_ALARM_BY_ID = "findById";

	/**
     * es 巡更 所用key
     */
    String PATROLLOG_CLUSTER_ES_INDEX = "xai.aip.std.village.patrollog";
    String PATROLLOG_CLUSTER_ES_INDEX_SEARCH = "xai.aip.std.village.patrollog/_search";
    String ES_SEARCH_PATROLLOG_PATH="esmapper/EsPatrollogXML.xml";
    String SEARCH_PATROLLOG_INFO = "getPartollogInfo";
	String SEARCH_PATROLLOG_BYID = "getPatrollogById";

    /**
     * es 人员 所用key
     */
    String PERSON_CLUSTER_ES_INDEX = "xai.aip.std.facerecog.facecheck";
    String PERSON_CLUSTER_ES_INDEX_SEARCH = "xai.aip.std.facerecog.facecheck/_search";
    String ES_SEARCH_PERSON_PATH="esmapper/EsPersonXML.xml";
    String SEARCH_PERSON = "queryPerson";

    /**
     * es 车辆 所用key
     */
    String VEHICLE_CLUSTER_ES_INDEX = "xai.aip.std.tollgate.vehicle";
    String VEHICLE_CLUSTER_ES_INDEX_SEARCH = "xai.aip.std.tollgate.vehicle/_search";
    String ES_SEARCH_VEHICLE_PATH="esmapper/EsVehicleXML.xml";
    String SEARCH_VEHICLE = "queryVehicle";

    /**
     * es 人流 所用key
     */
    String PERSON_TRAFFICFLOW_CLUSTER_ES_INDEX = "xai.sq.trafficflow.person";
    String PERSON_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH = "xai.sq.trafficflow.person/_search";
    String ES_SEARCH_PERSON_TRAFFICFLOW_PATH="esmapper/PersonTraffixFlow.xml";

    /**
     * es 车流 所用key
     */
    String VEHICLE_TRAFFICFLOW_CLUSTER_ES_INDEX = "xai.sq.trafficflow.vehicle";
    String VEHICLE_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH = "xai.sq.trafficflow.vehicle/_search";
    String ES_SEARCH_VEHICLE_TRAFFICFLOW_PATH="esmapper/VehicleTraffixFlow.xml";


    int THREAD_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() / 2;
    int THREAD_MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    String ZERO ="0";
    String ONE ="1";
    String TWO ="2";
    String THREE ="3";
}
