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
	public void release(HttpServletRequest request, HttpServletResponse response) {
		System.gc();
	}

	@Override
	@RequestMapping(value = "/**")
	public String defaultAction(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return "404";
	}

	@RequestMapping(value = "/run/{module}/{method}")
	@ResponseBody
	public Success exampleRun(@PathVariable("module") String module, @PathVariable("method") String method) {
		try {
			//String action = request.getParameter("action");
			String action = request.getParameter("action");
			if (action == null || action.isEmpty())
				action = "index";
			action = Alphabetic.instance().ucfirst(action) + "Action"; 
			if (module == null || module.isEmpty()) {
				module = "";
			} else {
				module = module.toLowerCase() + ".";
			}
			request.setAttribute("method", method);
			Class<?> controllerClass = Class.forName("com.Example.controller."+module+action);
			Object controllerAction = controllerClass.newInstance();

			Method excute = controllerClass.getMethod("excute", HttpServletRequest.class, HttpServletResponse.class);
			Object tempObj = excute.invoke(controllerAction, request, response);
			this.release(request, response);
			return (Success) tempObj;
		} catch (Exception e) {
			this.release(request, response);
			MDC.put("APP_NAME", "web_error");
			logger.error("系统错误", e);
		}
		Success successType = new Success();
		successType.setSuccess(false);
		successType.setErrorCode(ErrorCode.__F_SYS);
		return successType;
	}


}
