package com.gdy.board.Service;

import com.gdy.board.DTO.BoardDTO;
import com.gdy.board.Entity.BoardEntity;
import com.gdy.board.Entity.BoardFileEntity;
import com.gdy.board.Repository.BoardFileRepository;
import com.gdy.board.Repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// DTO(Controller에 사용) -> Entity : Entity class에서 사용
// Entity(Repository에 사용) -> DTO : DTO class에서 사용

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        //파일 첨부 여부에 따라 로직 분리
        if(boardDTO.getBoardFile().isEmpty()){
            // 첨부 파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //DTO 값을 entity로 넘겨줌
            boardRepository.save(boardEntity);
        } else{
            // 첨부 파일이 있음
            /*
            // 1. DTO에 담긴 파일을 꺼냄
            MultipartFile boardFile = boardDTO.getBoardFile();
            // 2. 파일의 이름을 가져옴
            String originalFilename = boardFile.getOriginalFilename();
            // 3. 서버 저장용 이름으로 만듦
            // System.currentTimeMillis ( 1970년 기준으로 현재까지 지난 ms 값 )
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            // 4. 저장 경로 설정
            String savePath = "C:\\springboot_img\\" + storedFileName; // C:\\springboot_img/8468465_내사진.jpg
            // 5. 해당 경로에 파일 저장
            boardFile.transferTo(new File(savePath));
            // 6. board_table에 해당 데이터 save 처리
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO); //여기엔 id 값이 없음
            // 7. board_file_table에 해당 데이터 save 처리
            Long savedId = boardRepository.save(boardEntity).getId(); // 해당 게시글의 (pk)id를 가져옴
            BoardEntity board = boardRepository.findById(savedId).get(); // id값으로 boardEntity 자체를 가져온다.

            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
            boardFileRepository.save(boardFileEntity);
             */

            //다중 파일 첨부 로직
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO); //여기엔 id 값이 없음
            Long savedId = boardRepository.save(boardEntity).getId(); // 해당 게시글의 (pk)id를 가져옴
            BoardEntity board = boardRepository.findById(savedId).get(); // id값으로 boardEntity 자체를 가져온다.
            for( MultipartFile boardFile: boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:\\springboot_img\\" + storedFileName; // C:\\springboot_img/8468465_내사진.jpg
                boardFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }

    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        //boardEntityList에 담긴걸 boardDTOList에 옮겨 담는다.
        for(BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional //수동적으로 관리해줄 때 사용하는 어노테이션, 영속성 컨텐츠를 처리한다.
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id); //키 값을 찾아서 삭제
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; //한 페이지에 보여줄 글 개수
        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"createdTime"))); // id: Entity에 pk값

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // board : enetities 객체를 의미
        // map : board에서 하나씩 꺼내 boardDTO에 옮겨준다.
        // 목록 : id, writer, title, hits, createdTime만 보여주기
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
