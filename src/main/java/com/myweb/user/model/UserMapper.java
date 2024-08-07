package com.myweb.user.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {
    String getPw(String user_id);
    String findId(UserDTO dto);
    UserDTO getUserById(String id);
    UserDTO getUserByNick(String nick);
    void updatePw(UserDTO dto);
    UserDTO checkPhone(String phone);
    List<UserDTO> getAllUserList();
    List<UserDTO> getUserList();
    List<UserDTO> getRankByRN(Map page);
    int getUserCnt();
    void createUser(UserDTO dto);

    int changeNick(UserDTO dto);

    void deleteUser(String id);

    void setPoint(UserDTO dto);

    void updateGrade(UserDTO dto);

    Date getDateByAttendance(String nick);

    void updateAttendanceDate(String nick);
}
