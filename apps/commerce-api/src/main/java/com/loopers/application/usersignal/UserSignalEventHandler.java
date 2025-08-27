package com.loopers.application.usersignal;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.UnlikeEvent;
import com.loopers.domain.usersignal.UserSignalService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignalEventHandler {
    private final UserSignalService userSignalService;

    @Async
    @EventListener
    public void handleLikeEvent(LikeEvent event) {
        userSignalService.increaseLikeCount(event.getType(), event.getTargetId());
    }

    @Async
    @EventListener
    public void handleUnlikeEvent(UnlikeEvent event) {
        userSignalService.decreaseLikeCount(event.getType(), event.getTargetId());
    }
}
