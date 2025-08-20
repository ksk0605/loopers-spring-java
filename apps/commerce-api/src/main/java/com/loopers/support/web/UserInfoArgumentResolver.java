package com.loopers.support.web;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.domain.user.UserInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String KEY_USER_ID = "X-USER-ID";

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public UserInfo resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        String userId = webRequest.getHeader(KEY_USER_ID);
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유저 ID 헤더는 필수입니다.");
        }

        User user = userService.get(userId);
        return new UserInfo(user.getId(), user.getUserId());
    }
}
