package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.exception.UserNotFoundException;
import com.nguyenminh.microservices.zwallet.model.Debt;
import com.nguyenminh.microservices.zwallet.model.PasswordResetToken;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.DebtRepository;
import com.nguyenminh.microservices.zwallet.repository.PasswordResetTokenRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final ValidateUserService validateUserService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public UserModel updateDebt(String creatorName, String debtorName, Debt debt) {
        // Validate the creator's access
        validateUserService.checkUserIsAcceptToUserApi(creatorName);

        // Retrieve both users from the database
        UserModel userCreator = userRepository.findByUserName(creatorName);
        UserModel userDebtor = userRepository.findByUserName(debtorName);
        if(userDebtor == null){
            throw new UserNotFoundException("Can't find user");
        }
        // Initialize debt lists if null
        List<Debt> creatorDebts = userCreator.getDebts() != null ? userCreator.getDebts() : new ArrayList<>();
        List<Debt> debtorDebts = userDebtor.getDebts() != null ? userDebtor.getDebts() : new ArrayList<>();

        // Configure debt details
        debt.setCreator(userCreator);
        debt.setDebtor(userDebtor);
        debt.setConfirmed(false); // Set initial confirmation to false

        // Save the debt in the database
        debtRepository.save(debt);

        // Add debt to both users' lists
        creatorDebts.add(debt);
        debtorDebts.add(debt);

        // Update the debt lists for each user
        userCreator.setDebts(creatorDebts);
        userDebtor.setDebts(debtorDebts);

        // Save the users after updating debts
        userRepository.save(userCreator);
        userRepository.save(userDebtor);
        return userCreator;
    }

    private String generateSecureToken() {
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(100000 + secureRandom.nextInt(900000));
    }

    public ResponseEntity<?> acceptTheDebt(String token) {
        PasswordResetToken debtConfirmationToken = passwordResetTokenRepository.findByToken(token);

        // Check if token is valid and not expired
        if (debtConfirmationToken == null || debtConfirmationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token is invalid or expired");
        }

        // Mark the debt as confirmed
        Debt debt = debtRepository.findByDebtorId(debtConfirmationToken.getUserId());
        if (debt != null) {
            debt.setConfirmed(true);
            debtRepository.save(debt);
            return ResponseEntity.ok("Debt accepted successfully");
        }

        return ResponseEntity.badRequest().body("Debt not found or already confirmed");
    }

    public String deleteDebt(String id) {
         debtRepository.deleteById(id);
        return "deleted";
    }
}
