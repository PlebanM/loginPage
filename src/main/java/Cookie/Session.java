package Cookie;

import DAO.DaoException;
import DAO.LoginDao;
import Model.LoginUser;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.Optional;

public class Session {

    private LoginDao loginDao;
    private LoginCookieHelper cookieHelper;

    public Session(LoginDao loginDao, LoginCookieHelper cookieHelper){
        this.loginDao = loginDao;
        this.cookieHelper = cookieHelper;
    }

    public Optional<LoginUser> getUserOfSession(HttpExchange exchange) throws DaoException {
        Optional<HttpCookie> cookie = cookieHelper.getSessionIdCookie(exchange);
        if (cookie.isPresent()){
            return loginDao.getUserBySessionId(cookieHelper.deleteQuotesFromCookieValue(cookie.get().getValue()));
        }else{
            return Optional.empty();
        }
    }




}
