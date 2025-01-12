package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.DebtResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.Debt;
import com.nguyenminh.microservices.zwallet.model.PaginatedResponse;
import com.nguyenminh.microservices.zwallet.repository.DebtRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final ValidateUserService validateUserService;
    private final Mapper mapper;
    private final UserDebtService userDebtService;
    private final PaginationService paginationService;
    private final DebtRepository debtRepository;
    private final UserModelService userModelService;
    private final UserRepository userRepository;

    public PaginatedResponse<DebtResponse> getDebtPagination(int page, int size, String sort, String userName) throws UnsupportedEncodingException {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        return paginationService.getDebtResponsePagination(page,size,sort,userName);
    }

    public UserResponse createDebt(String creatorName, String debtorName, Debt debt) {
        validateUserService.checkUserIsAcceptToUserApi(creatorName);
        return mapper.mapToUserResponse(
                userDebtService.updateDebt(creatorName,debtorName,debt)
        );
    }

    public String deleteDebt(String id) {
        return  userDebtService.deleteDebt(id);

    }

    public String adjustDebt(String id, String nameDebtor, Debt debt) {
        // Tìm kiếm Debt theo ID
        Optional<Debt> existingDebtOpt = debtRepository.findById(id);

        if (existingDebtOpt.isEmpty()) {
            throw new RuntimeException("Debt not found with ID: " + id);
        }

        Debt existingDebt = existingDebtOpt.get();

        // Kiểm tra xem debtor mới có tồn tại trong hệ thống hay không
        UserResponse newDebtorUser = userModelService.getUserByUserName3(nameDebtor);
        if (newDebtorUser == null) {
            throw new RuntimeException("Can't find user with username: " + nameDebtor);
        }

        // Lấy giá trị mới từ Debt được truyền vào
        String newDebtAmount = debt.getAmount();
        String newDebtContent = debt.getContent();
        Boolean newConfirmStatus = debt.isConfirmed(); // Lấy trạng thái confirmed

        // Nếu không có số tiền mới, sử dụng số tiền hiện tại
        if (newDebtAmount == null || newDebtAmount.isEmpty()) {
            newDebtAmount = existingDebt.getAmount();
        }

        // Nếu không có nội dung mới, sử dụng nội dung hiện tại
        if (newDebtContent == null || newDebtContent.isEmpty()) {
            newDebtContent = existingDebt.getContent();
        }

        // Cập nhật Debt với các giá trị mới
        existingDebt.setAmount(newDebtAmount);
        existingDebt.setContent(newDebtContent);

        // Cập nhật debtor mới
        existingDebt.setDebtor(userRepository.findByUserName(nameDebtor));

        // Cập nhật trạng thái confirmed
        existingDebt.setConfirmed(newConfirmStatus);

        // Lưu lại Debt đã được cập nhật
        debtRepository.save(existingDebt);

        // Trả về Debt đã được cập nhật
        return "done";
    }




}
