package Controllers;
import org.apache.log4j.BasicConfigurator;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

import static spark.Spark.get;
import static spark.Spark.post;

public class EndpointsHandlers {

    DbManager dbManager ;
    JWTController jwtController = new JWTController();
    public EndpointsHandlers() throws SQLException {

        BasicConfigurator.configure();
        dbManager = DbManager.getInstance();
    }
    public void runServer(){

        //add new user - ( to sign up )
        post("/users/create", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                String userName = request.queryParams("username");
                String password = request.queryParams("pass");
                dbManager.addUserToDb(userName, password);
                response.status(201);
                String res = "user creation done successfully!";
                return res;
            }
        });

        //add new user - ( to log in )
        get("/users/enter", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                String userName = request.queryParams("username");
                String password = request.queryParams("pass");
                if (dbManager.checkPass(userName, password)) {
                    String jwtToken = jwtController.createJWT(userName, password);
                    dbManager.registerLoginInDb(userName, jwtToken);
                    response.status(200);
                    response.body("Hi  " + userName + "! login done successfully.\n Your token is : " + jwtToken);
                }
                else{
                    response.status(401);
                    response.body("Username or password is invalid!");
                }

                return response.body();
            }
        });

        //add new url
        post("/api/url/create", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                String addr = request.queryParams("address");
                Integer thresh = Integer.parseInt(request.queryParams("threshold"));
                //zero index is Bearer word
                String jwtToken = request.headers("Authorization").split(" ")[1];
               try {
                    String userName = jwtController.extractUserNameFromJWT(jwtToken);
                    dbManager.addNewUrl(userName, addr, thresh);
                    response.body("New URL with followed info added successfully!\n" + "username = " +userName +"\n url address = " + addr );
                    response.status(201);
                }
                catch (Exception e){
                    response.status(401);
                    response.body("Invalid jwt");
                }

                return response.body();
            }
        });

        //get user urls
        get("/api/get/urls", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {

                try {
                    String jwtToken = request.headers("Authorization").split(" ")[1];
                    String userName = jwtController.extractUserNameFromJWT(jwtToken);
                    StringBuilder urls = new StringBuilder();
                    System.out.println("The URLs related to the user with username = " +userName+" are as follows :");
                    dbManager.getUrlsByUserName(userName).forEach(c -> urls.append(c+"\n"));
                    response.body(urls.toString());
                    response.status(200);
                }
                catch (Exception e){
                    response.body("Invalid jwt");
                    response.status(401);
                }
                return response.body();
            }


        });

        //get alarms
        get("/api/alarms", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                try {
                    String jwtToken = request.headers("Authorization").split(" ")[1];
                    String userName = jwtController.extractUserNameFromJWT(jwtToken);
                    StringBuilder urls = new StringBuilder();
                    System.out.println("The URLs monitoring report related to the user with username = " +userName+" is as follows :");
                    response.body(dbManager.getUrlsReportByUserName(userName));
                    response.status(200);
                }
                catch (Exception e){
                    response.body("Invalid jwt");
                    response.status(401);
                }

                return response.body();
            }
        });


        //get url stats
        get("/api/url/stat", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                try {
                    String jwtToken = request.headers("Authorization").split(" ")[1];
                    String userName = jwtController.extractUserNameFromJWT(jwtToken);
                    StringBuilder urls = new StringBuilder();
                    System.out.println("The URLs monitoring report related to the user with username = " +userName+" is as follows :");
                    response.body(dbManager.getUrlsReportByUserName(userName));
                    response.status(200);
                }
                catch (Exception e){
                    response.body("Invalid jwt");
                    response.status(401);
                }

                return response.body();
            }
        });

        //get url alerts
        get("/api/urls/:urlID", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                try {
                    String jwtToken = request.headers("Authorization").split(" ")[1];
                    String userName = jwtController.extractUserNameFromJWT(jwtToken);
                    StringBuilder urls = new StringBuilder();
                    response.body(dbManager.getAlerts(userName));
                    response.status(200);
                }
                catch (Exception e){
                    response.body("Invalid jwt");
                    response.status(401);
                }

                return response.body();
            }
        });

    }


    }

