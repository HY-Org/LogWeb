package org.hy.log.common;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.hy.common.app.Param;
import org.hy.common.xml.XJava;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;





/**
 * Restful接口注册服务类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0
 */
@ApplicationPath("/")
public class ApplicationRestfuls extends Application 
{
    
    @SuppressWarnings("unchecked")
    @Override
    public Set<Class<?>> getClasses() 
    {
        final Set<Class<?>> v_Classes     = new HashSet<Class<?>>();
        Map<String ,Param>  v_AppRestfuls = (Map<String ,Param>)XJava.getObject("AppRestfuls");
        
        for (Param v_Param : v_AppRestfuls.values())
        {
            try
            {
                v_Classes.add(Class.forName(v_Param.getValue()));
            }
            catch (Exception exce)
            {
                exce.printStackTrace();
                throw new VerifyError("-- 接口注册异常：" + v_Param.getName() + " : " + v_Param.getValue());
            }
        }
        
        return v_Classes;
    }
    
}
