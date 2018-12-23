import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FakeLiveBusiness {
	
	private int currIndex = 0;
	private final int[] counts = { 1, 1, 2, 0 };
	private final int[] entering = { 1, 0, 1, 0 };
	private final int[] exiting = { 0, 0, 0, 2 };
	
	private long myId;
	private int UPDATES_INTERVAL_SEC;
	
	private Timer timer;
	
	public void start() {
		getId();
	}
	
	private void getId() {
		 try {
			String server = "http://localhost:8083/";
			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
			ResponseEntity<BusinessDataTO> responseEntity = rest.exchange(server + "BusinessData", HttpMethod.GET, requestEntity, BusinessDataTO.class);
			this.myId = responseEntity.getBody().id;
			this.UPDATES_INTERVAL_SEC = responseEntity.getBody().time;
			System.out.println("*********** Got id : " + myId + " time = " + UPDATES_INTERVAL_SEC);
			startTimer();
		 }catch(org.springframework.web.client.ResourceAccessException ex) {
			  System.err.println(LocalDateTime.now().toLocalTime().toString() + " Connection error in end of day timer ");
		 }
	}
	
	private void startTimer() {
		if(timer == null) {
			timer = new Timer(true);
			
			timer.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
					  try {
							String server = "http://localhost:8083/";
							RestTemplate rest = new RestTemplate();
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							System.out.println(LocalDateTime.now().toLocalTime().toString() + " fake business timer elapsed");
							
							// Preparing the update:
							UpdateFromBusinessTO update = new UpdateFromBusinessTO();
							update.setDate(getCurrTime());
							update.setId(myId);
							update.setNumberOFPeople(counts[currIndex]);
							update.setNumberOFPeopleThatEnter(entering[currIndex]);
							update.setNumberOFPeopleThatExit(exiting[currIndex]);
							
							if(++currIndex >= counts.length) {
								currIndex = 0;
							}
							
							
							HttpEntity<UpdateFromBusinessTO> requestEntity = new HttpEntity<UpdateFromBusinessTO>(update, headers);
							rest.exchange(server + "updateFromBusiness", HttpMethod.POST, requestEntity, String.class);
					  }catch(org.springframework.web.client.ResourceAccessException ex) {
						  System.err.println(LocalDateTime.now().toLocalTime().toString() + " Connection error in end of day timer ");
					  }
				  }
				}, 0, UPDATES_INTERVAL_SEC * 1000);
		}
	}
	
	public void stop() {
		if(timer != null) {
			timer.cancel();
		}
	}
	
	public static int getCurrTime() {
		return LocalDateTime.now().toLocalTime().getHour() * 100 + 
				LocalDateTime.now().toLocalTime().getMinute();
	}
}
