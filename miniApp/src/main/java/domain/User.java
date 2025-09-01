//アプリ内のユーザーデータを保持するオブジェクト。
//DTOとは別に業務ロジックや表示用の加工データを持てる。

package domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User{
	
	private int id;
	private String loginId;
	private String password;
	private String name;
	private String anonId;
	private Date createdAt;
	private String createdAtStr;
	private String displayName;


	public User(){}
	
	public User(String loginId,String password,String name) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnonId() {
		return anonId;
	}
	
   	public void setAnonId(String anonId) {
		this.anonId = anonId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
		this.createdAtStr = new SimpleDateFormat("yyyy年MM月dd日hh時mm分").format(createdAt);
		
	}

	public String getCreatedAtStr() {
		return createdAtStr;
	}
	
	public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}