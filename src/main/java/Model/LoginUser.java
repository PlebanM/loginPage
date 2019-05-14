package Model;

public class LoginUser {

    private Integer id;
    private String login;
    private String password;

    public LoginUser(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
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

