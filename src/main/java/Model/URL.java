package Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int url_id;

    @Column
    private Date created_at;

    @Column
    private Date updated_at;

    @Column
    private Date deleted_at;

    @ManyToOne
    private User user;

    @Column
    private String address;

    @Column
    private int threshold;

    @Column
    private int failed_times;



    public URL() {
        // ORMLite needs a no-arg constructor
    }

    public int getUrl_id() {
        return url_id;
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

    public User getUser() {
        return user;
    }

    public String getAddress() {
        return address;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getFailed_times() {
        return failed_times;
    }



    public void setUrl_id(int url_id) {
        this.url_id = url_id;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setFailed_times(int failed_times) {
        this.failed_times = failed_times;
    }


    @Override
    public String toString() {
        return "address='" + address ;
    }
}
