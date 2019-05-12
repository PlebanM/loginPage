package DAO;

import Model.LoginUser;

import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginDao {

    public Optional<LoginUser> checkIfUserAndPasswordCorrect(LoginUser loginUser) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            String SQL = "SELECT * FROM authentication WHERE login=? AND password=?";
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, loginUser.getLogin());
            pstmt.setString(2, loginUser.getPassword());
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                return Optional.of(new LoginUser(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("session")));
            }else {
                return Optional.empty();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new DaoException("Problem in LoginDao");
        }
        }


        public void addSessionIdToUser(LoginUser loginUser, HttpCookie cookie) throws DaoException {
            try(Connection connection = DBCPDataSource.getConnection()){
                String SQL = "UPDATE authentication SET session=? WHERE id=?";
                PreparedStatement pstmt = connection.prepareStatement(SQL);
                pstmt.setString(1, cookie.getValue());
                pstmt.setInt(2, loginUser.getId());
                pstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("Problem in DAO - addSessionIdToUser");
            }
        }
    public void deleteUserSessionId(String session) throws DaoException {
        try(Connection connection = DBCPDataSource.getConnection()){
            String SQL = "UPDATE authentication SET session=NULL WHERE session=?";
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, session);
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Problem in DAO - addSessionIdToUser");
        }
    }

    public Optional<LoginUser> getUserBySessionId(String sessionId) throws DaoException{
        try (Connection connection = DBCPDataSource.getConnection()){
            String SQL = "SELECT * FROM authentication WHERE session=?";
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, sessionId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                return Optional.of(new LoginUser(resultSet.getInt("id"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("session")));
            }else {
                return Optional.empty();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new DaoException("Problem in GetUserBySessinoId");
        }
    }



}
