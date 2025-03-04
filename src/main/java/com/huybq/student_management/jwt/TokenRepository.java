package com.huybq.student_management.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.userId = u.userId\s
            where u.userId = :id and (t.expired = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    void deleteTokenByUser_UserId(Integer userUserId);

    Optional<Token> findByToken(String token);
}
