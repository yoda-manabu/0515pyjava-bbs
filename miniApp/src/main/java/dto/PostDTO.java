package dto;

import java.sql.Timestamp;

public class PostDTO {
    private int id;           // 投稿ID
    private String anonId;    // 匿名ユーザーID
    private String category;  // カテゴリ
    private String title;     // タイトル
    private String content;   // 本文
    private Timestamp createdAt; // 投稿日時
    private String displayName;
    
    public PostDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAnonId() { return anonId; }
    public void setAnonId(String anonId) { this.anonId = anonId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
