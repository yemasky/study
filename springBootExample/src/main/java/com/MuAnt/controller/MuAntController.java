package com.MuAnt.controller;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.AbstractController;
import com.base.type.ErrorCode;
import com.base.type.Success;

import core.util.Alphabetic;

@Controller
@RequestMapping("/muant")
public class MuAntController extends AbstractController {

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	@RequestMapping(value = "/**")
	public String defaultAction(HttpServletRequest request, HttpServletResponse response) {
		
		return "404";
	}

	@RequestMapping(value = "/")
	public String indexAction(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("Title", "后台管理");
		model.addAttribute("__VERSION", "1.20");
		model.addAttribute("__RESOURCE", "/resource/");
		Date dateNow = new Date( );
	    SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	 	model.addAttribute("thisDateTime", dateFormat.format(dateNow));
	 	model.addAttribute("__WEB", "/muant/app/");
	 	model.addAttribute("home_channel", "");
	 	model.addAttribute("noLogin", 1);
		return "muant/index";
	}
	
	@RequestMapping(value = "/app/{module}/{method}")
	@ResponseBody
	public Success appRun(@PathVariable("module") String module, @PathVariable("method") String method) {
		try {
			String action = request.getParameter("action");
			if (action == null || action.isEmpty()) {
				action = "index";
			}
			action = Alphabetic.instance().ucfirst(action) + "Action";
			if (module == null || module.isEmpty()) {
				module = "";
			} else {
				module = module.toLowerCase() + ".";
			}
			method = method == null || method.equals("") ? request.getParameter("method") : method;
			request.setAttribute("method", method);
			Class<?> controllerClass = Class.forName("com.MuAnt.controller." + module + action);
			Object controllerAction = controllerClass.newInstance();

			Method excute = controllerClass.getMethod("excute", HttpServletRequest.class, HttpServletResponse.class);
			Object tempObj = excute.invoke(controllerAction, request, response);

			return (Success) tempObj;
		} catch (Exception e) {
			MDC.put("APP_NAME", "web_muant_error");
			logger.error("系统错误", e);
		}
		Success success = new Success();
		success.setSuccess(false);
		success.setCode(ErrorCode.__F_SYS);
		return success;
	}

}
