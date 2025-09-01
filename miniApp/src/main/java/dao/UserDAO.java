package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.UserDTO;

public class UserDAO extends BaseDAO {

    public UserDTO selectByLoginId(String loginId) {
        UserDTO dto = null;
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
        		"SELECT id, login_id, password, display_name, anon_id, created_at FROM users WHERE login_id = ? AND is_deleted = FALSE")) {
            ps.setString(1, loginId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new UserDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setLoginId(rs.getString("login_id"));
                    dto.setPassword(rs.getString("password"));
                    dto.setDisplayName(rs.getString("display_name"));
                    dto.setAnonId(rs.getString("anon_id"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) { 
            System.err.println("UserDAO selectByLoginId error: " + e.getMessage());
            e.printStackTrace(); 
        }
        return dto;
    }

    // ★ insertメソッド追加
    public int insert(UserDTO dto) {
        int result = 0;
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);

        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (login_id, password, display_name, anon_id, created_at) VALUES (?, ?, ?, ?, NOW())"
            );
            ps.setString(1, dto.getLoginId());
            ps.setString(2, dto.getPassword());
            ps.setString(3, dto.getDisplayName());
            ps.setString(4, dto.getAnonId());
            
            result = ps.executeUpdate();
            tm.commit();
            
        } catch (SQLException e) {
            tm.rollback();
            System.err.println("UserDAO insert error: " + e.getMessage());
            e.printStackTrace();
        }
        tm.close();
        return result;
    }

    // ★ UserDeleteServiceで使うdeleteメソッド
    public int delete(String loginId) {
        int result = 0;
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);

        try {
            PreparedStatement ps = conn.prepareStatement(
            		"UPDATE users SET is_deleted = TRUE WHERE login_id = ?"
            );
            ps.setString(1, loginId);
            result = ps.executeUpdate();
            tm.commit();

        } catch (SQLException e) {
            tm.rollback();
            System.err.println("UserDAO delete error: " + e.getMessage());
            e.printStackTrace();
        }
        tm.close();
        return result;
    }

    // 会員情報を更新
    public int edit(UserDTO dto) {
        int result = 0;
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);

        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET password = ?, display_name = ? WHERE login_id = ?"
            );
            ps.setString(1, dto.getPassword());
            ps.setString(2, dto.getDisplayName());
            ps.setString(3, dto.getLoginId());

            result = ps.executeUpdate();
            tm.commit();

        } catch (SQLException e) {
            tm.rollback();
            System.err.println("UserDAO edit error: " + e.getMessage());
            e.printStackTrace();
        }
        tm.close();
        return result;
    }
}

