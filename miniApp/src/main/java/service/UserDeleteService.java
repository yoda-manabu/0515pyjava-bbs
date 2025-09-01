// ===== 修正版 UserDeleteService.java =====
package service;

import dao.UserDAO;
import domain.User;

public class UserDeleteService {
    
    public boolean userDeleteDo(User user) {
        UserDAO userDAO = new UserDAO();
        // ★ String loginIdを直接渡す（DTOではなく）
        int result = userDAO.delete(user.getLoginId());
        
        return result == 1;
    }
}

