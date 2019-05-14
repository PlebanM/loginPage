package DAO;

import Model.LoginUser;

import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SessionsDao {


    public void saveSession(LoginUser user, HttpCookie cookie) throws DaoException{
     try (Connection connection = DBCPDataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
                  "INSERT INTO sessions (user_id, session_id) VALUES (?,?);"))
          {
              preparedStatement.setInt(1, user.getId());
              preparedStatement.setString(2, cookie.getValue());
              preparedStatement.execute();

     } catch (SQLException e) {
         e.printStackTrace();
         throw new DaoException("Problem in SessionsDao");
     }
    }


    public void deleteSessionCookie(String cookie) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM sessions WHERE session_id = ?;"
             )) {
            statement.setString(1, cookie);
            System.out.println("Delete->" + cookie);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during deleting session cookie");
        }
    }

    public Optional<LoginUser> getUserOfSession(String session) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT authentication.id as id, login, password, sessions.session_id AS session FROM authentication JOIN sessions ON authentication.id=sessions.user_id \n" +
                             "WHERE sessions.session_id = ?"
             )) {
            statement.setString(1, session);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? Optional.of(createUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during getting user of session");
        }

    }

    private LoginUser createUser(ResultSet resultSet) throws SQLException {

        return new LoginUser(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"));
    }

}
