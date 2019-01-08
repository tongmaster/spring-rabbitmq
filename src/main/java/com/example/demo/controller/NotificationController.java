package com.example.demo.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Application;
import com.example.demo.model.FcmResponse;
import com.example.demo.model.Message;
import com.example.demo.model.PayloadLog;
import com.example.demo.model.TokenDevice;
import com.example.demo.service.AndroidPushNotificationsService;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.PayloadService;
import com.example.demo.service.TokenDeviceService;
import com.example.demo.service.UserDeviceService;

@CrossOrigin(allowCredentials = "true")
@RestController
//@RequestMapping("/demo")
public class NotificationController {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	public final static String AUTH_KEY_FCM = "AAAA0iKrgYY:APA91bGP0yh1yEhyhkNH6e_dB7o22iuFAv9-YE6_x1P_UzJtf9J-HzYQJm21cgfGpaYQswwRP8HMBOWhEf-k9EoiLdgLnbC8UKYI9roDL-RlEgYdug5OCtBFBieQnoXHnn5HDzid6KbM";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	@Autowired
	PayloadService payloadService;

	@Autowired
	TokenDeviceService tokenDeviceService;

	@Autowired
	ApplicationService appService;

	@Autowired
	UserDeviceService udService;
	/*
	 * @Autowired private CustomPropertyPlaceholderConfigurer placeholderConfig;
	 */

	/*
	 * @Value("${fcm.authKey}") private static String authKey;
	 */

	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;

	@PostMapping("/getdevicetoken")
	// @CrossOrigin(origins = "http://192.168.0.25:8080")
	// @GetMapping("/greeting")
	public ResponseEntity<TokenDevice> greeting(@RequestBody TokenDevice tokendevice) {
		System.out.println("data token >>  " + tokendevice.toString());
		// androidPushNotificationsService.send(entity)
		// System.out.println("data token >> "+name+"
		// "+String.format(placeholderConfig.getProperty("test")));
		tokenDeviceService.saveOrUpdate(tokendevice);
		return new ResponseEntity<>(tokendevice, HttpStatus.CREATED);
	}

	@GetMapping("/getall")
	// @CrossOrigin(origins = "http://192.168.0.25:8080")
	// @GetMapping("/greeting")
	public ResponseEntity<List<TokenDevice>> getAll() {
		// androidPushNotificationsService.send(entity)
		// System.out.println("data token >> "+name+"
		// "+String.format(placeholderConfig.getProperty("test")));
		List<TokenDevice> list = tokenDeviceService.getAllToken();
		return new ResponseEntity<>(list, HttpStatus.CREATED);
	}
	/*
	 * @Bean public static PropertySourcesPlaceholderConfigurer properties(){
	 * PropertySourcesPlaceholderConfigurer pspc= new
	 * PropertySourcesPlaceholderConfigurer(); Resource[] resources = new
	 * ClassPathResource[]{ new ClassPathResource( "foo.properties" ) };
	 * pspc.setLocations( resources ); pspc.setIgnoreUnresolvablePlaceholders( true
	 * ); return pspc; }
	 */

	/*
	 * public static void pushFCMNotification(String userDeviceIdKey) throws
	 * Exception{
	 * 
	 * //String authKey = AUTH_KEY_FCM; // You FCM AUTH key String FMCurl =
	 * API_URL_FCM;
	 * 
	 * URL url = new URL(FMCurl); HttpURLConnection conn = (HttpURLConnection)
	 * url.openConnection();
	 * 
	 * conn.setUseCaches(false); conn.setDoInput(true); conn.setDoOutput(true);
	 * 
	 * conn.setRequestMethod("POST");
	 * conn.setRequestProperty("Authorization","key="+authKey);
	 * conn.setRequestProperty("Content-Type","application/json");
	 * 
	 * JSONObject json = new JSONObject(); json.put("to",userDeviceIdKey.trim());
	 * JSONObject info = new JSONObject(); info.put("title", "Notificatoin Title");
	 * // Notification title info.put("body", "Hello Test notification"); //
	 * Notification body json.put("notification", info);
	 * 
	 * OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	 * wr.write(json.toString()); wr.flush(); conn.getInputStream(); }
	 */

	@GetMapping("/send")
	public ResponseEntity<?> send() throws JSONException {

		JSONObject body = new JSONObject();

		body.put("to","eLc1pMQuvVg:APA91bGQTG-AJPCFwiS6bv3N-1IQBg1WXsf4P33oeLFHN_0lqkFYEeeZFSxKVbXYmFShVauoKCteTrgUcmtOcSptgSvI5WnucBJdJ9W4oa1gfF3ls_Lg5GH_DzgwnYBZU68G5fr2jtks");
		// body.put("to",
		// "dkZGLDv9T6Q:APA91bHz6xRmSN5jCv1YXOoHZo0Pu-Kw7VS3xohaNKRRbLKRhZFbuZM0pxtmmdQwIsGB0pOZwxQhth2j_9nEfXm9LHtb7jdB3IEMXwzNcFjGbZV-wC1YMZgdNlRq7JWfB73I_opoVjVe");
		body.put("priority", "high");

		JSONObject notification = new JSONObject();
		notification.put("title", "JSA Notification");
		notification.put("body", "Happy Message!");

		/*
		 * JSONObject data = new JSONObject(); data.put("Key-1", "JSA Data 1");
		 * data.put("Key-2", "JSA Data 2");
		 */
		body.put("notification", notification);
		// body.put("data", data);

		/*
		 * { "notification": { "title": "JSA Notification", "body": "Happy Message!" },
		 * "data": { "Key-1": "JSA Data 1", "Key-2": "JSA Data 2" }, "to":
		 * "/topics/JavaSampleApproach", "priority": "high" } { "to" :
		 * "eLc1pMQuvVg:APA91bGQTG-AJPCFwiS6bv3N-1IQBg1WXsf4P33oeLFHN_0lqkFYEeeZFSxKVbXYmFShVauoKCteTrgUcmtOcSptgSvI5WnucBJdJ9W4oa1gfF3ls_Lg5GH_DzgwnYBZU68G5fr2jtks",
		 * 
		 * "notification" :{ "title":"Notification title", "body":"Notification body",
		 * "sound":"default", "click_action":"FCM_PLUGIN_ACTIVITY",
		 * "icon":"fcm_push_icon" } }
		 */

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<FcmResponse> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			FcmResponse firebaseResponse = pushNotification.get();

			return new ResponseEntity<FcmResponse>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
		// return new ResponseEntity<>("Push Notification OK", HttpStatus.OK);
	}

	@PostMapping("/send")
	public ResponseEntity<String> send(@RequestBody Message message) throws JSONException {

		
		/*System.out.println(payload.getTo() + "  " + payload.getPriority());

		JSONObject body = new JSONObject();

		body.put("to", payload.getTo());

		if (payload.getPriority() != null && !"".equals(payload.getPriority()))
			body.put("priority", payload.getPriority());

		JSONObject notification = new JSONObject();
		notification.put("title", payload.getNotification().getTitle());
		notification.put("text", payload.getNotification().getText());
		notification.put("sound", payload.getNotification().getSound());
		notification.put("badge", payload.getNotification().getBadge());
		notification.put("click_action", payload.getNotification().getClickAction());
		notification.put("icon", payload.getNotification().getIcon());

		JSONObject data = new JSONObject();
		data.put("param1", payload.getData().getParam1());
		data.put("param2", payload.getData().getParam2());

		body.put("notification", notification);
		body.put("data", data);
		System.out.println(body.toString());

		HttpEntity<String> request = new HttpEntity<>(body.toString());

		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();

		try {
			String firebaseResponse = pushNotification.get();

			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		*/
		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
		// return new ResponseEntity<>("data ok", HttpStatus.OK);
	}

	@PostMapping("/savepayload")
	public ResponseEntity<String> savePayLoad(@RequestBody PayloadLog payload) throws JSONException {
		payloadService.saveOrUpdate(payload);
		return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		// return new ResponseEntity<>("Push Notification ERROR!",
		// HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/saveapp")
	public ResponseEntity<String> saveApp(@RequestBody Application app) throws JSONException {
		appService.saveOrUpdate(app);
		return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		// return new ResponseEntity<>("Push Notification ERROR!",
		// HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/saveuserdevice")
	public ResponseEntity<String> saveUserDevice(@RequestBody TokenDevice token) throws JSONException {
		tokenDeviceService.saveOrUpdate(token);
		return new ResponseEntity<>("insert ok", HttpStatus.CREATED);
		// return new ResponseEntity<>("Push Notification ERROR!",
		// HttpStatus.BAD_REQUEST);
	}

	

}
