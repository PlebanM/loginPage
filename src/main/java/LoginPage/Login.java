package LoginPage;


import Cookie.LoginCookieHelper;
import Cookie.Session;
import DAO.DaoException;
import DAO.LoginDao;
import DAO.SessionsDao;
import Model.LoginUser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Login implements HttpHandler {


    private LoginCookieHelper loginCookieHelper;
    private LoginDao loginDao;
    private Session session;

    public Login(LoginCookieHelper loginCookieHelper, LoginDao loginDao) {
        this.loginCookieHelper = loginCookieHelper;
        this.session = new Session(new SessionsDao(),loginCookieHelper);
        this.loginDao = loginDao;
    }


    public void handle(HttpExchange httpExchange) throws IOException {

       if (httpExchange.getRequestMethod().equals("GET")){
           try {
               Optional<LoginUser> user = session.getUserOfSession(httpExchange);
               if(user.isPresent()){
                   redirectToResourcesPage(httpExchange,createLogoutPage(user.get().getLogin()));
               }else{
                   redirectToResourcesPage(httpExchange, createLoginPage());
               }
           } catch (DaoException e) {
               e.printStackTrace();
           }
       }


        if (httpExchange.getRequestMethod().equals("POST")) {
            Optional<LoginUser> userFromForm = getDataFromForm(httpExchange);
            try {
                Optional<LoginUser> user = loginDao.checkIfUserAndPasswordCorrect(userFromForm.get());
                if(user.isPresent()){
                    loginCookieHelper.createCookie(httpExchange, user);
                }else{
                    redirectToResourcesPage(httpExchange, createLoginPage());
                }
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

    private String createLoginPage() throws FileNotFoundException {
        File file = new File(
                getClass().getClassLoader().getResource("static/login.html").getFile()
        );
        Scanner scanner = new Scanner(file);

        String resource = scanner.useDelimiter("\\A").next();
        scanner.close();
        return resource;

    }

    private String createLogoutPage(String login) {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("static/logout.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("login", login);
        return template.render(model);

    }

    private void redirectToResourcesPage(HttpExchange httpExchange, String resource) throws IOException {
        httpExchange.sendResponseHeaders(200,resource.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(resource.getBytes());
        os.close();
    }



    private Optional<LoginUser> getDataFromForm(HttpExchange exchange) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String userCredentials = br.readLine();
        String[] entries = userCredentials.split("&");
        String login = URLDecoder.decode(entries[0].split("=")[1].trim(), StandardCharsets.UTF_8.toString());
        String password = URLDecoder.decode(entries[1].split("=")[1].trim(), StandardCharsets.UTF_8.toString());
        return Optional.ofNullable(new LoginUser(null, login, password, null));

    }

}



