package com.MpEnterprise.controller;

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

@Controller
@RequestMapping("/mp")
public class AppController extends AbstractController {

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public String defaultAction(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/app/{module}")
	@ResponseBody
	public Success appRun(@PathVariable("module") String module) {
		try {
			String action = request.getParameter("action");
			//String module = request.getParameter("module");

			if (action == null || action.isEmpty())
				action = "index";
			action = Alphabetic.instance().ucfirst(action) + "Action";
			if (module == null || module.isEmpty()) {
				module = "";
			} else {
				module = module.toLowerCase() + ".";
			}
			Class<?> controllerClass = Class.forName("com.MpEnterprise.controller." + module + action);
			Object controllerAction = controllerClass.newInstance();

			Method excute = controllerClass.getMethod("excute", HttpServletRequest.class, HttpServletResponse.class);
			Object tempObj = excute.invoke(controllerAction, request, response);

			return (Success) tempObj;
		} catch (Exception e) {
			MDC.put("APP_NAME", "web_example_error");
			//logger.error("系统错误", e);
		}
		Success successType = new Success();
		successType.setSuccess(false);
		successType.setCode(ErrorCode.__F_SYS);
		return successType;
	}

}
