import Cookie.LoginCookieHelper;
import DAO.LoginDao;
import LoginPage.Login;
import LoginPage.Logout;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/static", new Static());
        server.createContext("/login", new Login(new LoginCookieHelper(), new LoginDao()));
        server.createContext("/logout", new Logout(new LoginDao()));

        server.setExecutor(null); // creates a default executor
        server.start();

    }
}
