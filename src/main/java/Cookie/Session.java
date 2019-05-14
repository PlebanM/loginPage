package Cookie;

import DAO.DaoException;
import DAO.SessionsDao;
import Model.LoginUser;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.Optional;

public class Session {

    private SessionsDao sessionsDao;
    private LoginCookieHelper cookieHelper;

    public Session(SessionsDao sessionsDao, LoginCookieHelper cookieHelper){
        this.sessionsDao = sessionsDao;
        this.cookieHelper = cookieHelper;
    }

    public Optional<LoginUser> getUserOfSession(HttpExchange exchange) throws DaoException {
        Optional<HttpCookie> cookie = cookieHelper.getSessionIdCookie(exchange);
        if (cookie.isPresent()){
            return sessionsDao.getUserOfSession(cookieHelper.deleteQuotesFromCookieValue(cookie.get().getValue()));
        }else{
            return Optional.empty();
        }
    }




}
