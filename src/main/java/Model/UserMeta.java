package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserMeta {

    @OneToOne
    private User user;

    @Column
    private int login;

    @Column
    private String token;

    public UserMeta() {
    }

    public User getUser() {
        return user;
    }

    public int getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserMeta{" +
                "user=" + user +
                ", login=" + login +
                ", token='" + token + '\'' +
                '}';
    }
}
