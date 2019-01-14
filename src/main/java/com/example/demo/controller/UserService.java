package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.ApplicationConfigReader;
import com.example.config.ApplicationConstant;
import com.example.demo.MessageSender;
import com.example.demo.model.Application;
import com.example.demo.model.CodeResponse;
import com.example.demo.model.CustomField;
import com.example.demo.model.DeviceDetail;
import com.example.demo.model.FcmResponse;
import com.example.demo.model.FcmResult;
import com.example.demo.model.Message;
import com.example.demo.model.MessageResponse;
import com.example.demo.model.Notif;
import com.example.demo.model.NotifResponse;
import com.example.demo.model.Payload;
import com.example.demo.model.Receiver;
import com.example.demo.model.TokenDevice;
import com.example.demo.repo.NotifResponseRepository;
import com.example.demo.service.AndroidPushNotificationsService;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.CodeResponseService;
import com.example.demo.service.CustomService;
import com.example.demo.service.MessageService;
import com.example.demo.service.NotifResponseService;
import com.example.demo.service.NotifService;
import com.example.demo.service.ReceiverService;
import com.example.demo.service.TokenDeviceService;

@RestController
@RequestMapping(path = "/userservice")
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	private final RabbitTemplate rabbitTemplate;
	private ApplicationConfigReader applicationConfig;
	private MessageSender messageSender;

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
	
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;

	public ApplicationConfigReader getApplicationConfig() {
		return applicationConfig;
	}

	@Autowired
	public void setApplicationConfig(ApplicationConfigReader applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	@Autowired
	public UserService(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	@Autowired
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	/*
	 * @RequestMapping(path = "/add", method = RequestMethod.POST, produces =
	 * MediaType.TEXT_PLAIN_VALUE) public ResponseEntity<?> sendMessage(@RequestBody
	 * UserDetails user) { String exchange =
	 * getApplicationConfig().getApp1Exchange(); String routingKey =
	 * getApplicationConfig().getApp1RoutingKey(); Sending to Message Queue try {
	 * messageSender.sendMessage(rabbitTemplate, exchange, routingKey, user); return
	 * new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK); }
	 * catch (Exception ex) { log.
	 * error("Exception occurred while sending message to the queue. Exception= {}",
	 * ex); return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR,
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */

	@PostMapping("/add")
	public ResponseEntity<?> sendMessage(@RequestBody TokenDevice token) {
		String exchange = getApplicationConfig().getApp1Exchange();
		String routingKey = getApplicationConfig().getApp1RoutingKey();
		log.info(">>>>>>>>>>>>>>" + token.toString());
		/* Sending to Message Queue */
		try {
			messageSender.sendMessage(rabbitTemplate, exchange, routingKey, token);
			return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
			return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addapp")
	public ResponseEntity<?> sendMessage(@RequestBody Application app) {

		/* Sending to Message Queue */
		try {
			appService.saveOrUpdate(app);
			MessageResponse<?> response = new MessageResponse();
			response.setMsg("insert success");
			response.setStatus(0);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception ex) {
			MessageResponse<?> response = new MessageResponse();
			response.setMsg("insert error " + ex.getMessage());
			response.setStatus(-1);
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getapp")
	public ResponseEntity<List<?>> getAllApplication() {

		List<Application> list = appService.getAllApp();
		for (Application application : list) {
			Set<TokenDevice> setofdevice = application.getToken();
			for (TokenDevice setdata : setofdevice) {
				System.out.println(setdata.getDeviceDet());
			}
		}

		/*
		 * List<TokenDevice> token = tokenDeviceService.getAllToken(); for (TokenDevice
		 * tokenDevice : token) { Application app = tokenDevice.getApplication();
		 * System.out.println(app.getAppName()); }
		 */
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/addtoken")
	public ResponseEntity<?> addToken(@RequestBody DeviceDetail device) {

		/* Sending to Message Queue */
		System.out.println(device.toString());
		try {

			TokenDevice deviceData = tokenDeviceService.findTokenByDeviceDet(device.getPerid(), device.getAppId(),
					device.getUuid());
			if (deviceData != null && deviceData.getToken() != null && !"".equals(deviceData.getToken())) {
				if (!deviceData.getToken().equals(device.getToken())) {
					int updateData = tokenDeviceService.updateTokenByDeviceDet(device.getToken(), device.getPerid(),
							device.getAppId(), device.getUuid());
					MessageResponse response = new MessageResponse();
					response.setMsg("token update " + updateData);
					response.setStatus(0);
					return new ResponseEntity<MessageResponse>(response, HttpStatus.OK);
				} else {
					MessageResponse response = new MessageResponse();
					response.setMsg("token existing");
					response.setStatus(0);
					return new ResponseEntity<MessageResponse>(response, HttpStatus.OK);
				}

			} else {
				log.error(">>>>>" + device.getAppId());
				TokenDevice tdevice = new TokenDevice();
				Application appmodel = new Application();
				appmodel.setAppId(device.getAppId());
				tdevice.setApplication(appmodel);
				tdevice.setToken(device.getToken());
				tdevice.setOsType(TokenDevice.enum_os.valueOf(device.getPlatform().toUpperCase()));
				tdevice.setUserRef(device.getPerid());
				tdevice.setDeviceDet(device.getModel() == null ? "" : device.getModel());
				tdevice.setDeviceUuid(device.getUuid());
				// tdevice.setLastLogin("");
				tokenDeviceService.saveOrUpdate(tdevice);
				MessageResponse response = new MessageResponse();
				response.setMsg("insert token success ");
				response.setStatus(0);
				return new ResponseEntity<MessageResponse>(response, HttpStatus.OK);
			}

		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			MessageResponse response = new MessageResponse();
			response.setMsg(ex.getMessage());
			response.setStatus(-1);
			return new ResponseEntity<MessageResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addmsg")
	public ResponseEntity<?> addMsg(@RequestBody Message message) {

		/* Sending to Message Queue */
		System.out.println(message.toString());
		try {
			messageService.saveOrUpdate(message);
			return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/qmsg")
	public ResponseEntity<?> qMsg(@RequestBody Message message) {

		/* Sending to Message Queue */
		// System.out.println(message.toString());
		try {
			/*
			 * CustomField cust = new CustomField(); cust.setCustomFieldId(customFieldId);
			 * msg.setCustomField(customField);
			 */
			/*
			 * Application app = new Application();
			 * app.setAppId(message.getMessage().getApplication().getAppId());
			 * msg.setApplication(application);
			 */
			/*
			 * Application app = new Application(); app.setAppId(1);
			 * message.setApplication(app);
			 */
//			Set<CustomField> a = message.getCustomField();
//			for (CustomField customField : a) {
//				System.out.println(customField.getCustomFieldId().getMsgId()+"
//				 "+customField.getCustomFieldId().getKeyName()+" "+customField.getKeyValue());
//			}

			// messageService.saveOrUpdate(message);
			// messageService.saveOrUpdate(message.getMess1age());
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/q")
	public ResponseEntity<?> qgMsg(@RequestBody Message cus) {

		/* Sending to Message Queue */
		System.out.println(cus.toString());
		try {
			// System.out.println(cus.getApplication().getAppId());
			// cus.getApplication().setAppId(1);
			/*
			 * CustomFieldId id = new CustomFieldId(); //id.setMsgId(2);;
			 * id.setKeyName("a"); CustomField e = new CustomField();
			 * 
			 * e.setCustomFieldId(id); e.setKeyValue("b"); Set<CustomField> cus = new
			 * HashSet<CustomField>(); cus.add(e); message.setCustomField(cus);
			 */
	messageService.saveOrUpdate(cus);
			return new ResponseEntity<>(cus, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
/*
	@PostMapping("/addmsgall")
	public ResponseEntity<?> addMsg(@RequestBody Payload message) {

		 Sending to Message Queue 
		System.out.println(message.toString());
		try {
		
			Integer id = messageService.saveOrUpdate(message.getMessage());
			System.out.println(id);
			Receiver receiverBody = null;
			ReceiverId receiverId = null;
			for (int i = 0; i < message.getReceiver().size(); i++) {
				receiverBody = new Receiver();
				receiverId = new ReceiverId();
				// receiverId.setMsgId(id);
				// receiverId.setUserRef(message.getUserRef().get(i));
				// receiverBody.setReceiverId(receiverId);
				recService.saveOrUpdate(receiverBody);
			}
			CustomFieldId g = null;
			CustomField h = null;
			for (Map.Entry<String, String> entry : message.getData().entrySet()) {
				g = new CustomFieldId();
				h = new CustomField();
				// g.setMsgId(id);
				// g.setKeyName(entry.getKey());
				// h.setCustomFieldId(g);
				h.setKeyValue(entry.getValue());
				System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
				customService.saveOrUpdate(h);
			}
			return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : " + ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}*/

	@PostMapping("/push")
	public ResponseEntity<?> pushNotification(@RequestBody Payload payload) {

		/* Sending to Message Queue */
		System.out.println(payload.toString());
		Message msg = new Message();
		try {
			/*
			 * java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(0);
			 * message.getMessage().setSendTime(currentTimestamp);
			 */
			
			
			if (payload.getReceiver() != null && payload.getReceiver().size() > 0) {
				JSONObject body = new JSONObject();
				Application app = new Application();
				app.setAppId(payload.getMessage().getAppId());
				msg.setApplication(app);
				msg.setBadge(payload.getMessage().getBadge());
				msg.setBody(payload.getMessage().getBody());
				msg.setBroadcast(payload.getMessage().isBroadcast());
				msg.setClickAction(payload.getMessage().getClickAction());
				msg.setMsgType(Message.enum_msg_type.valueOf(payload.getMessage().getMsgType()));
				msg.setPriority(payload.getMessage().getPriority());
				// msg.setSendTime(sendTime);
				msg.setSound(payload.getMessage().getSound());
				msg.setTimeToLive(payload.getMessage().getTimeToLive());
				msg.setTitle(payload.getMessage().getTitle());
				Integer id = messageService.saveOrUpdate(msg);
				
				Set<Receiver> recSet = new HashSet<Receiver>();
				List<TokenDevice> listUserRef = null;
				if (payload.getReceiver().size() == 1) {

					Receiver rec = null;
					for (String recStr : payload.getReceiver()) {
						
						rec = new Receiver();
						rec.setMsgId(id);
						rec.setUserRef(recStr);
						rec.setNotifStatus("N");

						listUserRef = tokenDeviceService.getTokenByUserRef(recStr);
						body.put("to", listUserRef.get(0).getToken());
						recSet.add(rec);
						recService.saveOrUpdate(rec);
					}
				}
				//msg.setReceiver(recSet);
				
				
				System.out.println(">>>>>>>>>>>> "+payload.getData().size());
				Set<CustomField> cusSet = new HashSet<>();
				JSONObject data = new JSONObject();
				if (payload.getData().size() > 0) {
					CustomField cus = null;

					for (Map.Entry<String, String> entry : payload.getData().entrySet()) {
						cus = new CustomField();
						cus.setMsgId(id);
						cus.setKeyName(entry.getKey());
						cus.setKeyValue(entry.getValue());
						data.put(entry.getKey(), entry.getValue());
						cusSet.add(cus);
						customService.saveOrUpdate(cus);
						System.out.println(">>>>>>>>>>>> "+entry.getKey()+" "+entry.getValue());
					}
					
				}
				
				//msg.setCustomField(cusSet);
				//body.put("data", data);

				
				System.out.println(id);

				JSONObject notification = new JSONObject();
				notification.put("title", payload.getMessage().getTitle());
				notification.put("text", payload.getMessage().getBody());
				notification.put("sound", payload.getMessage().getSound());
				notification.put("badge", payload.getMessage().getBadge());
				notification.put("click_action", payload.getMessage().getClickAction());
				// notification.put("icon", message.getMessage().getPriority());

				body.put("notification", notification);

				System.out.println(body.toString());
				Notif notif = new Notif();
//notif.setMsgId(id);
				notif.setNotifStatus("N");
				notif.setRequestBody(body.toString());
				//notif.setTokenId(listUserRef.get(0).getTokenId());
				notif.setMsgId(id);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				notif.setRequestTime(timestamp);
				notifService.saveOrUpdate(notif);

				HttpEntity<String> request = new HttpEntity<>(body.toString());

				CompletableFuture<FcmResponse> pushNotification = androidPushNotificationsService.send(request);
				CompletableFuture.allOf(pushNotification).join();
				FcmResponse resp = pushNotification.get();
				System.out.println(" >>> " + resp);
				JSONObject res = new JSONObject(resp);

				
				notif.setResponseBody(res.toString());
				Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
				if (res.has("success") && res.getInt("success") > 0) {
					notif.setNotifStatus("S");
					notif.setResponseTime(timestamp1);
				}
				else {
					notif.setNotifStatus("F");
					notif.setResponseTime(timestamp1);
				}
				
				// notif.setTokenId(listUserRef.get(0).getTokenId());
				notifService.saveOrUpdate(notif);

				MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
				response.setMsg("insert token success ");
				response.setStatus(0);
				response.setData(resp);
					
				return new ResponseEntity(response, HttpStatus.CREATED);
		
			} else {
				MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
				response.setMsg("no data");
				response.setStatus(1);
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			MessageResponse response = new MessageResponse();
			response.setMsg(ex.getMessage());
			response.setStatus(-1);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping("/push2")
	public ResponseEntity<?> pushNotification2(@RequestBody Payload payload) {

		/* Sending to Message Queue */
		System.out.println(payload.toString());
		Message msg = new Message();
		try {
		
			
			if (payload.getReceiver() != null && payload.getReceiver().size() > 0) {
				
				if(payload.getReceiver().size() == 1) {
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
					
					JSONObject notification = new JSONObject();
					notification.put("title", payload.getMessage().getTitle());
					notification.put("text", payload.getMessage().getBody());
					notification.put("sound", payload.getMessage().getSound());
					notification.put("badge", payload.getMessage().getBadge());
					notification.put("click_action", payload.getMessage().getClickAction());
					notification.put("time_to_live", payload.getMessage().getClickAction());
					body.put("notification", notification);
					Integer id = messageService.saveOrUpdate(msg);
					
					
					JSONObject data = new JSONObject();
					if (payload.getData().size() > 0) {
						CustomField cus = null;
						for (Map.Entry<String, String> entry : payload.getData().entrySet()) {
							cus = new CustomField();
							cus.setMsgId(id);
							cus.setKeyName(entry.getKey());
							cus.setKeyValue(entry.getValue());
							data.put(entry.getKey(), entry.getValue());
							//cusSet.add(cus);
							customService.saveOrUpdate(cus);
							System.out.println(">>>>>>>>>>>> "+entry.getKey()+" "+entry.getValue());
						}
					}
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
					
					/*rec.setMsgId(id);
					rec.setUserRef(payload.getReceiver().get(0));
					rec.setNotifStatus("N");
					recService.saveOrUpdate(rec);*/
					
					//Set<CustomField> cusSet = new HashSet<>();
					
					
					Notif notif = new Notif();
					notif.setNotifStatus("N");
					notif.setRequestBody(body.toString());
					notif.setMsgId(id);
					notif.setRequestTime(new Timestamp(System.currentTimeMillis()));
					HttpEntity<String> request = new HttpEntity<>(body.toString());
					CompletableFuture<FcmResponse> pushNotification = androidPushNotificationsService.send(request);
					CompletableFuture.allOf(pushNotification).join();
					FcmResponse resp = pushNotification.get();
					System.out.println(" >>> " + resp);
					JSONObject res = new JSONObject(resp);
					notif.setResponseBody(res.toString());
					Receiver rec = new Receiver();
					if (res.has("success") && res.getInt("success") > 0 && res.getInt("failure") == 0) {
						notif.setNotifStatus("S");
						notif.setResponseTime(new Timestamp(System.currentTimeMillis()));
						rec.setMsgId(id);
						rec.setUserRef(payload.getReceiver().get(0));
						rec.setNotifStatus("S");
						recService.saveOrUpdate(rec);
						
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
						rec.setMsgId(id);
						rec.setUserRef(payload.getReceiver().get(0));
						rec.setNotifStatus("F");
						recService.saveOrUpdate(rec);
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
						
					return new ResponseEntity(response, HttpStatus.OK);
				
					
					
				
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
					
					JSONObject notification = new JSONObject();
					notification.put("title", payload.getMessage().getTitle());
					notification.put("text", payload.getMessage().getBody());
					notification.put("sound", payload.getMessage().getSound());
					notification.put("badge", payload.getMessage().getBadge());
					notification.put("click_action", payload.getMessage().getClickAction());
					notification.put("time_to_live", payload.getMessage().getClickAction());
					body.put("notification", notification);
					Integer id = messageService.saveOrUpdate(msg);
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
							cus.setMsgId(id);
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
					notif.setMsgId(id);
					notif.setRequestTime(new Timestamp(System.currentTimeMillis()));
					HttpEntity<String> request = new HttpEntity<>(body.toString());
					CompletableFuture<FcmResponse> pushNotification = androidPushNotificationsService.send(request);
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
							rec.setMsgId(id);
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
							rec.setMsgId(id);
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
						
					return new ResponseEntity(response, HttpStatus.OK);
					
				
				
				}

				
			} else {
				MessageResponse<FcmResponse> response = new MessageResponse<FcmResponse>();
				response.setMsg("no data");
				response.setStatus(1);
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			MessageResponse response = new MessageResponse();
			response.setMsg(ex.getMessage());
			response.setStatus(-1);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	}
}
