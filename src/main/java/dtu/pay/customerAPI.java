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
		account = c.target("http://localhost:8080/");
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
}
