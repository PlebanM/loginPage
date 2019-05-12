package Model;

public class LoginUser {

    private Integer id;
    private String login;
    private String password;
    private String sessionId;

    public LoginUser(Integer id, String login, String password, String sessionId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}

