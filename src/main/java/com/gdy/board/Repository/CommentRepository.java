package com.gdy.board.Repository;

import com.gdy.board.Entity.BoardEntity;
import com.gdy.board.Entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    // select * from comment_table where board_id=? order by id desc;

    //select * from comment_table where board_id=? = findAllByBoardEntity
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);

}
