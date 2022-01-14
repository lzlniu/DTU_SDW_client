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

public class customerAPI {
	Client c;
	WebTarget account, payment, report, token;
	

	
	public customerAPI() {
		c = ClientBuilder.newClient();
		account = c.target("http://localhost:8080/accounts/");
		payment = c.target("http://localhost:8081/");
		report = c.target("http://localhost:8082/");
		token = c.target("http://localhost:8083/");
	}

	public String registerCustomer(String firstName, String lastName, String bankID, String CPR) throws Exception {
		Response response = account.path("customers").request().post(Entity.entity(new DtuPayUser(firstName,lastName,null,bankID,CPR), MediaType.APPLICATION_JSON));
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
		List<DtuPayUser> customers = account.path("customers").request().get(new GenericType<List<DtuPayUser>>(){});
		return isUserThere(customers,cid);
	}

	public List<String> getNewTokens(DtuPayUser customer, int n) throws Exception {
		Response r = token.path("tokens").path(String.valueOf(n)).request()
				.post(Entity.entity(customer, MediaType.APPLICATION_JSON));
		if (r.getStatusInfo() == Response.Status.OK){
			return r.readEntity(new GenericType<List<String>>(){});
		} else {
			System.out.println("Response status: " + r.getStatus());
			throw new Exception(r.readEntity(String.class));
		}
	}

	public List<Payment> getReport(String customerID) {
		List<Payment> payments = report.path("reports/customers").path(customerID).
				                        request().get(new GenericType<List<Payment>>(){});
		return payments;
	}
}
