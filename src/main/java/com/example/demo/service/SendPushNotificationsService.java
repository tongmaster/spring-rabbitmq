package com.example.demo.service;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Application;
import com.example.demo.model.CodeResponse;
import com.example.demo.model.CustomField;
import com.example.demo.model.FcmResponse;
import com.example.demo.model.FcmResult;
import com.example.demo.model.Message;
import com.example.demo.model.MessageResponse;
import com.example.demo.model.Notif;
import com.example.demo.model.NotifResponse;
import com.example.demo.model.Payload;
import com.example.demo.model.Receiver;
import com.example.demo.model.TokenDevice;

@Service
public class SendPushNotificationsService {
	private static final Logger log = LoggerFactory.getLogger(SendPushNotificationsService.class);
	//private static final String FIREBASE_SERVER_KEY = "Your Server Key here!";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
    @Value("${fcm.authKey}")
    private  String authKey;
    
    @Autowired
	ApplicationService appService;

	@Autowired
	TokenDeviceService tokenDeviceService;

	@Autowired
	MessageService messageService;

	@Autowired
	CustomService customService;

	@Autowired
	ReceiverService recService;

	@Autowired
	NotifService notifService;
	
	@Autowired
	CodeResponseService  codeResponseService;


	@Autowired
	NotifResponseService notifRespService;
	
	
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
	
	public void pushNotificationFromQueues(Payload payload) {


		/* Sending to Message Queue */
		log.info(">>>>>>>>>>>> "+payload.toString());
		Message msg = new Message();
		try {
		
			
			if (payload.getReceiver() != null && payload.getReceiver().size() > 0) {
				log.info("2 >>>>>>>>>>>> "+payload.toString());
				if(payload.getReceiver().size() == 1) {
					
					log.info("3 >>>>>>>>>>>> "+payload.toString());
					JSONObject body = new JSONObject();
					Application app = new Application();
					app.setAppId(payload.getMessage().getAppId());
					msg.setApplication(app);
					msg.setBadge(payload.getMessage().getBadge());
					msg.setBody(payload.getMessage().getBody());
					msg.setBroadcast(false);
					msg.setClickAction(payload.getMessage().getClickAction());
					msg.setMsgType(Message.enum_msg_type.valueOf(payload.getMessage().getMsgType()));
					msg.setPriority(payload.getMessage().getPriority());
					// msg.setSendTime(sendTime);
					msg.setSound(payload.getMessage().getSound());
					msg.setTimeToLive(payload.getMessage().getTimeToLive());
					msg.setTitle(payload.getMessage().getTitle());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					msg.setSendTime(timestamp);
					msg.setQueue(false);
					msg.setMessageStatus("S");
					msg.setMsgId(payload.getMessage().getMsgId());
					JSONObject notification = new JSONObject();
					notification.put("title", payload.getMessage().getTitle());
					notification.put("text", payload.getMessage().getBody());
					notification.put("sound", payload.getMessage().getSound());
					notification.put("badge", payload.getMessage().getBadge());
					notification.put("click_action", payload.getMessage().getClickAction());
					notification.put("time_to_live", payload.getMessage().getClickAction());
					body.put("notification", notification);
					log.info("3 >>>>>>>>>>>> "+payload.toString());
					int test = messageService.updateByMsgId(msg);
					
					log.info("4 >>>>>>>>>>>> "+test	);
				/*	JSONObject data = new JSONObject();
					if (payload.getData().size() > 0) {
						CustomField cus = null;
						for (Map.Entry<String, String> entry : payload.getData().entrySet()) {
							cus = new CustomField();
							cus.setMsgId(payload.getMessage().getMsgId());
							cus.setKeyName(entry.getKey());
							cus.setKeyValue(entry.getValue());
							data.put(entry.getKey(), entry.getValue());
							//cusSet.add(cus);
							customService.saveOrUpdate(cus);
							System.out.println(">>>>>>>>>>>> "+entry.getKey()+" "+entry.getValue());
						}
					}*/
					//Set<Receiver> recSet = new HashSet<Receiver>();
					// insert receiver
					JSONArray array = new JSONArray();
					JSONArray arrayToken = new JSONArray();
					List<TokenDevice> listUserRef = tokenDeviceService.findTokenByUserRefAndAppId(payload.getReceiver().get(0),payload.getMessage().getAppId());
					if(listUserRef.size() == 1) {
						body.put("to", listUserRef.get(0).getToken());
						JSONObject objNotifResp =new JSONObject();
						objNotifResp.put("tokenId", listUserRef.get(0).getTokenId());
						objNotifResp.put("token", listUserRef.get(0).getToken());
						arrayToken.put(listUserRef.get(0).getToken());
						array.put(objNotifResp);
					}
					else {
						for (int i = 0; i < listUserRef.size(); i++) {
							JSONObject objNotifResp =new JSONObject();
							objNotifResp.put("tokenId", listUserRef.get(i).getTokenId());
							objNotifResp.put("token", listUserRef.get(i).getToken());
							arrayToken.put(listUserRef.get(i).getToken());
							array.put(objNotifResp);
						}
						body.put("registration_ids", arrayToken);
					}
					log.info("5 >>>>>>>>>>>> ");
					/*rec.setMsgId(id);
					rec.setUserRef(payload.getReceiver().get(0));
					rec.setNotifStatus("N");
					recService.saveOrUpdate(rec);*/
					
					//Set<CustomField> cusSet = new HashSet<>();
					
					
					Notif notif = new Notif();
					notif.setNotifStatus("N");
					notif.setRequestBody(body.toString());
					notif.setMsgId(payload.getMessage().getMsgId());
					notif.setRequestTime(new Timestamp(System.currentTimeMillis()));
					HttpEntity<String> request = new HttpEntity<>(body.toString());
					CompletableFuture<FcmResponse> pushNotification = send(request);
					CompletableFuture.allOf(pushNotification).join();
					FcmResponse resp = pushNotification.get();
					System.out.println(" >>> " + resp);
					JSONObject res = new JSONObject(resp);
					notif.setResponseBody(res.toString());
					Receiver rec = new Receiver();
					log.info("5.1 >>>>>>>>>>>> "+res.toString());
					if (res.has("success") && res.getInt("success") > 0 && res.getInt("failure") == 0) {
						notif.setNotifStatus("S");
						notif.setResponseTime(new Timestamp(System.currentTimeMillis()));
						rec.setMsgId(payload.getMessage().getMsgId());
						rec.setUserRef(payload.getReceiver().get(0));
						rec.setNotifStatus("S");
						recService.updateReceiverByMsgId(rec);
						log.info("5.2 >>>>>>>>>>>> ");
						Notif resultNotif = notifService.saveOrUpdate(notif);
						
						
						List<FcmResult> fcmListResp = resp.getResults();
						
						for (int i = 0; i < fcmListResp.size(); i++) {
							 NotifResponse nfr = new NotifResponse();
							 nfr.setResponseId(0);
							 JSONObject obj = array.getJSONObject(i);
							 nfr.setTokenId(obj.getInt("tokenId"));
							 nfr.setNotifUuid(resultNotif.getNotifUuid());
							 notifRespService.saveOrUpdate(nfr);	
						}
					}
					else {
						notif.setNotifStatus("F");
						notif.setResponseTime(new Timestamp(System.currentTimeMillis()));
						rec.setMsgId(payload.getMessage().getMsgId());
						rec.setUserRef(payload.getReceiver().get(0));
						rec.setNotifStatus("F");
						log.info("6 >>>>>>>>>>>> ");
						recService.updateReceiverByMsgId(rec);
					
						Notif resultNotif = notifService.saveOrUpdate(notif);
						
						List<FcmResult> fcmListResp = resp.getResults();
						
						for (int i = 0; i < fcmListResp.size(); i++) {
							FcmResult fcmResult = fcmListResp.get(i);
						//for (FcmResult fcmResult : fcmListResp) {
							System.out.println(fcmResult.getError());
							System.out.println(fcmResult.getMessage_id());
							if(fcmResult.getError() != null) {
								 CodeResponse insertReponseCode = codeResponseService.getCodeRespByName(fcmResult.getError());
								 if(insertReponseCode != null) {
									 System.out.println(insertReponseCode.getResponseId());
									 //notif.setResponseId(insertReponseCode.getResponseId());
									 //for (int i = 0; i < array.length(); i++) {
										 NotifResponse nfr = new NotifResponse();
										 nfr.setResponseId(insertReponseCode.getResponseId());
										 JSONObject obj = array.getJSONObject(i);
										 nfr.setTokenId(obj.getInt("tokenId"));
										 nfr.setNotifUuid(resultNotif.getNotifUuid());
										 notifRespService.saveOrUpdate(nfr);	
									 //}
									 
								 } else {
									 CodeResponse codeRep = new CodeResponse();
									 List<CodeResponse> lastId =  codeResponseService.findLastId();
									 codeRep.setResponseId(lastId.get(0).getResponseId()+1);
									 codeRep.setResponseCode(fcmResult.getError());
									 CodeResponse a = codeResponseService.saveOrUpdate(codeRep);
									 
									 System.out.println("++++++++++++++++++++>>>>>>>>>>>>>> "+a.getResponseId());
									 //notif.setResponseId(codeRep.getResponseId());
									 //for (int i = 0; i < array.length(); i++) {
										 NotifResponse nfr = new NotifResponse();
										 nfr.setResponseId(a.getResponseId());
										 JSONObject obj = array.getJSONObject(i);
										 nfr.setTokenId(obj.getInt("tokenId"));
										 nfr.setNotifUuid(resultNotif.getNotifUuid());
										 notifRespService.saveOrUpdate(nfr);	
									 //}
								 }
								 
							}else {
								System.out.println("8888888888888888>>>>>>>>>>>>>> "+resultNotif.getNotifUuid());
								//for (int i = 0; i < array.length(); i++) {
								
									 NotifResponse nfr = new NotifResponse();
									 nfr.setResponseId(0);
									 JSONObject obj = array.getJSONObject(i);
									 
									 nfr.setTokenId(obj.getInt("tokenId"));
									 nfr.setNotifUuid(resultNotif.getNotifUuid());
										System.out.println("8888888888888888>>>>>>>>>>>>>> "+resultNotif.getNotifUuid()+"  "+obj.getInt("tokenId")+"  "+0);
									 notifRespService.saveOrUpdate(nfr); 	
								// }
							}
							
						}
					}
					MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
					response.setMsg("insert token success ");
					response.setStatus(0);
					response.setData(resp);
						
					//return new ResponseEntity(response, HttpStatus.OK);
				
					
					
				
				}
				else 
				{

					JSONObject body = new JSONObject();
					Application app = new Application();
					app.setAppId(payload.getMessage().getAppId());
					msg.setApplication(app);
					msg.setBadge(payload.getMessage().getBadge());
					msg.setBody(payload.getMessage().getBody());
					msg.setBroadcast(true);
					msg.setClickAction(payload.getMessage().getClickAction());
					msg.setMsgType(Message.enum_msg_type.valueOf(payload.getMessage().getMsgType()));
					msg.setPriority(payload.getMessage().getPriority());
					// msg.setSendTime(sendTime);
					msg.setSound(payload.getMessage().getSound());
					msg.setTimeToLive(payload.getMessage().getTimeToLive());
					msg.setTitle(payload.getMessage().getTitle());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					msg.setSendTime(timestamp);
					msg.setQueue(false);
					msg.setMessageStatus("S");
					msg.setMsgId(payload.getMessage().getMsgId());
					
					JSONObject notification = new JSONObject();
					notification.put("title", payload.getMessage().getTitle());
					notification.put("text", payload.getMessage().getBody());
					notification.put("sound", payload.getMessage().getSound());
					notification.put("badge", payload.getMessage().getBadge());
					notification.put("click_action", payload.getMessage().getClickAction());
					notification.put("time_to_live", payload.getMessage().getClickAction());
					body.put("notification", notification);
					messageService.saveOrUpdate(msg);
					//Set<Receiver> recSet = new HashSet<Receiver>();
					// insert receiver
					//List<TokenDevice> listUserRef = tokenDeviceService.getTokenByUserRef(payload.getReceiver().get(0));
					//body.put("to", listUserRef.get(0).getToken());
					//Receiver rec = new Receiver();
					//Set<Receiver> recSet = new HashSet<Receiver>();
					JSONArray array = new JSONArray();
					JSONArray arrayToken = new JSONArray();
					
					List<TokenDevice> listUserRef = null;
					for (String recStr : payload.getReceiver()) {
						listUserRef = tokenDeviceService.findTokenByUserRefAndAppId(recStr, payload.getMessage().getAppId());
						for (int i = 0; i < listUserRef.size(); i++) {
							System.out.println("7777777777>>>>>>>>>>>>>> "+listUserRef.get(i).getTokenId()+"  "+listUserRef.get(i).getToken());
							JSONObject objNotifResp =new JSONObject();
							objNotifResp.put("tokenId", listUserRef.get(i).getTokenId());
							objNotifResp.put("token", listUserRef.get(i).getToken());
							arrayToken.put(listUserRef.get(i).getToken());
							array.put(objNotifResp);
						}
					}
					System.out.println("99999999999999999>>>>>>>>>>>>>> "+array.length());
					body.put("registration_ids", arrayToken);
					System.out.println(body.toString());
					JSONObject data = new JSONObject();
					
					if (payload.getData().size() > 0) {
						CustomField cus = null;
						for (Map.Entry<String, String> entry : payload.getData().entrySet()) {
							cus = new CustomField();
							cus.setMsgId(payload.getMessage().getMsgId());
							cus.setKeyName(entry.getKey());
							cus.setKeyValue(entry.getValue());
							data.put(entry.getKey(), entry.getValue());
							//cusSet.add(cus);
							customService.saveOrUpdate(cus);
							System.out.println(">>>>>>>>>>>> "+entry.getKey()+" "+entry.getValue());
						}
					}
					
					Notif notif = new Notif();
					notif.setNotifStatus("N");
					notif.setRequestBody(body.toString());
					notif.setMsgId(payload.getMessage().getMsgId());
					notif.setRequestTime(new Timestamp(System.currentTimeMillis()));
					HttpEntity<String> request = new HttpEntity<>(body.toString());
					CompletableFuture<FcmResponse> pushNotification = send(request);
					CompletableFuture.allOf(pushNotification).join();
					FcmResponse resp = pushNotification.get();
					System.out.println(" >>> " + resp);
					JSONObject res = new JSONObject(resp);
					
					notif.setResponseBody(res.toString());
					//กรณีที่ success ทั้งหมด -> fcm ResponseCode > 0 ทุกอัน 
					if (res.has("success") && res.getInt("success") > 0 &&  res.getInt("failure") == 0) {
						notif.setNotifStatus("S");
						notif.setResponseTime(new Timestamp(System.currentTimeMillis()));
						Receiver rec = null;
						for (String recStr : payload.getReceiver()) {
							rec = new Receiver();
							rec.setMsgId(payload.getMessage().getMsgId());
							rec.setUserRef(recStr);
							rec.setNotifStatus("S");
							recService.saveOrUpdate(rec);
						}
						Notif resultNotif = notifService.saveOrUpdate(notif);
						List<FcmResult> fcmListResp = resp.getResults();
						
						for (int i = 0; i < fcmListResp.size(); i++) {
							 NotifResponse nfr = new NotifResponse();
							 nfr.setResponseId(0);
							 JSONObject obj = array.getJSONObject(i);
							 nfr.setTokenId(obj.getInt("tokenId"));
							 nfr.setNotifUuid(resultNotif.getNotifUuid());
							 notifRespService.saveOrUpdate(nfr);	
						}
					
					}
					//กรณีที่ success แค่บ้างรายการ 
					else 
					{
						notif.setNotifStatus("F");
						notif.setResponseTime(new Timestamp(System.currentTimeMillis()));
						Receiver rec = null;
						for (String recStr : payload.getReceiver()) {
							rec = new Receiver();
							rec.setMsgId(payload.getMessage().getMsgId());
							rec.setUserRef(recStr);
							rec.setNotifStatus("S");
							recService.saveOrUpdate(rec);
						}
						Notif resultNotif = notifService.saveOrUpdate(notif);
						List<FcmResult> fcmListResp = resp.getResults();
						for (int i = 0; i < fcmListResp.size(); i++) {
							FcmResult fcmResult = fcmListResp.get(i);
						//for (FcmResult fcmResult : fcmListResp) {
							System.out.println(fcmResult.getError());
							System.out.println(fcmResult.getMessage_id());
							if(fcmResult.getError() != null) {
								 CodeResponse insertReponseCode = codeResponseService.getCodeRespByName(fcmResult.getError());
								 if(insertReponseCode != null) {
									 System.out.println(insertReponseCode.getResponseId());
									 //notif.setResponseId(insertReponseCode.getResponseId());
									 //for (int i = 0; i < array.length(); i++) {
									 NotifResponse nfr = new NotifResponse();
									 nfr.setResponseId(insertReponseCode.getResponseId());
									 JSONObject obj = array.getJSONObject(i);
									 nfr.setTokenId(obj.getInt("tokenId"));
									 nfr.setNotifUuid(resultNotif.getNotifUuid());
									 notifRespService.saveOrUpdate(nfr);	
									 //}
									 
								 } else {
									 CodeResponse codeRep = new CodeResponse();
									 List<CodeResponse> lastId =  codeResponseService.findLastId();
									 codeRep.setResponseId(lastId.get(0).getResponseId()+1);
									 codeRep.setResponseCode(fcmResult.getError());
									 CodeResponse a = codeResponseService.saveOrUpdate(codeRep);
									 
									 System.out.println("++++++++++++++++++++>>>>>>>>>>>>>> "+a.getResponseId());
									 //notif.setResponseId(codeRep.getResponseId());
									 //for (int i = 0; i < array.length(); i++) {
									 NotifResponse nfr = new NotifResponse();
									 nfr.setResponseId(a.getResponseId());
									 JSONObject obj = array.getJSONObject(i);
									 nfr.setTokenId(obj.getInt("tokenId"));
									 nfr.setNotifUuid(resultNotif.getNotifUuid());
									 notifRespService.saveOrUpdate(nfr);	
									 //}
								 }
								 
							}else {
								System.out.println("8888888888888888>>>>>>>>>>>>>> "+resultNotif.getNotifUuid());
							//for (int i = 0; i < array.length(); i++) {
							
								NotifResponse nfr = new NotifResponse();
								nfr.setResponseId(0);
								JSONObject obj = array.getJSONObject(i);
								 
								nfr.setTokenId(obj.getInt("tokenId"));
								nfr.setNotifUuid(resultNotif.getNotifUuid());
									System.out.println("8888888888888888>>>>>>>>>>>>>> "+resultNotif.getNotifUuid()+"  "+obj.getInt("tokenId")+"  "+0);
								notifRespService.saveOrUpdate(nfr); 	
							// }
							}
							
						}
					}
					
					

					MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
					response.setMsg("insert token success ");
					response.setStatus(0);
					response.setData(resp);
						
					//return new ResponseEntity(response, HttpStatus.OK);
					
				
				
				}

				
			} else {
				MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
				response.setMsg("no data");
				response.setStatus(1);
				//return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			MessageResponse response = new MessageResponse();
			response.setMsg(ex.getMessage());
			response.setStatus(-1);
			//return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	
	}
	
	
	
	
}
