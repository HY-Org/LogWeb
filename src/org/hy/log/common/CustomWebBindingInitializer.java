package org.hy.log.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;





public class CustomWebBindingInitializer implements WebBindingInitializer 
{
	
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) 
	{
		binder.registerCustomEditor(java.util.Date.class, new DateEditor(DATEFORMAT, true));
		binder.registerCustomEditor(Timestamp.class, new DateEditor(TIMEFORMAT, true));

	}

}
