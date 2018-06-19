package com.example.ws;

import com.example.service.PlaceIntrod;

public class ClientMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PlaceIntrodImpl placeIntrodImpl = new PlaceIntrodImpl();
		PlaceIntrod placeIntrod = placeIntrodImpl.getPlaceIntrodImplPort();
		System.out.println(placeIntrod.getIntrod("神游"));
	}

}
