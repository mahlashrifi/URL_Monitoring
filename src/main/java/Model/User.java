package Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column
    private Date created_at;

    @Column
    private Date updated_at;

    @Column
    private Date deleted_at;

    @Column
    private String username;

    @Column
    private String password;



    public User() {
        // ORMLite needs a no-arg constructor
    }

    public int getUser_id() {
        return user_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", created_at=" + created_at +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
