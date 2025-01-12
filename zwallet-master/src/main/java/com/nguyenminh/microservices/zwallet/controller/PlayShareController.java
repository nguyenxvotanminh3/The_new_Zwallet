package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.dto.PlayShareRequest;
import com.nguyenminh.microservices.zwallet.dto.PlayShareResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PaginatedResponse;
import com.nguyenminh.microservices.zwallet.model.PlayShare;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.service.PlayShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v5")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PlayShareController {
    private final PlayShareService playShareService;

    @GetMapping("/play")
@ResponseStatus(HttpStatus.OK)
public PaginatedResponse<PlayShareResponse> getPlayShareResponse(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sort,
        @RequestParam String userName
) throws UnsupportedEncodingException {
    return playShareService.getPlayShareResponsePagination(page,size,sort,userName);
}



    @PostMapping("/play/create")
    @ResponseStatus(HttpStatus.OK)
    public String createTransactionHistory (@RequestBody PlayShareRequest playShare){
        return playShareService.createPlayShare(playShare);
    }


        @PutMapping("/play/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable String id,@RequestBody PlayShareRequest playShare){
        return playShareService.updatePlayShare(id,playShare);
    }


        @PutMapping("/play/status/{playShareId}/{username}")
        @ResponseStatus(HttpStatus.OK)
        public String updateStatusForParticipant(@PathVariable String playShareId, @PathVariable String username) throws UnsupportedEncodingException {
            return playShareService.updateStatusForParticipant(playShareId,username);
        }

    @DeleteMapping("/play/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id){
         playShareService.deletePlayShare(id);
    }
}
