package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.constant.TransactionCategory;
import com.nguyenminh.microservices.zwallet.dto.MoneyPredictResponse;
import com.nguyenminh.microservices.zwallet.model.MoneyPredict;
import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.model.PercentUsage;
import com.nguyenminh.microservices.zwallet.model.PercentUsageTotal;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.MoneyPredictRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsageCaculatorService {

    private final UserRepository userRepository;
    private final MoneyPredictRepository moneyPredictRepository;
    private final ValidateUserService validateUserService;
    private final Mapper mapper;

    public ResponseEntity<?> usageCaculate(String username) {
        validateUserService.checkUserIsAcceptToUserApi(username);

        UserModel userModel = userRepository.findByUserName(username);

        if (userModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find user with user name: " + username);
        }

        // Use a map to store category counts
        Map<TransactionCategory, AtomicInteger> categoryCountMap = new EnumMap<>(TransactionCategory.class);
        for (TransactionCategory category : TransactionCategory.values()) {
            categoryCountMap.put(category, new AtomicInteger(0));
        }

        List<TransactionHistory> transactionHistories = userModel.getTransactionHistory();
        List<TransactionHistoryResponse> transactionResponses = transactionHistories.stream()
                .map(mapper::mapToTransactionResponse)
                .toList();

        AtomicInteger totalTransaction = new AtomicInteger(userModel.getTransactionHistory().size());

        transactionResponses.forEach(transactionHistoryResponse -> {
            String categoryStr = transactionHistoryResponse.getCategory();
            if (categoryStr != null) {
                try {
                    TransactionCategory category = TransactionCategory.fromString(categoryStr);
                    categoryCountMap.get(category).incrementAndGet();
                } catch (IllegalArgumentException e) {
                    totalTransaction.getAndDecrement(); // Invalid category, ignore
                }
            }
        });

        // Log information
        categoryCountMap.forEach((category, count) -> log.info(category + ": " + count));

        // Create PercentUsage object and set the percentage for each category
        PercentUsage percentUsage = new PercentUsage();
        categoryCountMap.forEach((category, count) -> {
            double percentage = (float) (count.get() * 100L / totalTransaction.get());
            switch (category) {
                case FOOD -> percentUsage.setFood(percentage);
                case BILL -> percentUsage.setBill(percentage);
                case ENTERTAIN -> percentUsage.setEntertain(percentage);
                case SHOPPING -> percentUsage.setShopping(percentage);
                case INVESTMENT -> percentUsage.setInvestment(percentage);
                case MEDICINE -> percentUsage.setMedicine(percentage);
                case EDUCATION -> percentUsage.setEducation(percentage);
                case TRAVEL -> percentUsage.setTravel(percentage);
                case RENT -> percentUsage.setRent(percentage);
                case TRANSPORTATION -> percentUsage.setTransportation(percentage);
                case UTILITIES -> percentUsage.setUtilities(percentage);
                case SAVINGS -> percentUsage.setSavings(percentage);
                case CHARITY -> percentUsage.setCharity(percentage);
                case INSURANCE -> percentUsage.setInsurance(percentage);
                case GIFTS -> percentUsage.setGifts(percentage);
                case OTHERS -> percentUsage.setOthers(percentage);
                case RECEIVE_MONEY -> percentUsage.setRecive(percentage);
                case TRANSFER_MONEY -> percentUsage.setTransfer(percentage);
            }
        });

        return ResponseEntity.ok(percentUsage);
    }



    public ResponseEntity<?> usageCaculateTotal(String username) {
        // Kiểm tra người dùng
        validateUserService.checkUserIsAcceptToUserApi(username);

        // Khởi tạo map để lưu trữ tổng tiền cho mỗi category
        Map<TransactionCategory, AtomicInteger> categoryMap = new EnumMap<>(TransactionCategory.class);
        for (TransactionCategory category : TransactionCategory.values()) {
            categoryMap.put(category, new AtomicInteger(0));
        }

        // Tìm người dùng theo username
        UserModel userModel = userRepository.findByUserName(username);
        if (userModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find user with username: " + username);
        }

        List<TransactionHistory> transactionHistories = userModel.getTransactionHistory();

        // Chuyển các giao dịch thành các response
        List<TransactionHistoryResponse> transactionResponses = transactionHistories.stream()
                .map(mapper::mapToTransactionResponse)
                .toList();

        log.info("Transaction" + transactionResponses);

        // Tính tổng số tiền cho mỗi category
        transactionResponses.forEach(transactionHistoryResponse -> {
            try {
                // Lấy category của giao dịch
                TransactionCategory category = TransactionCategory.fromString(transactionHistoryResponse.getCategory());
                int amount = Integer.parseInt(transactionHistoryResponse.getAmountUsed());

                // Cộng số tiền vào category tương ứng
                categoryMap.get(category).addAndGet(amount);
            } catch (IllegalArgumentException e) {
                log.error("Unknown category: " + transactionHistoryResponse.getCategory());
            }
        });

        // Tạo đối tượng PercentUsageTotal để lưu tổng tiền cho mỗi category
        PercentUsageTotal percentUsageTotal = new PercentUsageTotal();
        percentUsageTotal.setFood(BigDecimal.valueOf(categoryMap.get(TransactionCategory.FOOD).get()));
        percentUsageTotal.setBill(BigDecimal.valueOf(categoryMap.get(TransactionCategory.BILL).get()));
        percentUsageTotal.setEntertain(BigDecimal.valueOf(categoryMap.get(TransactionCategory.ENTERTAIN).get()));
        percentUsageTotal.setShopping(BigDecimal.valueOf(categoryMap.get(TransactionCategory.SHOPPING).get()));
        percentUsageTotal.setInvestment(BigDecimal.valueOf(categoryMap.get(TransactionCategory.INVESTMENT).get()));
        percentUsageTotal.setMedicine(BigDecimal.valueOf(categoryMap.get(TransactionCategory.MEDICINE).get()));
        percentUsageTotal.setEducation(BigDecimal.valueOf(categoryMap.get(TransactionCategory.EDUCATION).get()));
        percentUsageTotal.setTravel(BigDecimal.valueOf(categoryMap.get(TransactionCategory.TRAVEL).get()));
        percentUsageTotal.setRent(BigDecimal.valueOf(categoryMap.get(TransactionCategory.RENT).get()));
        percentUsageTotal.setTransportation(BigDecimal.valueOf(categoryMap.get(TransactionCategory.TRANSPORTATION).get()));
        percentUsageTotal.setUtilities(BigDecimal.valueOf(categoryMap.get(TransactionCategory.UTILITIES).get()));
        percentUsageTotal.setSavings(BigDecimal.valueOf(categoryMap.get(TransactionCategory.SAVINGS).get()));
        percentUsageTotal.setCharity(BigDecimal.valueOf(categoryMap.get(TransactionCategory.CHARITY).get()));
        percentUsageTotal.setInsurance(BigDecimal.valueOf(categoryMap.get(TransactionCategory.INSURANCE).get()));
        percentUsageTotal.setGifts(BigDecimal.valueOf(categoryMap.get(TransactionCategory.GIFTS).get()));
        percentUsageTotal.setOthers(BigDecimal.valueOf(categoryMap.get(TransactionCategory.OTHERS).get()));
        percentUsageTotal.setRecive(BigDecimal.valueOf(categoryMap.get(TransactionCategory.RECEIVE_MONEY).get()));
        percentUsageTotal.setTransfer(BigDecimal.valueOf(categoryMap.get(TransactionCategory.TRANSFER_MONEY).get()));

        return ResponseEntity.ok(percentUsageTotal);
    }



    public ResponseEntity<?> getFutureFund(String userName,String incomes, boolean save) {
        validateUserService.checkUserIsAcceptToUserApi(userName);
        log.info("userName{}", userName);
        UserModel userModel = userRepository.findByUserName(userName);

        float needs = (float) (Integer.parseInt(incomes) * 55) /100;
        float saving = (float) (Integer.parseInt(incomes) * 10) /100;
        float investment = (float) (Integer.parseInt(incomes) * 10) /100;
        float hobbies = (float) (Integer.parseInt(incomes) * 10) /100;
        float emergency = (float) (Integer.parseInt(incomes) * 10) /100;
        float charity = (float) (Integer.parseInt(incomes) * 5) /100;

        MoneyPredict moneyPredict = new MoneyPredict();
        moneyPredict.setNeeds(BigDecimal.valueOf(needs));
        moneyPredict.setSavings(BigDecimal.valueOf(saving));
        moneyPredict.setInvestment(BigDecimal.valueOf(investment));
        moneyPredict.setHobbies(BigDecimal.valueOf(hobbies));
        moneyPredict.setEmergency(BigDecimal.valueOf(emergency));
        moneyPredict.setCharity(BigDecimal.valueOf(charity));
        moneyPredict.setUserModel(userModel);

        MoneyPredictResponse moneyPredictResponse = mapper.maptoMoneyPredictResponse(moneyPredict);
        if(save){
            moneyPredictRepository.save(moneyPredict);
            userModel.setMoneyPredict(moneyPredict);
            userRepository.save(userModel);
            moneyPredictRepository.save(moneyPredict);
        }

        return ResponseEntity.ok(moneyPredictResponse);

    }
}
