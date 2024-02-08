package com.gdy.board.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originFileName;

    @Column
    private String storedFileName;

    //부모를 다 가져와서 사용 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") //실제 db에 만들어지는 컬럼 이름
    private BoardEntity boardEntity;

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginFileName(originFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity); // pk값이 아니라 부모 엔티티를 넘겨줘야한다.
        return boardFileEntity;
    }
}
