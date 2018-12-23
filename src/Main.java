import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Main {

	private static final int PREDICTION_INTERVAL = 4000;
	private static final int END_OF_DAY_INTERVAL = 10000;
	// Indicating if we want to simulate a fake business in the current run
	private static final boolean IS_FAKE_BUSINESS_ACTIVE = false;
	
	public static void main(String[] args) {

		Timer predictionsTimer  = new Timer(true);
		Timer endOfDayTimer = new Timer(true);
		
		initializeTimers(endOfDayTimer, predictionsTimer);
		
		// Initializing the fake business if necessary
		FakeLiveBusiness fakeLiveBusiness;
		if(IS_FAKE_BUSINESS_ACTIVE) {
			fakeLiveBusiness = new FakeLiveBusiness();
			fakeLiveBusiness.start();
		}
		
		System.out.println(LocalDateTime.now().toLocalTime().toString() + " Timers started");  
        Scanner in = new Scanner(System.in);
        boolean isRunning = true;
        while(isRunning) {
        	System.out.println("Enter e to stop timers.");    
        	String input = in.next();
	        if(input.toLowerCase().equals("e")) {
	        	isRunning = false;
	        }
        }
        
        System.out.println(LocalDateTime.now().toLocalTime().toString() + " Exiting");
        in.close();
        
        predictionsTimer.cancel();
        endOfDayTimer.cancel();
        
        if(IS_FAKE_BUSINESS_ACTIVE) {
			fakeLiveBusiness.stop();
		}
	}
	
	private static void initializeTimers(Timer endOfDayTimer, Timer predictionsTimer) {
		String server = "http://localhost:8083/";
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		endOfDayTimer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  try {
					  System.out.println(LocalDateTime.now().toLocalTime().toString() + " End of day timer elapsed");
					  HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
					  rest.exchange(server + "updatePredictions", HttpMethod.POST, requestEntity, String.class);
				  }catch(org.springframework.web.client.ResourceAccessException ex) {
					  System.err.println(LocalDateTime.now().toLocalTime().toString() + " Connection error in end of day timer ");
				  }
			  }
			}, 0, END_OF_DAY_INTERVAL);
		
		predictionsTimer = new Timer(true);
		predictionsTimer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  try {
					  System.out.println(LocalDateTime.now().toLocalTime().toString() + " Prediction timer elapsed");
					  HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
					  rest.exchange(server + "updatePredictions", HttpMethod.POST, requestEntity, String.class);
				  }catch(org.springframework.web.client.ResourceAccessException ex) {
					  System.err.println(LocalDateTime.now().toLocalTime().toString() + " Connection error in prediction timer ");  
				  }
			  }
			}, 0, PREDICTION_INTERVAL);
	}
}
