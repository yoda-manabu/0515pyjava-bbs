package service;

import java.util.UUID;

import dao.UserDAO;
import domain.User;
import dto.UserDTO;

public class UserRegisterService {

    // ログインIDの重複チェック
    public boolean userEntryConfirm(User user) {
        UserDAO userDAO = new UserDAO();
        UserDTO userDTO = userDAO.selectByLoginId(user.getLoginId());
        return userDTO == null;
    }

    public boolean userEntryDo(User user) {
        try {
            UserDAO userDAO = new UserDAO();
            // UserDTOを作成 - displayNameを正しく設定
            UserDTO dto = new UserDTO();
            dto.setLoginId(user.getLoginId());
            dto.setPassword(user.getPassword());
            dto.setDisplayName(user.getDisplayName()); // getName()ではなくgetDisplayName()を使用
            dto.setAnonId(UUID.randomUUID().toString());
            
            int result = userDAO.insert(dto);
            return result == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
    
    // 新規会員登録
//    public boolean userEntryDo(User user) {
//        UserDAO userDAO = new UserDAO();
//
//        // ★ anon_idを必ずUUIDで生成
//        UserDTO dto = new UserDTO(user.getLoginId(), user.getPassword(), user.getName());
//        dto.setAnonId(UUID.randomUUID().toString());
//
//        int result = userDAO.insert(dto);
//        return result == 1;
//    }
//}
