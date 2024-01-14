package com.gdy.board.DTO;

import lombok.*;

import java.time.LocalDateTime;

//DTO(Data Transfer Object), VO, Bean : 정보를 주고받을 때 사용하는 객체
@Setter
@Getter
@NoArgsConstructor //기본 생성자
@AllArgsConstructor//모든 필드를 매개변수로 하는 생성자
@ToString
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPassword;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

}
