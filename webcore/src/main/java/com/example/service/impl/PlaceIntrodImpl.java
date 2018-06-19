package com.example.service.impl;

import javax.jws.WebService;

import com.example.service.PlaceIntrod;

@WebService(endpointInterface="com.test.service.PlaceIntrod",serviceName="PlaceIntrodImpl")
public class PlaceIntrodImpl implements PlaceIntrod {

	@Override
	public String getIntrod(String place) {
		// TODO Auto-generated method stub
		return "旅游"+place+",人间天堂。";
	}

}
