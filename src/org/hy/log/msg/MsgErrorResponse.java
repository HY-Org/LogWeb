package org.hy.log.msg;

import org.hy.common.app.Param;





/**
 * 接口统一错误信息
 * 
 *   错误编码格式：AA   BB   CC   DDD    E
 *   错误编码共10位编码；
 *        AA 表示系统名称       ，用数字表示；          例：01表示商城前台系统、02表示商城管理系统；
 *        BB 表示模块(类)名称，用数字表示；          例：01表示商家信息模块、02表示商铺信息模块；
 *        CC 表示业务操作名称，用数字表示；          例：01表示创建订单、02表示终止订单；
 *        DDD表示信息编号       ，用数字和字母表示；例：A01表示创建订单时的数据存储服务，A02表示订单校验错误；建议字母全为大字
 *        E  表示错误类别       ，用数字表示；           ‘0’和‘1’表示系统类错误和业务类错误；
 *        
 *   统一规范：
 *        01：商城前台
 *        02：商城运营管理
 *        03：第三方接口
 *        04：业务逻辑
 *        05：CMS
 *        06：会员
 *        07：商户
 *        08：商品
 *        09：结算
 *        10：订单
 *        11：诚信评价
 *        12：系统管理
 *        13: 日志
 *        
 *   日志系统的模块分类(BB级)
 *        00：通用模块
 *        01：创建模块  - Log.Create
 * 
 * @author      ZhengWei(HY)
 * @version     v1.0  
 * @createDate  2014-09-28
 */
public class MsgErrorResponse
{
    
    /** 错误编码 */
    private String errorCode;
    
    /** 错误信息 */
    private String errorInfo;
    
    
    
    public MsgErrorResponse()
    {
        
    }
    
    
    
    public MsgErrorResponse(Param i_Param)
    {
        this.errorCode = i_Param.getName();
        this.errorInfo = i_Param.getValue();
    }

    
    
    /**
     * 获取：错误编码
     */
    public String getErrorCode()
    {
        return errorCode;
    }


    
    /**
     * 设置：错误编码
     * 
     * @param errorCode 
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }


    
    /**
     * 获取：错误信息
     */
    public String getErrorInfo()
    {
        return errorInfo;
    }


    
    /**
     * 设置：错误信息
     * 
     * @param errorInfo 
     */
    public void setErrorInfo(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }
    
}
