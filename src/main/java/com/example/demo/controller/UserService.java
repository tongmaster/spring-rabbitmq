package com.example.demo.controller;

import java.util.List;
import java.util.Map;
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
import com.example.demo.model.CustomField;
import com.example.demo.model.CustomFieldId;
import com.example.demo.model.Message;
import com.example.demo.model.Notif;
import com.example.demo.model.Payload;
import com.example.demo.model.Receiver;
import com.example.demo.model.ReceiverId;
import com.example.demo.model.TokenDevice;
import com.example.demo.service.AndroidPushNotificationsService;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.CustomService;
import com.example.demo.service.MessageService;
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
	@RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> sendMessage(@RequestBody UserDetails user) {
		String exchange = getApplicationConfig().getApp1Exchange();
		String routingKey = getApplicationConfig().getApp1RoutingKey();
		 Sending to Message Queue 
		try {
			messageSender.sendMessage(rabbitTemplate, exchange, routingKey, user);
			return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
			return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/
	
	
	@PostMapping("/add")
	public ResponseEntity<?> sendMessage(@RequestBody TokenDevice token) {
		String exchange = getApplicationConfig().getApp1Exchange();
		String routingKey = getApplicationConfig().getApp1RoutingKey();
		log.info(">>>>>>>>>>>>>>" +token.toString());
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
			return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/getapp")
	public ResponseEntity<List<Application>> getAllApplication() {
		// androidPushNotificationsService.send(entity)
		// System.out.println("data token >> "+name+"
		// "+String.format(placeholderConfig.getProperty("test")));
		List<Application> list = appService.getAllApp();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	
	@PostMapping("/addtoken")
	public ResponseEntity<?> addToken(@RequestBody TokenDevice token) {
		
		/* Sending to Message Queue */
		System.out.println(token.toString());
		try {
			tokenDeviceService.saveOrUpdate(token);
			return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity("error : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/addmsgall")
	public ResponseEntity<?> addMsg(@RequestBody Payload message) {
		
		/* Sending to Message Queue */
		System.out.println(message.toString());
		try {
	/*		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(0);
			message.getMessage().setSendTime(currentTimestamp);*/
				Integer id = messageService.saveOrUpdate(message.getMessage());
				System.out.println(id);
				Receiver receiverBody = null;
				ReceiverId receiverId = null;
				for (int i = 0; i < message.getUserRef().size(); i++) {
					receiverBody = new Receiver();
					receiverId = new ReceiverId();
					receiverId.setMsgId(id);
					receiverId.setUserRef(message.getUserRef().get(i));
					receiverBody.setReceiverId(receiverId);
					recService.saveOrUpdate(receiverBody);
				}
				CustomFieldId g = null;
				CustomField h = null;
				for (Map.Entry<String, String> entry : message.getData().entrySet()) {
					g =  new CustomFieldId();
					h =  new CustomField();
					g.setMsgId(id);
					g.setKeyName(entry.getKey());
					h.setCustomFieldId(g);
					h.setKeyValue(entry.getValue());
					System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
					customService.saveOrUpdate(h);
				}
			return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@PostMapping("/push")
	public ResponseEntity<?> pushNotification(@RequestBody Payload message) {
		
		/* Sending to Message Queue */
		System.out.println(message.toString());
		try {
	/*		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(0);
			message.getMessage().setSendTime(currentTimestamp);*/
			if(message.getUserRef() != null && message.getUserRef().size() > 0) {
				JSONObject body = new JSONObject();
				Integer id = messageService.saveOrUpdate(message.getMessage());
				System.out.println(id);
				Receiver receiverBody = null;
				ReceiverId receiverId = null;
				
				if(message.getUserRef().size() == 1) {
					List<TokenDevice> listUserRef = tokenDeviceService.getTokenByUserRef((String)message.getUserRef().get(0));
					
					
					receiverBody = new Receiver();
					receiverId = new ReceiverId();
					receiverId.setMsgId(id);
					receiverId.setUserRef(message.getUserRef().get(0));
					receiverBody.setReceiverId(receiverId);
					recService.saveOrUpdate(receiverBody);
					body.put("to", listUserRef.get(0).getToken());
				}else {
					JSONArray jarray = new JSONArray();
					for (int i = 0; i < message.getUserRef().size(); i++) {
						receiverBody = new Receiver();
						receiverId = new ReceiverId();
						receiverId.setMsgId(id);
						receiverId.setUserRef(message.getUserRef().get(i));
						receiverBody.setReceiverId(receiverId);
						jarray.put(message.getUserRef().get(i));
						recService.saveOrUpdate(receiverBody);
					}
					body.put("registration_ids",jarray);
				}
				if(message.getData() != null && message.getData().size() > 0) {
					CustomFieldId g = null;
					CustomField h = null;
					JSONObject data = new JSONObject();
					for (Map.Entry<String, String> entry : message.getData().entrySet()) {
						g =  new CustomFieldId();
						h =  new CustomField();
						g.setMsgId(id);
						g.setKeyName(entry.getKey());
						h.setCustomFieldId(g);
						h.setKeyValue(entry.getValue());
						System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
						customService.saveOrUpdate(h);
						data.put(entry.getKey(), entry.getValue());
					}
					body.put("data", data);
				}
				
				
				System.out.println(message.getUserRef().get(0));
				
				/*
				if (message.getMessage().getPriority() != null && !"".equals(message.getMessage().getPriority()))
					body.put("priority", message.getMessage().getPriority());*/

				JSONObject notification = new JSONObject();
				notification.put("title", message.getMessage().getTitle());
				notification.put("text", message.getMessage().getBody());
				notification.put("sound", message.getMessage().getSound());
				notification.put("badge", message.getMessage().getBadge());
				notification.put("click_action", message.getMessage().getClickAction());
				//notification.put("icon", message.getMessage().getPriority());

			
			

				body.put("notification", notification);
				
				System.out.println(body.toString());
				Notif notif = new Notif();
				notif.setMsgId(id);
				notif.setQueueStatus("N");
				notif.setRequestBody(body.toString());
				notif.setSendStatus("N");
				//notif.setTokenId(listUserRef.get(0).getTokenId());
				notifService.saveOrUpdate(notif);
				
				
				HttpEntity<String> request = new HttpEntity<>(body.toString());

				CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
				CompletableFuture.allOf(pushNotification).join();
				String resp = pushNotification.get();
				System.out.println(" >>> "+resp);
				JSONObject res = new JSONObject(resp);
				
				/*Notif notifRes = new Notif();
				notifRes.setMsgId(id);
				notifRes.setQueueStatus("S");
				notifRes.setRequestBody(resp);*/
				notif.setResponseBody(resp);
				if(res.has("success") &&  res.getInt("success") > 0)
					notif.setSendStatus("S");
				else
					notif.setSendStatus("F");
				//notif.setTokenId(listUserRef.get(0).getTokenId());
				notifService.saveOrUpdate(notif);
					
				return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity("not data", HttpStatus.BAD_REQUEST);
			}	
				
			
		} catch (Exception ex) {
			log.error("Exception occurred while save application ", ex);
			return new ResponseEntity("error : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
