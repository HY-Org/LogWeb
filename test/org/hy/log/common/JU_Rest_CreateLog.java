package org.hy.log.common;

import org.hy.common.Date;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.thread.Task;
import org.hy.common.thread.TaskPool;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试：创建日志
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-30
 * @version     v1.0
 */
@Xjava(XType.XML)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_Rest_CreateLog extends BaseJunit
{
    private static final String $TaskType = "RestTask";
    
    private static       int    $SerialNo = 0;
    
    private static       int    $Count    = 0;
    
    
    
    @Test
    public void test_001()
    {
        for (int x=1; x<10; x++)
        {
            for (int i=1; i<=10000; i++)
            {
                TaskPool.putTask(new RestTask(i));
            }
        }
        
        while ( ThreadPool.getActiveThreadCount() >= 1 )
        {
            ThreadPool.sleep(1000 * 60);
        }
    }
    
    
    
    public synchronized int getCount()
    {
        return ++$Count;
    }
    
    
    
    public void executeRestTask(int i_Index)
    {
        Param  v_RestRequest  = (Param)this.getObject("RestRequest_CreateLog");
        String v_RestInfo     = v_RestRequest.getValue();
        Date   v_BeginTime    = new Date();
        
        String v_RestResponse = BaseAppMessage.restRequest(v_RestRequest.getName() ,v_RestInfo ,"UTF-8");
        
        
        System.out.println("-- TNO = " + StringHelp.lpad(i_Index    ,8 ," ") 
                         + "   CNO = " + StringHelp.lpad(getCount() ,8 ," ") 
                         + "   " + (Date.getNowTime().getTime() - v_BeginTime.getTime()) + "毫秒    "
                         + Date.getNowTime().getFullMilli() + " " + v_RestResponse);
    }
    
    
    
    
    
    class RestTask extends Task<Object>
    {
        private int index;
        
        
        
        public RestTask(int i_Index)
        {
            super($TaskType);
            
            this.index = i_Index;
        }


        private synchronized int GetSerialNo()
        {
            return ++$SerialNo;
        }
        
        
        @Override
        public void execute()
        {
            executeRestTask(this.index);
            
            this.finishTask();
        }

        
        @Override
        public int getSerialNo()
        {
            return GetSerialNo();
        }
        

        @Override
        public String getTaskDesc()
        {
            return "" + this.getTaskNo();
        }
        
    }
    
}
