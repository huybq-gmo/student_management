package com.huybq.student_management.processor;

import com.huybq.student_management.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User user) throws Exception {
        log.info("Process user {}", user);
        return user;
    }
}
