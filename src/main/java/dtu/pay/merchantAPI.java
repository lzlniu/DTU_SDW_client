package dtu.pay;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

public class merchantAPI {
    Client c;
    WebTarget account, payment, report, token;



    public merchantAPI() {
        c = ClientBuilder.newClient();
        account = c.target("http://localhost:8080/accounts/");
        payment = c.target("http://localhost:8081/");
        report = c.target("http://localhost:8082/");
        token = c.target("http://localhost:8083/");
    }

    public String registerMerchants(String firstName, String lastName, String bankID, String CPR) throws Exception {
        Response response = account.path("merchants").request().post(Entity.entity(new DtuPayUser(firstName,lastName,null,bankID,CPR), MediaType.APPLICATION_JSON));
        if (response.getStatusInfo() == Response.Status.OK){
            return response.readEntity(String.class);
        } else {
            throw new Exception(response.readEntity(String.class));
        }
    }

    public boolean merchantIsRegistered(String mid) {
        List<DtuPayUser> merchants= account.path("merchants").request().get(new GenericType<List<DtuPayUser>>(){});
        return isUserThere(merchants,mid);
    }

    public Boolean isUserThere(List<DtuPayUser> list, String id){
        for (DtuPayUser dtuPayUser : list) {
            if (dtuPayUser.getDtuPayID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public boolean pay(BigDecimal amount, String cid, String mid) throws Exception {
        Payment p = new Payment(cid, mid, amount);
        Response response = payment.path("payments").request().
                post(Entity.entity(p, MediaType.APPLICATION_JSON));
        if (response.getStatusInfo() == Response.Status.OK) {
            return true;
        } else {
            throw new Exception(response.readEntity(String.class));
        }
    }

    public List<Payment> getPayments(){
        return payment.path("payments").request().get(new GenericType<List<Payment>>(){});
    }
}
