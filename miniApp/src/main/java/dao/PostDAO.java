package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.PostDTO;

public class PostDAO extends BaseDAO {

    // 一覧取得（新しい順）
    public List<PostDTO> selectAll() {
        List<PostDTO> list = new ArrayList<>();
        Connection conn = getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, anon_id, category, title, content, created_at " +
                "FROM posts ORDER BY created_at DESC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PostDTO dto = new PostDTO();
                dto.setId(rs.getInt("id"));
                dto.setAnonId(rs.getString("anon_id"));
                dto.setCategory(rs.getString("category"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

 // 詳細取得（修正版2）
    
    public PostDTO selectById(int id) {
        PostDTO dto = null;
        Connection conn = getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT p.id, p.anon_id, p.category, p.title, p.content, p.created_at, " +
                "COALESCE(u.display_name, p.display_name, '匿名ユーザー') as user_display_name " +
                "FROM posts p " +
                "LEFT JOIN users u ON p.anon_id = u.anon_id " +
                "WHERE p.id = ?"
            );
            ps.setInt(1, id);
            
            System.out.println("DEBUG: SQL = " + ps.toString());
            
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto = new PostDTO();
                dto.setId(rs.getInt("id"));
                dto.setAnonId(rs.getString("anon_id"));
                dto.setCategory(rs.getString("category"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                
                String displayName = rs.getString("user_display_name");
                System.out.println("DEBUG: anon_id = " + rs.getString("anon_id"));
                System.out.println("DEBUG: user_display_name = " + displayName);
                
                dto.setDisplayName(displayName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }
    
    
    
    
// // 詳細取得（修正版）
//    public PostDTO selectById(int id) {
//        PostDTO dto = null;
//        Connection conn = getConnection();
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT p.id, p.anon_id, p.category, p.title, p.content, p.created_at, u.display_name " +
//                "FROM posts p " +
//                "LEFT JOIN users u ON p.anon_id = u.anon_id " +
//                "WHERE p.id = ?"
//            );
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                dto = new PostDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setCategory(rs.getString("category"));
//                dto.setTitle(rs.getString("title"));
//                dto.setContent(rs.getString("content"));
//                dto.setCreatedAt(rs.getTimestamp("created_at"));
//                dto.setDisplayName(rs.getString("display_name"));
//                System.out.println(dto.getDisplayName());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dto;
//    }
    
//    // 詳細取得
//    public PostDTO selectById(int id) {
//        PostDTO dto = null;
//        Connection conn = getConnection();
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, anon_id, category, title, content, created_at " +
//                "FROM posts WHERE id = ?"
//            );
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                dto = new PostDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setCategory(rs.getString("category"));
//                dto.setTitle(rs.getString("title"));
//                dto.setContent(rs.getString("content"));
//                dto.setCreatedAt(rs.getTimestamp("created_at"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dto;
//    }

    // 投稿登録
    public int insert(PostDTO dto) {
        int result = 0;
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);

        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO posts (anon_id, category, title, content) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, dto.getAnonId());
            ps.setString(2, dto.getCategory());
            ps.setString(3, dto.getTitle());
            ps.setString(4, dto.getContent());

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
     * 投稿削除（返信も含めて完全削除）
     * @param postId 削除する投稿のID
     * @return 削除成功時true
     */
    public boolean deleteById(int postId) {
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);
        
        try {
            // 1. まず関連する返信をすべて削除
            PreparedStatement psReply = conn.prepareStatement(
                "DELETE FROM replies WHERE posts_id = ?"
            );
            psReply.setInt(1, postId);
            int replyCount = psReply.executeUpdate();
            System.out.println("削除された返信数: " + replyCount);
            
            // 2. その後投稿を削除
            PreparedStatement psPost = conn.prepareStatement(
                "DELETE FROM posts WHERE id = ?"
            );
            psPost.setInt(1, postId);
            int postResult = psPost.executeUpdate();
            
            if (postResult == 1) {
                tm.commit();
                System.out.println("投稿ID " + postId + " を削除しました（返信 " + replyCount + " 件も削除）");
                return true;
            } else {
                tm.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            tm.rollback();
            System.err.println("投稿削除エラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            tm.close();
        }
    }
    
    /**
     * 既存投稿を管理者権限で削除（anon_idチェックなし）
     * @param postId 削除する投稿のID
     * @return 削除成功時true
     */
    public boolean forceDeleteById(int postId) {
        Connection conn = getConnection();
        TransactionManager tm = new TransactionManager(conn);
        
        try {
            // 投稿の存在確認
            PostDTO post = selectById(postId);
            if (post == null) {
                System.out.println("投稿ID " + postId + " は存在しません");
                return false;
            }
            
            // 1. 関連する返信をすべて削除
            PreparedStatement psReply = conn.prepareStatement(
                "DELETE FROM replies WHERE posts_id = ?"
            );
            psReply.setInt(1, postId);
            int replyCount = psReply.executeUpdate();
            
            // 2. 投稿を削除
            PreparedStatement psPost = conn.prepareStatement(
                "DELETE FROM posts WHERE id = ?"
            );
            psPost.setInt(1, postId);
            int postResult = psPost.executeUpdate();
            
            if (postResult == 1) {
                tm.commit();
                System.out.println("【管理者削除】投稿ID " + postId + " (" + post.getTitle() + ") を削除しました（返信 " + replyCount + " 件も削除）");
                return true;
            } else {
                tm.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            tm.rollback();
            System.err.println("管理者削除エラー: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            tm.close();
        }
    }
    
    /**
     * 投稿数を取得
     * @return 総投稿数
     */
    public int getPostCount() {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM posts");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 投稿に紐づく返信数を取得
     * @param postId 投稿ID
     * @return 返信数
     */
    public int getReplyCount(int postId) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM replies WHERE posts_id = ?"
            );
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
//import dto.PostDTO;
//
//public class PostDAO extends BaseDAO {
//
//    // 一覧取得（新しい順）
//    public List<PostDTO> selectAll() {
//        List<PostDTO> list = new ArrayList<>();
//        Connection conn = getConnection();
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, anon_id, category, title, content, created_at " +
//                "FROM posts ORDER BY created_at DESC"
//            );
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                PostDTO dto = new PostDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setCategory(rs.getString("category"));
//                dto.setTitle(rs.getString("title"));
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
//    // 詳細取得
//    public PostDTO selectById(int id) {
//        PostDTO dto = null;
//        Connection conn = getConnection();
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT id, anon_id, category, title, content, created_at " +
//                "FROM posts WHERE id = ?"
//            );
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                dto = new PostDTO();
//                dto.setId(rs.getInt("id"));
//                dto.setAnonId(rs.getString("anon_id"));
//                dto.setCategory(rs.getString("category"));
//                dto.setTitle(rs.getString("title"));
//                dto.setContent(rs.getString("content"));
//                dto.setCreatedAt(rs.getTimestamp("created_at"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dto;
//    }
//
//    // 投稿登録
//    public int insert(PostDTO dto) {
//        int result = 0;
//        Connection conn = getConnection();
//        TransactionManager tm = new TransactionManager(conn);
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO posts (anon_id, category, title, content) VALUES (?, ?, ?, ?)"
//            );
//            ps.setString(1, dto.getAnonId());
//            ps.setString(2, dto.getCategory());
//            ps.setString(3, dto.getTitle());
//            ps.setString(4, dto.getContent());
//
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
// // 投稿削除
//    public boolean deleteById(int postId) {
//        String sql = "DELETE FROM posts WHERE id = ?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, postId);
//            int result = ps.executeUpdate();
//            return result == 1;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//}
