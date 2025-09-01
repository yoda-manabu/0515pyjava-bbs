package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ReplyDTO;

public class ReplyDAO extends BaseDAO {

	
	public List<ReplyDTO> selectByPostId(int postId) {
	    List<ReplyDTO> list = new ArrayList<>();
	    Connection conn = getConnection();
	    try {
	        PreparedStatement ps = conn.prepareStatement(
	            "SELECT r.id, r.posts_id, r.anon_id, r.content, r.created_at, " +
	            "COALESCE(u.display_name, '匿名ユーザー') as user_display_name " +
	            "FROM replies r " +
	            "LEFT JOIN users u ON r.anon_id = u.anon_id " +
	            "WHERE r.posts_id = ? ORDER BY r.created_at ASC"
	        );
	        ps.setInt(1, postId);
	        
	        System.out.println("DEBUG <ReplyDAO>: ps = " + ps);
	        
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            ReplyDTO dto = new ReplyDTO();
	            dto.setId(rs.getInt("id"));
	            dto.setPostsId(rs.getInt("posts_id"));
	            dto.setAnonId(rs.getString("anon_id"));
	            dto.setContent(rs.getString("content"));
	            dto.setCreatedAt(rs.getTimestamp("created_at"));
	            
	            String displayName = rs.getString("user_display_name");
	            dto.setDisplayName(displayName);
	            list.add(dto);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	
//	public List<ReplyDTO> selectByPostId(int postId) {
//	    List<ReplyDTO> list = new ArrayList<>();
//	    Connection conn = getConnection();
//	    try {
//	        PreparedStatement ps = conn.prepareStatement(
//	            "SELECT r.id, r.posts_id, r.anon_id, r.content, r.created_at, u.display_name " +
//	            "FROM replies r " +
//	            "LEFT JOIN users u ON r.anon_id = u.anon_id " +
//	            "WHERE r.posts_id = ? ORDER BY r.created_at ASC"
//	        );
//	        ps.setInt(1, postId);
//	        
//	        System.out.println("DEBUG <ReplyDAO>: ps = " + ps);
//	        
//	        ResultSet rs = ps.executeQuery();
//	        while (rs.next()) {
//	            ReplyDTO dto = new ReplyDTO();
//	            dto.setId(rs.getInt("id"));
//	            dto.setPostsId(rs.getInt("posts_id"));
//	            dto.setAnonId(rs.getString("anon_id"));
//	            dto.setContent(rs.getString("content"));
//	            dto.setCreatedAt(rs.getTimestamp("created_at"));
//	            dto.setDisplayName(rs.getString("display_name"));
//	            list.add(dto);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return list;
//	}
//	
	
	
//	public List<ReplyDTO> selectByPostId(int postId) {
//        List<ReplyDTO> list = new ArrayList<>();
//        Connection conn = getConnection();
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, posts_id, anon_id, content, created_at " +
//                "FROM replies WHERE posts_id = ? ORDER BY created_at ASC"
//            );
//            ps.setInt(1, postId);
//            
//            System.out.println("DEBUG <ReplyDAO>: ps = " + ps);
//            
//            
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                ReplyDTO dto = new ReplyDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setPostsId(rs.getInt("posts_id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setContent(rs.getString("content"));
//                dto.setCreatedAt(rs.getTimestamp("created_at"));
//                list.add(dto);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 返信IDで単一の返信を取得
     * @param id 返信ID
     * @return 返信データ（見つからない場合はnull）
     */
    public ReplyDTO selectById(int id) {
        ReplyDTO dto = null;
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, posts_id, anon_id, content, created_at " +
                "FROM replies WHERE id = ?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new ReplyDTO();
                dto.setId(rs.getInt("id"));
                dto.setPostsId(rs.getInt("posts_id"));
                dto.setAnonId(rs.getString("anon_id"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }

    public int insert(ReplyDTO dto) {
        int result = 0;
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO replies (posts_id, anon_id, content) VALUES (?, ?, ?)"
            );
            ps.setInt(1, dto.getPostsId());
            ps.setString(2, dto.getAnonId());
            ps.setString(3, dto.getContent());
            result = ps.executeUpdate();
            tm.commit();
        } catch (SQLException e) {
            tm.rollback();
            e.printStackTrace();
        }
        tm.close();
        return result;
    }
    
    /**
     * 返信ID指定で削除
     * @param replyId 削除する返信のID
     * @return 削除成功時true
     */
    public boolean deleteById(int replyId) {
        String sql = "DELETE FROM replies WHERE id = ?";
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, replyId);
            int result = ps.executeUpdate();
            tm.commit();
            return result == 1;
        } catch (SQLException e) {
            tm.rollback();
            e.printStackTrace();
        } finally {
            tm.close();
        }
        return false;
    }

    /**
     * 投稿IDに紐づく返信をすべて削除
     * @param postId 投稿ID
     * @return 削除された返信の数
     */
    public int deleteByPostId(int postId) {
        String sql = "DELETE FROM replies WHERE posts_id = ?";
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            int result = ps.executeUpdate();
            tm.commit();
            return result;
        } catch (SQLException e) {
            tm.rollback();
            e.printStackTrace();
        } finally {
            tm.close();
        }
        return 0;
    }
}


//package dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import dto.ReplyDTO;
//
//public class ReplyDAO extends BaseDAO {
//
//    public List<ReplyDTO> selectByPostId(int postId) {
//        List<ReplyDTO> list = new ArrayList<>();
//        Connection conn = getConnection();
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, posts_id, anon_id, content, created_at " +
//                "FROM replies WHERE posts_id = ? ORDER BY created_at ASC"
//            );
//            ps.setInt(1, postId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                ReplyDTO dto = new ReplyDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setPostsId(rs.getInt("posts_id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setContent(rs.getString("content"));
//                dto.setCreatedAt(rs.getTimestamp("created_at"));
//                list.add(dto);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    public int insert(ReplyDTO dto) {
//        int result = 0;
//        Connection conn = getConnection();
//        TransactionManager tm = new TransactionManager(conn);
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO replies (posts_id, anon_id, content) VALUES (?, ?, ?)"
//            );
//            ps.setInt(1, dto.getPostsId());
//            ps.setString(2, dto.getAnonId());
//            ps.setString(3, dto.getContent());
//            result = ps.executeUpdate();
//            tm.commit();
//        } catch (SQLException e) {
//            tm.rollback();
//            e.printStackTrace();
//        }
//        tm.close();
//        return result;
//    }
//    
// // コメント削除
//    public boolean deleteById(int replyId) {
//        String sql = "DELETE FROM replies WHERE id = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, replyId);
//            int result = ps.executeUpdate();
//            return result == 1;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//}
