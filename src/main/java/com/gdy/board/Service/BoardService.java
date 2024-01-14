package com.gdy.board.Service;

import com.gdy.board.DTO.BoardDTO;
import com.gdy.board.Entity.BoardEntity;
import com.gdy.board.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// DTO(Controller에 사용) -> Entity : Entity class에서 사용
// Entity(Repository에 사용) -> DTO : DTO class에서 사용

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //DTO 값을 entity로 넘겨줌
        boardRepository.save(boardEntity);
    }
}
