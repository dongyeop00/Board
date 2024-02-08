package com.gdy.board.Controller;

import com.gdy.board.DTO.BoardDTO;
import com.gdy.board.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
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
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable){
        // 1. 해당 게시글의 조회수를 하나 올린다.
        boardService.updateHits(id);

        // 2. 게시글 데이터를 가져와서 detail.html에 출력한다.
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page",pageable.getPageNumber());
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

    @GetMapping("/paging") // /board/paging?page=1
    public String paging(@PageableDefault(page=1)Pageable pageable, Model model){
        // 한페이지에 몇개씩 보냐에 따라 전체 페이지 수가 달라짐
        //int pageNumber = pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
        // 7 8 9 보여줘야 하는데 전체 페이지가 8이면 8을 보여줘라

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "paging";
    }
}
