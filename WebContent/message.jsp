<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>   
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="jsoneditor-3.1.2/jquery.min.js"></script> 
<title>Comet Weather</title>

<script TYPE="text/javascript">
	function messageListener()
	{
    	var v_URL     = '<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>/LogWeb/messageListener?rid=<%=request.getParameter("rid")%>';
        var v_Request =  new XMLHttpRequest();
        
        v_Request.open("POST" ,v_URL ,true);
        v_Request.setRequestHeader("Content-Type" ,"application/x-javascript;");
        v_Request.onreadystatechange = function() 
        {
			if ( v_Request.readyState == 3 && v_Request.status == 200 ) 
			{
				if ( v_Request.responseText ) 
				{
					$('#messageInfo').val("\n" + v_Request.responseText);
				}
			}
		};
		v_Request.send(null);
	}
	
	
	
	function send()
	{   
	     $.ajax({   
	         type: "POST"   
	        ,url: "message"    
	        ,data: {
	        	    sender: '<%=request.getParameter("rid")%>'
	        	   ,receiver: $('#Receiver').val() 
	        	   ,msg: $('#Msg').val()
	        	   }
	     });   
	}  
	
	
	
	function sendAll()
	{   
	     $.ajax({   
	         type: "POST"   
	        ,url: "message"    
	        ,data: {
	        	    sender: "$ADMIN$"
	        	   ,receiver: "$ALL$"
	        	   ,msg: $('#Msg').val()
	        	   }
	     });   
	} 
</script>

</head>
    <body onload="messageListener();">
    	<textarea id="messageInfo" rows="16" cols="100"></textarea>
    	
    	<br />
    	<label>接收者：</label>
    	<input type="text"   id="Receiver" width="300"></input>
    	
    	<br />
    	<label>消息：</label>
    	<input type="text"   id="Msg" width="300"></input>
        <input type="button" onclick="send()"    value="发送"></input>
        
        <input type="button" onclick="sendAll()" value="向所有人发送"></input>
    </body>
</html>