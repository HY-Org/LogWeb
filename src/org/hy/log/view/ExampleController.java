package org.hy.log.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 控制层的样例 
 *
 * @author      ZhengWei(HY)
 * @createDate  2017-04-07
 * @version     v1.0
 */
@Controller 
public class ExampleController
{
    
    @RequestMapping("/hello")  
    public String hello(Model io_Model) 
    {  
        //将数据添加到视图数据容器中  
        io_Model.addAttribute("param01" ,"Hello ");  
        io_Model.addAttribute("param02" ,"World!");  
        
        return "Hello";  
    }  
    
}
