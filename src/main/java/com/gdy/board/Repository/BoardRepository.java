package com.gdy.board.Repository;

import com.gdy.board.Entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//무조건 entity만 받는다.
public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
}
