package com.hanghae.final_project.domain.repository.chat;

import com.hanghae.final_project.domain.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatJdbcRepository {

    private final JdbcTemplate jdbcTemplate;


    public void batchInsertRoomInventories(List<Chat> chatList){


        String sql = "INSERT INTO chats"
                +  "(message,users,workspace_id,created_at) VALUE(?,?,?,?)";


        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Chat chat = chatList.get(i);
                ps.setString(1,chat.getMessage());
                ps.setString(2,chat.getUsers());
                ps.setLong(3,chat.getWorkSpace().getId());
                ps.setString(4,chat.getCreatedAt());
            }

            @Override
            public int getBatchSize() {
                return chatList.size();
            }
        });
    }

}