package com.example.demo.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.FcmResponse;

@Service
public class AndroidPushNotificationsService {
	//private static final String FIREBASE_SERVER_KEY = "Your Server Key here!";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
    @Value("${fcm.authKey}")
    private  String authKey;
    
	@Async
	public CompletableFuture<FcmResponse> send(HttpEntity<String> entity) {
 
		RestTemplate restTemplate = new RestTemplate();
 
		/**
		https://fcm.googleapis.com/fcm/send
		Content-Type:application/json
		Authorization:key=FIREBASE_SERVER_KEY*/
		
		//System.out.println(authKey);
 
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + authKey));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json;charset=UTF-8"));
		restTemplate.setInterceptors(interceptors);
		restTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("UTF-8")));
		System.out.println(entity);
 
		FcmResponse firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, FcmResponse.class);
		//ResponseEntity<String> firebaseResponse = restTemplate.exchange(FIREBASE_API_URL,HttpMethod.POST, entity, String.class);
		System.out.println(firebaseResponse);
		 
		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity, String uuid) {
 
		RestTemplate restTemplate = new RestTemplate();
 
		/**
		https://fcm.googleapis.com/fcm/send
		Content-Type:application/json
		Authorization:key=FIREBASE_SERVER_KEY*/
		
		//System.out.println(authKey);
 
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + authKey));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		interceptors.add(new HeaderRequestInterceptor("uuid",uuid ));
		restTemplate.setInterceptors(interceptors);
		System.out.println(entity);
 
		//String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
		ResponseEntity<String> firebaseResponse = restTemplate.exchange(FIREBASE_API_URL,HttpMethod.POST, entity, String.class);
		//System.out.println(firebaseResponse.getBody());
		//System.out.println(firebaseResponse.getHeaders());
		 
		return CompletableFuture.completedFuture(firebaseResponse.getBody());
	}
	
	
	
	
	
}
