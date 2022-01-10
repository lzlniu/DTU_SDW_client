package dtu.pay;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;

public class SimpleDTUPay {
	Client c;
	WebTarget r;
	

	
	public SimpleDTUPay() {
		c = ClientBuilder.newClient();
		r = c.target("http://localhost:8080/");
	}

	public boolean pay(BigDecimal amount, String cid, String mid) throws Exception {
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

	public String registerCustomer(String firstName, String lastName, String bankID, String CPR) throws Exception {
		Response response = r.path("customers").request().post(Entity.entity(new DtuPayUser(firstName,lastName,null,bankID,CPR), MediaType.APPLICATION_JSON));
		if (response.getStatusInfo() == Response.Status.OK){
			return response.readEntity(String.class);

		} else {
			throw new Exception(response.readEntity(String.class));
		}
	}

	public String registerMerchants(String firstName, String lastName, String bankID, String CPR) throws Exception {
		Response response = r.path("merchants").request().post(Entity.entity(new DtuPayUser(firstName,lastName,null,bankID,CPR), MediaType.APPLICATION_JSON));
		if (response.getStatusInfo() == Response.Status.OK){
			return response.readEntity(String.class);
		} else {
			throw new Exception(response.readEntity(String.class));
		}
	}

	public Boolean isUserThere(List<DtuPayUser> list, String id){
		for (DtuPayUser dtuPayUser : list) {
			if (dtuPayUser.getDtuPayID().equals(id)){
				return true;
			}
		}
		return false;
	}

	public boolean customerIsRegistered(String cid) {
		List<DtuPayUser> customers = r.path("customers").request().get(new GenericType<List<DtuPayUser>>(){});
		return isUserThere(customers,cid);
	}

	public boolean merchantIsRegistered(String mid) {
		List<DtuPayUser> merchants= r.path("merchants").request().get(new GenericType<List<DtuPayUser>>(){});
		return isUserThere(merchants,mid);
	}
}
