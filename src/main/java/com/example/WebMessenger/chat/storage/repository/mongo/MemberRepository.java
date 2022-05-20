package com.example.WebMessenger.chat.storage.repository.mongo;

import com.example.WebMessenger.chat.storage.entity.mongo.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("mongoMemberRepository")
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByUserName(String userName);

    boolean existsByUserName(String userName);
}
