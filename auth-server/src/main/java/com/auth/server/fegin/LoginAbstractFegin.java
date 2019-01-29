package com.auth.server.fegin;

import com.auth.server.security.vo.AuthUser;

/**
 * Created by 李雷 on 2018/8/10.
 */
public interface LoginAbstractFegin {

    AuthUser findUserById(String id);

    AuthUser findUserByPhone(String phone);
}
