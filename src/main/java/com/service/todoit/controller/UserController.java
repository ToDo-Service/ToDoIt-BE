package com.service.todoit.controller;

import com.service.todoit.common.api.Api;
import com.service.todoit.domain.project.dto.ProjectDto;
import com.service.todoit.domain.project.dto.ProjectResponseDto;
import com.service.todoit.domain.user.User;
import com.service.todoit.domain.user.dto.LoginResponse;
import com.service.todoit.domain.user.dto.OauthUser;
import com.service.todoit.domain.user.dto.UserDto;
import com.service.todoit.sercurity.jwt.JwtTokens;
import com.service.todoit.sercurity.jwt.TokenProvider;
import com.service.todoit.service.ProjectService;
import com.service.todoit.service.UserService;
import com.service.todoit.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final ProjectService projectService;

    @PostMapping("/social/{type}")
    public ResponseEntity<Api<?>> googleLogin(
            @PathVariable String type,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = request.getHeader("Authorization").substring(7);
        // 유저 정보
        OauthUser oauthUser = userService.getUserInfo(accessToken, type);
        User user = userService.saveAndGetUser(oauthUser, type);

        List<ProjectResponseDto> projects = projectService.getProjectList(user.getId());

        JwtTokens tokens = tokenProvider.issueTokens(user);

        LoginResponse loginResponse = new LoginResponse(UserDto.From(user, tokens.getAccessToken()), projects);

        CookieUtil.addCookie(response, "refreshToken", tokens.getRefreshToken());
        return ResponseEntity.<Api<?>>ok()
                .body(Api.OK("로그인이 성공적으로 수행되었습니다.", loginResponse));
    }

}