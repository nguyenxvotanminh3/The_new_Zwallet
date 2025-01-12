package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.DebtResponse;
import com.nguyenminh.microservices.zwallet.dto.PlayShareResponse;
import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.model.*;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaginationService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public PaginatedResponse<TransactionHistoryResponse> getTransactionHistoryPagination(int page, int size, String sort, String userName) throws UnsupportedEncodingException {

        // Must use redis


        // 1  _ Find in redis



        // if yes ->

        //
        UserModel userModel = userRepository.findByUserName(userName);

        if (userModel == null) {
            throw new RuntimeException("Can't find user");
        }


        List<TransactionHistory> transactionHistories = userModel.getTransactionHistory();


        transactionHistories.sort(Comparator.comparing(
                TransactionHistory::getCreatedAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));


        int totalElements = transactionHistories.size();


        int totalPages = (int) Math.ceil((double) totalElements / size);


        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);


        if (startIndex >= totalElements) {
            return new PaginatedResponse<>(page, size, totalPages, totalElements, new ArrayList<>());
        }


        List<TransactionHistory> paginatedTransactions = transactionHistories.subList(startIndex, endIndex)
                .stream()
                .filter(Objects::nonNull) // Bỏ qua phần tử null
                .collect(Collectors.toList());


        List<TransactionHistoryResponse> transactionResponses = paginatedTransactions.stream()
                .map(mapper::mapToTransactionResponse)
                .collect(Collectors.toList());


        return new PaginatedResponse<>(
                page,              // Trang hiện tại
                size,              // Kích thước trang
                totalPages,        // Tổng số trang
                totalElements,     // Tổng số giao dịch
                transactionResponses // Danh sách giao dịch trong trang hiện tại
        );
    }

    public PaginatedResponse<DebtResponse> getDebtResponsePagination(int page, int size, String sort, String userName) throws UnsupportedEncodingException {

        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel == null) {
            throw new RuntimeException("Can't find user");
        }


        List<Debt> debts = userModel.getDebts();


        debts.sort(Comparator.comparing(
                Debt::getCreatedAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));


        int totalElements = debts.size();


        int totalPages = (int) Math.ceil((double) totalElements / size);


        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);


        if (startIndex >= totalElements) {
            return new PaginatedResponse<>(page, size, totalPages, totalElements, new ArrayList<>());
        }


        List<Debt> paginatedDebt = debts.subList(startIndex, endIndex)
                .stream()
                .filter(Objects::nonNull) // Bỏ qua phần tử null
                .toList();


        List<DebtResponse> debtResponses = paginatedDebt.stream()
                .map(mapper::mapToDebtResponse)
                .collect(Collectors.toList());


        return new PaginatedResponse<>(
                page,              // Trang hiện tại
                size,              // Kích thước trang
                totalPages,        // Tổng số trang
                totalElements,     // Tổng số giao dịch
                debtResponses // Danh sách giao dịch trong trang hiện tại
        );
    }

    public PaginatedResponse<PlayShareResponse> getPlayShareResponsePagination(int page, int size, String sort, String userName) throws UnsupportedEncodingException {

        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel == null) {
            throw new RuntimeException("Can't find user");
        }


        List<PlayShare> playShares = userModel.getPlayShares();


        playShares.sort(Comparator.comparing(
                PlayShare::getCreatedAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));


        int totalElements = playShares.size();


        int totalPages = (int) Math.ceil((double) totalElements / size);


        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);


        if (startIndex >= totalElements) {
            return new PaginatedResponse<>(page, size, totalPages, totalElements, new ArrayList<>());
        }


        List<PlayShare> playShares1 = playShares.subList(startIndex, endIndex)
                .stream()
                .filter(Objects::nonNull) // Bỏ qua phần tử null
                .toList();


        List<PlayShareResponse> playShareResponses = playShares1.stream()
                .map(mapper::mapToPlayShareResponse)
                .toList();


        return new PaginatedResponse<>(
                page,              // Trang hiện tại
                size,              // Kích thước trang
                totalPages,        // Tổng số trang
                totalElements,     // Tổng số giao dịch
                playShareResponses // Danh sách giao dịch trong trang hiện tại
        );
    }
}
