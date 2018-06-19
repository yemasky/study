package com.example.ws;

import javax.xml.ws.Endpoint;

import com.example.service.PlaceIntrod;
import com.example.service.impl.PlaceIntrodImpl;

public class ServerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PlaceIntrod placeIntrod = new PlaceIntrodImpl();
		Endpoint.publish("http://127.0.0.1/kfserver", placeIntrod);
	}

}
