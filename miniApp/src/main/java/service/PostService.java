package service;

import java.util.List;
import dao.PostDAO;
import dto.PostDTO;

public class PostService {
    private static final PostService instance = new PostService();
    private PostService() {}
    public static PostService getInstance() {
        return instance;
    }

    public List<PostDTO> getAllPosts() {
        PostDAO dao = new PostDAO();
        return dao.selectAll();
    }

    public PostDTO getPostById(int id) {
        PostDAO dao = new PostDAO();
        return dao.selectById(id);
    }

    public int createPost(PostDTO dto) {
    	PostDAO dao = new PostDAO();
    	return dao.insert(dto);
    }

}
