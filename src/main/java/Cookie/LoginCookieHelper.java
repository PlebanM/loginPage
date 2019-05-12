package Cookie;

import DAO.DaoException;
import DAO.LoginDao;
import Model.LoginUser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoginCookieHelper {
    private LoginDao loginDao;
    private static final String SESSION_COOKIE_NAME = "sessionId";

    public LoginCookieHelper() {
        this.loginDao = new LoginDao();
    }

    private HttpCookie createSessionNumber(){
        UUID uuid = UUID.randomUUID();
        return new HttpCookie(SESSION_COOKIE_NAME, uuid.toString());
    }

    public Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange){
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = parseCookies(cookieStr);
            return findCookieByName(SESSION_COOKIE_NAME, cookies);
    }

    public void createCookie(HttpExchange httpExchange, Optional<LoginUser> user) throws DaoException {
        HttpCookie cookie = createSessionNumber();
        loginDao.addSessionIdToUser(user.get(), cookie);
        httpExchange.getResponseHeaders().set("Set-Cookie", cookie.toString());
        redirectToResourcePage(httpExchange);
    }

    public String deleteQuotesFromCookieValue(String cookie) {
        return cookie.replace("\"", "");
    }

    private void redirectToResourcePage(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().set("Location", "/login");
        try {
            httpExchange.sendResponseHeaders(303,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<HttpCookie> findCookieByName(String name, List<HttpCookie> cookies){
        for(HttpCookie cookie : cookies){
            if(cookie.getName().equals(name))
                return Optional.ofNullable(cookie);
        }
        return Optional.empty();
    }

    private List<HttpCookie> parseCookies(String cookieString){
        List<HttpCookie> cookies = new ArrayList<>();
        if(cookieString == null || cookieString.isEmpty()){
            return cookies;
        }
        for(String cookie : cookieString.split(";")){
            int indexOfEq = cookie.indexOf('=');
            String cookieName = cookie.substring(0, indexOfEq);
            String cookieValue = cookie.substring(indexOfEq + 1);
            cookies.add(new HttpCookie(cookieName, cookieValue));
        }
        return cookies;
    }
}
