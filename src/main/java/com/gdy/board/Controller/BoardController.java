package com.gdy.board.Controller;

import com.gdy.board.DTO.BoardDTO;
import com.gdy.board.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String save(){
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println(boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/") // 주소 : /board/
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        // 1. 해당 게시글의 조회수를 하나 올린다.
        boardService.updateHits(id);

        // 2. 게시글 데이터를 가져와서 detail.html에 출력한다.
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id); //id로 값들을 찾아 dto에 저장
        model.addAttribute("boardUpdate", boardDTO); //저장한 값을 model에 담아 전달
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model){
        BoardDTO board = boardService.update(boardDTO); // dto -> entity로 변환해 저장
        model.addAttribute("board",board); //저장한 값을 model에 담아 전달
        return "detail";
        // return "redirect:/board/" + boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/"; //리스트로 이동
    }
}
