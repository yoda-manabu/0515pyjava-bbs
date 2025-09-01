package dto;

import java.sql.Timestamp;

public class ReplyDTO {
    private int id;
    private int postsId;
    private String anonId;
    private String content;
    private Timestamp createdAt;
    private String displayName;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostsId() { return postsId; }
    public void setPostsId(int postsId) { this.postsId = postsId; }

    public String getAnonId() { return anonId; }
    public void setAnonId(String anonId) { this.anonId = anonId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
