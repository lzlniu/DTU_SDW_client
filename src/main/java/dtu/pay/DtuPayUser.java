package dtu.pay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
//@author s212643 - Xingguang Geng
public class DtuPayUser {
    private String firstName;
    private String lastName;
    private String dtuPayID;
    private String bankID;
    private String CPR;
}
