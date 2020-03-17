package mouble;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class User {
    private Integer id;
    private String name;
    private Date createTime;
}
