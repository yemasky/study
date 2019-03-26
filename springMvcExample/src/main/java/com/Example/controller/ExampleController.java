package com.Example.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.AbstractController;
import com.base.type.ErrorCode;
import com.base.type.Success;

import core.util.Alphabetic;

//import com.Example.controller.action.*;

@Controller
@RequestMapping("/example")
public class ExampleController extends AbstractController {

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}


	@Override
	public void afterCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String defaultAction(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/test/{action}")
	@ResponseBody
	public Success example(@PathVariable("action") String action) {
		try {
			//String action = request.getParameter("action");
			if(action == null || action.isEmpty()) action = "index";
			action = Alphabetic.instance().ucfirst(action) + "Action"; 
			Class<?> controllerClass = Class.forName("com.Example.controller.action."+action);
			Object controllerAction = controllerClass.newInstance();

			Method excute = controllerClass.getMethod("excute", HttpServletRequest.class, HttpServletResponse.class);
			Object tempObj = excute.invoke(controllerAction, request, response);
		
			return (Success) tempObj;
		} catch (Exception e) {
			MDC.put("APP_NAME", "web_error");
			logger.error("系统错误", e);
		}
		Success successType = new Success();
		successType.setSuccess(false);
		successType.setErrorCode(ErrorCode.__F_SYS);
		return successType;
	}


}
