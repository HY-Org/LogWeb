package org.hy.log.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.hy.log.common.BaseJunit;
import org.hy.log.enums.ERegisterType;
import org.hy.log.msg.MsgLogRegisterRequest;
import org.hy.log.service.impl.LogInfoService;
import org.hy.common.Date;





/**
 * 测试单元：日志操作的服务类
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-14
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_LogInfoService extends BaseJunit
{
    private LogInfoService  logInfoService = (LogInfoService) this.getObject("LogInfoService");
    
    
    
    @Test
    public void buildLogTable()
    {
        MsgLogRegisterRequest v_Msg = new MsgLogRegisterRequest();
        
        v_Msg.setSysID("HY");
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Normal);
        this.logInfoService.buildLogTable(v_Msg ,new Date());
        
        
        // 测试：年
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_Year);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-06-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));
        
        
        // 测试：月
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_Month);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-05-15"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-09-30"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));
        
        
        // 测试：旬
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_10Day);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-05-15"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-09-30"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));
        
        
        // 测试：周
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_Week);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-05-15"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-09-30"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));  // 边界值
        
        
        // 测试：天
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_Day);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-05-15"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-09-30"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));
    }
    
    
    
    @Test
    public void buildLogTable_Hour()
    {
        MsgLogRegisterRequest v_Msg = new MsgLogRegisterRequest();
        
        v_Msg.setSysID("HY");
        
        // 测试：小时
        v_Msg.setRegisterTypeEnumInfo(ERegisterType.$Partition_Hour);
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-01-01"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-05-15"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-09-30"));
        this.logInfoService.buildLogTable(v_Msg ,new Date("2014-12-31"));
    }
    
}
