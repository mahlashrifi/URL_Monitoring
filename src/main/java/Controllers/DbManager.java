package Controllers;

import Model.Request;
import Model.URL;
import Model.User;
import Model.UserMeta;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbManager {


    private ConnectionSource connectionSource;
    private Dao<User,String> userDao;
    private Dao<UserMeta,String> userMetaDao;
    private Dao<URL,String> urlDao;
    private static Dao<Request,String> requestDao;
    private static DbManager dbManager;

    private DbManager() throws SQLException {
        setupDb();
        //dropAllTables();
        initTables();
        initDaos();
    }

    public static DbManager getInstance() throws SQLException {
        if (dbManager == null)
            dbManager = new DbManager();

        return dbManager;
    }



    private void dropAllTables() throws SQLException {
        TableUtils.dropTable(connectionSource, User.class, false);
        TableUtils.dropTable(connectionSource, UserMeta.class, false);
        TableUtils.dropTable(connectionSource, Request.class, false);
        TableUtils.dropTable(connectionSource, URL.class, false);

    }
    public void setupDb() throws SQLException {
        String databaseUrl = "jdbc:sqlite:identifier.sqlite";
        connectionSource = new JdbcConnectionSource(databaseUrl);
    }


    public void initTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, URL.class);
        TableUtils.createTableIfNotExists(connectionSource, Request.class);
        TableUtils.createTableIfNotExists(connectionSource, UserMeta.class);




    }


    public static void addRequestLog(URL url, int statusCode) throws SQLException {
        Request request = new Request();
        request.setCreated_at(new Date());
        request.setUrl(url);
        request.setResult(statusCode);
        requestDao.create(request);

    }

    public void initDaos() throws SQLException {
        userDao = DaoManager.createDao(connectionSource, User.class);
        urlDao = DaoManager.createDao(connectionSource, URL.class);
        requestDao = DaoManager.createDao(connectionSource, Request.class);
        userMetaDao = DaoManager.createDao(connectionSource, UserMeta.class);

    }
    public void addNewUrl(String userName, String addr, int threshold) throws SQLException {
        URL url = new URL();
        User user = userDao.queryForEq("username", userName).get(0);
        url.setAddress(addr);
        url.setThreshold(threshold);
        url.setUser(user);
        urlDao.create(url);
    }
    public void addUserToDb(String userName, String password) throws SQLException {
        User user = new User();
        user.setUsername(userName);
        String haashedPass = Common.getMd5(password);
        user.setPassword(haashedPass);
        user.setCreated_at(new Date());
        userDao.create(user);
    }
    public String getAddressByUrlId(int urlId) throws SQLException {
        return urlDao.queryForId(String.valueOf(urlId)).getAddress();
    }
    public void registerLoginInDb(String userName, String jwtToken) throws SQLException {
        //note that we do not have duplicated user name
        User user = userDao.queryForEq("username", userName).get(0);
        UserMeta userMeta = new UserMeta();
        userMeta.setUser(user);
        userMeta.setLogin(1);
        userMeta.setToken(jwtToken);
        userMetaDao.create(userMeta);
    }



    public List<URL> getUrlsByUserName(String userName) throws SQLException {
        User user = userDao.queryForEq("username", userName).get(0);
        return urlDao.queryForEq("user_id", user.getUser_id());
    }



    public List<User> getAllUsers() throws SQLException {
        return userDao.queryForAll();
    }

    public boolean checkPass(String userName, String password) throws SQLException {
        User user = userDao.queryForEq("userName", userName).get(0);
        String passInDb = user.getPassword();
        if(Common.getMd5(password).equals(user.getPassword()))
            return true;
        else return false;
    }


    public List<URL> getAllURLs() throws SQLException {
        return urlDao.queryForAll();
    }

    public String getUrlsReportByUserName(String userName) throws SQLException {
        User user = userDao.queryForEq("username", userName).get(0);
        QueryBuilder<URL, String> urlQueryBuilder = urlDao.queryBuilder();
        QueryBuilder<Request, String> requestQueryBuilder = requestDao.queryBuilder();
        urlQueryBuilder.where().eq("user_id", user.getUser_id());
        requestQueryBuilder.join(urlQueryBuilder);
        PreparedQuery<Request> preparedQuery = requestQueryBuilder.prepare();
        List<Request> requests = requestDao.query(preparedQuery);
        List<Request> finalRequests = new ArrayList<>();
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        for (Request req : requests) {
            if(req.getCreated_at().after(date))
                finalRequests.add(req);
        }
        int success = 0;
        int notSuccess = 0;
        for (Request req : finalRequests) {
            if(req.getResult()/100 == 2)
                success+=1;
            else notSuccess +=1;
        }
        return "Report for username =" + userName +
                "\nNumber of today successful requests =" + success
                +"\nNumber of failed requests = " +notSuccess + finalRequests;
    }


    public String getAlerts(String userName) throws SQLException {
        User user = userDao.queryForEq("username", userName).get(0);
        List<URL> urls = urlDao.queryForEq("user_id", user.getUser_id());
        StringBuilder result = new StringBuilder();
        for (URL url : urls) {
            if (url.getFailed_times() >= url.getThreshold())
                    result.append(url);
        }
        return result.toString();

    }
}
