package LoginPage;

import Cookie.LoginCookieHelper;
import DAO.DaoException;
import DAO.LoginDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.net.HttpCookie;
import java.util.Optional;

public class Logout implements HttpHandler {
    private LoginCookieHelper loginCookieHelper = new LoginCookieHelper();
    private LoginDao loginDao;

    public Logout(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        deleteSessionCookie(httpExchange);
        httpExchange.getResponseHeaders().set("Location", "/login");
        httpExchange.sendResponseHeaders(303, 0);
    }

    private void deleteSessionCookie(HttpExchange exchange) {
        Optional<HttpCookie> cookie = loginCookieHelper.getSessionIdCookie(exchange);
        if (cookie.isPresent()){
            try {
                loginDao.deleteUserSessionId(loginCookieHelper.deleteQuotesFromCookieValue(cookie.get().getValue()));
            }catch (DaoException ex){
                ex.printStackTrace();
            }
        }
    }
}
