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

    //@author s213578 - Johannes Pedersen
    public merchantAPI(boolean runningLocally) {
        c = ClientBuilder.newClient();
        String serverHost = "";
        serverHost = runningLocally ? "http://localhost" : "http://fm-20.compute.dtu.dk";
        account = c.target(serverHost + ":8080/accounts/");
        payment = c.target(serverHost + ":8081/");
        report = c.target(serverHost + ":8082/");
        token = c.target(serverHost + ":8083/");
    }

    //@author s212643 - Xingguang Geng
    public String registerMerchant(String firstName, String lastName, String bankID, String CPR) throws Exception {
        Response response = account.path("merchants").request().post(Entity.entity(new DtuPayUser(firstName,lastName,null,bankID,CPR), MediaType.APPLICATION_JSON));
        if (response.getStatusInfo() == Response.Status.OK){
            return response.readEntity(String.class);
        } else {
            throw new Exception(response.readEntity(String.class));
        }
    }

    //@author s164422 - Thomas Bergen
    public boolean merchantIsRegistered(String mid) {
        List<DtuPayUser> merchants= account.path("merchants").request().get(new GenericType<List<DtuPayUser>>(){});
        return isUserThere(merchants, mid);
    }

    //@author s215949 - Zelin Li
    public void deleteMerchant(String mid) throws Exception {
        Response r = account.path("merchants").path(mid).request().delete();
        if (r.getStatusInfo() != Response.Status.OK){
            throw new Exception(r.readEntity(String.class));
        }
    }

    //@author s174293 - Kasper Jørgensen
    public Boolean isUserThere(List<DtuPayUser> list, String id){
        for (DtuPayUser dtuPayUser : list) {
            if (dtuPayUser.getDtuPayID().equals(id)){
                return true;
            }
        }
        return false;
    }

    //@author s202772 - Gustav Kinch
    public boolean pay(BigDecimal amount, String customerToken, String mid) throws Exception {
        Payment p = new Payment(customerToken, mid, amount);
        Response response = payment.path("payments").request().
                post(Entity.entity(p, MediaType.APPLICATION_JSON));
        if (response.getStatusInfo() == Response.Status.OK) {
            return true;
        } else {
            throw new Exception(response.readEntity(String.class));
        }
    }

    //@author s164422 - Thomas Bergen
    public boolean refund(BigDecimal amount, String customerToken, String mid) throws Exception {
        Payment p = new Payment(customerToken, mid, amount);
        Response response = payment.path("payments/refunds").request().
                post(Entity.entity(p, MediaType.APPLICATION_JSON));
        if (response.getStatusInfo() == Response.Status.OK) {
            return true;
        } else {
            System.out.println(response.getStatus());
            throw new Exception(response.readEntity(String.class));
        }
    }

    //@author s215949 - Zelin Li
    public List<Payment> getSuperReport(String password) {
        if (password.equals("managerPSW")) { //emulates authenticating manager
            return report
                    .path("reports")
                    .request()
                    .get(new GenericType<List<Payment>>(){});
        } else {
            return null;
        }
    }

    //@author s213578 - Johannes Pedersen
    public List<Payment> getReport(String merchantID) {
        return report
                .path("reports/merchants")
                .path(merchantID)
                .request()
                .get(new GenericType<List<Payment>>(){});
    }
}
