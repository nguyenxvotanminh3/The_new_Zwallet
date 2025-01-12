package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.PlayShareRequest;
import com.nguyenminh.microservices.zwallet.dto.PlayShareResponse;
;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PaginatedResponse;

import com.nguyenminh.microservices.zwallet.model.PlayShare;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayShareService {

    private final ValidateUserService validateUserService;
    private final PaginationService paginationService;
    private final Mapper mapper;
    private final UserPlayShareService userPlayShareService;

    public String createPlayShare(PlayShareRequest playShare) {

        return userPlayShareService.createPlayShare(playShare);

    }

    public String updatePlayShare(String id,PlayShareRequest playShare) {

        return userPlayShareService.updatePlayShare(id,playShare);

    }


    public PaginatedResponse<PlayShareResponse> getPlayShareResponsePagination(int page, int size, String sort, String userName) throws UnsupportedEncodingException {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        return paginationService.getPlayShareResponsePagination(page,size,sort,userName);
    }

    public void deletePlayShare(String id) {

         userPlayShareService.deletePlayShare(id);

    }

    public String updateStatusForParticipant(String playShareId, String username) throws UnsupportedEncodingException {
        validateUserService.checkUserIsAcceptToUserApi(username);
        return userPlayShareService.updateStatusForParticipant(playShareId, username);
    }

}
