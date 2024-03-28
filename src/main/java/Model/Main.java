package Model;

//https://www.boxuk.com/insight/creating-a-rest-api-quickly-using-pure-java/
//https://ormlite.com/javadoc/ormlite-core/com/j256/ormlite/dao/Dao.html
//https://ormlite.com/javadoc/ormlite-core/com/j256/ormlite/table/TableUtils.html

import Controllers.DbManager;
import Controllers.EndpointsHandlers;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) throws SQLException {
        EndpointsHandlers handlers = new EndpointsHandlers();
        handlers.runServer();
        Runnable r = new Monitor();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(r, 10, 5, TimeUnit.SECONDS);
    }

    static class Monitor implements Runnable {

        @Override
        public void run() {

            List<URL> urls = null;
            try {
                urls = DbManager.getInstance().getAllURLs();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (URL url : urls) {
                HttpGet httpget = new HttpGet(url.getAddress());
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httpget);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int statusCode = response.getStatusLine().getStatusCode();
                try {
                    DbManager.addRequestLog(url, statusCode);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}