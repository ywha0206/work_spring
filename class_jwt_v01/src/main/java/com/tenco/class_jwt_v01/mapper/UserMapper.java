package com.tenco.class_jwt_v01.mapper;

import com.tenco.class_jwt_v01.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 반드시 필요 (마이바티스 사용)
public interface UserMapper {
    void save(User user);
    User findByUsername(String username);
    void updateRefreshToken(User user);
}
