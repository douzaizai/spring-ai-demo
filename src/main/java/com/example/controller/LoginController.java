package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.User;
import com.example.enums.ReturnCodeEnum;
import com.example.enums.UserStatus;
import com.example.mapper.UserMapper;
import com.example.request.LoginRequest;
import com.example.response.LoginResponse;
import com.example.response.Response;
import com.example.utils.JwtUtil;
import com.example.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, loginRequest.getUserName()).eq(User::getPassword, loginRequest.getPassword()).eq(User::getStatus, UserStatus.ACTIVE.getCode());

        List<User> userList = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userList)) {
            return Response.error(ReturnCodeEnum.FORBIDDEN);
        } else {
            User user = userList.get(0);
            UserVO userVO = UserVO.builder().id(user.getId()).userName(user.getUserName()).firstName(user.getFirstName()).lastName(user.getLastName()).build();
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUser(userVO);
            loginResponse.setToken(jwtUtil.generateToken());
            return Response.success(loginResponse);
        }
    }
}
