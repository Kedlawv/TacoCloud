package tacos.domain;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User  {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private  String username;
    private  String password;
    private  String fullname;

    public User() {
    }

    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public String getPassword() {
        return this.password;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }


}
