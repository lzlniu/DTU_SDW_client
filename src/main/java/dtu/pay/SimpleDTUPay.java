package dtu.pay;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class SimpleDTUPay {
	Client c;
	WebTarget r;
	
	public SimpleDTUPay() {
		c = ClientBuilder.newClient();
		r = c.target("http://localhost:8080/");
	}

	public boolean pay(int amount, String cid, String mid) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public List<Payment> getPayments(){
		return null;
	}
}
