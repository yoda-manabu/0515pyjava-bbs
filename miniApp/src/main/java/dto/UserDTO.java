// ===== 修正版 UserDTO.java =====
package dto;

import java.sql.Timestamp;

public class UserDTO {
    private int id;
    private String loginId;
    private String password;
    private String name;
    private String displayName;
    private String anonId;
    private Timestamp createdAt;

    public UserDTO() {}

    public UserDTO(String loginId, String password, String displayName) {
        this.loginId = loginId;
        this.password = password;
        this.displayName = displayName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // ★ メソッド名を正しく修正
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAnonId() { return anonId; }
    public void setAnonId(String anonId) { this.anonId = anonId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}