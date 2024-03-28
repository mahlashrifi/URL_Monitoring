package Model;


import Controllers.DbManager;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Date;

public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date created_at;

    @Column
    private Date updated_at;

    @Column
    private Date deleted_at;

    @ManyToOne
    private URL url;

    @Column
    private int result;


    public int getId() {
        return id;
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

    public URL getUrl() {
        return url;
    }

    public int getResult() {
        return result;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {

        try {
            DbManager dbManager = DbManager.getInstance();
            return "Request{" +
                    "created_at=" + created_at +
                    ", url_id=" + dbManager.getAddressByUrlId(url.getUrl_id()) +
                    ", result=" + result +
                    '}';
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
