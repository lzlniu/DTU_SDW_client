package dtu.pay;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SimpleDTUPay {
	Client c;
	WebTarget r;
	
	public SimpleDTUPay() {
		c = ClientBuilder.newClient();
		r = c.target("http://localhost:8080/");
	}

	public boolean pay(int amount, String cid, String mid) throws Exception {
		Payment p = new Payment(cid, mid, amount);
		Response response = r.path("payments").request().
						   post(Entity.entity(p, MediaType.APPLICATION_JSON));
		if (response.getStatusInfo() == Response.Status.OK) {
			return true;
		} else {
			throw new Exception(response.readEntity(String.class));
		}
	}
	
	public List<Payment> getPayments(){
		return r.path("payments").request().get(new GenericType<List<Payment>>(){});
	}
}
