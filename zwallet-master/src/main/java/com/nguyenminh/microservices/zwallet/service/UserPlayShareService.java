package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.PlayShareRequest;
import com.nguyenminh.microservices.zwallet.exception.UserNotFoundException;
import com.nguyenminh.microservices.zwallet.model.PlayShare;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.PlayShareRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPlayShareService {
    private final ValidateUserService validateUserService;
    private final UserRepository userRepository;
    private final PlayShareRepository playShareRepository;

    // Tạo PlayShare mới
    public String createPlayShare(PlayShareRequest playShareRequest) {
        List<UserModel> userModelList = new ArrayList<>();
        // Tạo đối tượng PlayShare từ dữ liệu yêu cầu
        PlayShare playShareEntity = PlayShare.builder()
                .id(playShareRequest.getId())
                .createdAt(playShareRequest.getCreatedAt())
                .totalAmount(playShareRequest.getTotalAmount())
                .status(false) // Luôn đặt status ban đầu là false
                .title(playShareRequest.getTitle())
                .userOwedAmounts(playShareRequest.getUserOwedAmounts()) // Cập nhật đúng kiểu Map
                .participants(userModelList)
                .build();

        // Kiểm tra và xử lý từng người dùng trong userOwedAmounts
        for (String name : playShareRequest.getUserOwedAmounts().keySet()) {
            UserModel user = userRepository.findByUserName(name);

            if (user == null) {
                throw new UserNotFoundException("Can't find user: " + name);
            }

            // Cập nhật danh sách PlayShares của người dùng
            user.getPlayShares().add(playShareEntity);

            // Thêm người dùng vào danh sách participants
            userModelList.add(user);

            // Cập nhật danh sách participants vào PlayShare
            playShareEntity.setParticipants(userModelList);
            playShareRepository.save(playShareEntity);

            // Lưu người dùng đã được cập nhật vào cơ sở dữ liệu
            userRepository.save(user);
        }

        return "PlayShare created and participants updated successfully.";
    }

    // Cập nhật PlayShare hiện tại
    public String updatePlayShare(String id, PlayShareRequest playShareRequest) {
        Optional<PlayShare> playShareOpt = playShareRepository.findById(id);
        log.info("1");

        // Nếu không tìm thấy đối tượng PlayShare, trả về lỗi
        if (!playShareOpt.isPresent()) {
            throw new RuntimeException("PlayShare not found with id: " + id);
        }

        playShareOpt.ifPresent(playShare -> {
            log.info("2");

            // Cập nhật các trường khác của PlayShare
            playShare.setTitle(playShareRequest.getTitle());
            playShare.setStatus(playShareRequest.getStatus());
            playShare.setTotalAmount(playShareRequest.getTotalAmount());
            playShare.setUserOwedAmounts(playShareRequest.getUserOwedAmounts());

            // Tạo lại danh sách participants mới
            List<UserModel> participants = new ArrayList<>();

            // Lưu lại danh sách PlayShare cũ của các user để xóa
            List<UserModel> currentParticipants = new ArrayList<>(playShare.getParticipants());

            for (String name : playShareRequest.getUserOwedAmounts().keySet()) {
                UserModel user = userRepository.findByUserName(name);  // Tìm người dùng theo tên

                if (user == null) {
                    log.error("User not found: " + name);
                    throw new UserNotFoundException("Can't find user: " + name);
                }

                // Nếu người dùng hiện tại không có trong danh sách participants thì không cần xóa họ
                if (!participants.contains(user)) {
                    participants.add(user);
                }

                // Cập nhật lại danh sách PlayShares của người dùng (nếu chưa có)
                if (!user.getPlayShares().contains(playShare)) {
                    user.getPlayShares().add(playShare);
                }

                // Lưu người dùng đã được cập nhật vào cơ sở dữ liệu
                userRepository.save(user);
            }

            // Lưu danh sách người dùng không còn tham gia nữa và xóa khỏi danh sách PlayShare của họ
            for (UserModel currentParticipant : currentParticipants) {
                // Nếu người dùng không có trong danh sách mới, xóa họ khỏi PlayShare
                if (!participants.contains(currentParticipant)) {
                    currentParticipant.getPlayShares().remove(playShare);
                    userRepository.save(currentParticipant);  // Cập nhật lại người dùng
                }
            }

            // Cập nhật lại danh sách participants trong PlayShare
            playShare.setParticipants(participants);

            // Lưu lại đối tượng PlayShare sau khi cập nhật
            playShareRepository.save(playShare);
        });

        return "PlayShare updated and participants updated successfully.";
    }


    public String updateStatusForParticipant(String playShareId, String username) {
        // Tìm PlayShare theo ID
        Optional<PlayShare> playShareOpt = playShareRepository.findById(playShareId);

        if (!playShareOpt.isPresent()) {
            throw new RuntimeException("PlayShare not found with id: " + playShareId);
        }

        // Lấy PlayShare
        PlayShare playShare = playShareOpt.get();

        // Tìm UserModel theo username
        UserModel user = userRepository.findByUserName(username);

        if (user == null) {
            log.error("User not found: " + username);
            throw new UserNotFoundException("Can't find user: " + username);
        }

        // Kiểm tra xem người dùng có tham gia PlayShare không
        if (!playShare.getParticipants().contains(user)) {
            throw new RuntimeException("User with username " + username + " is not part of this PlayShare.");
        }

        // Cập nhật status của người dùng thành true trong userOwedAmounts
        Map<String, Map<String, Boolean>> userOwedAmounts = playShare.getUserOwedAmounts();

        // Kiểm tra xem người dùng đã có trong userOwedAmounts
        if (userOwedAmounts.containsKey(username)) {
            Map<String, Boolean> amounts = userOwedAmounts.get(username);

            // Kiểm tra xem người dùng có số tiền nào và cập nhật status
            for (Map.Entry<String, Boolean> entry : amounts.entrySet()) {
                entry.setValue(true); // Đặt giá trị status thành true
            }
        } else {
            log.error("User is not in the owed amounts list: " + username);
            throw new RuntimeException("User " + username + " does not have owed amounts in this PlayShare.");
        }

        // Cập nhật lại PlayShare với userOwedAmounts đã thay đổi
        playShare.setUserOwedAmounts(userOwedAmounts);

        // Lưu lại PlayShare đã cập nhật
        playShareRepository.save(playShare);

        // Trả về thông báo thành công
        return "User's status updated successfully to 'true' in the PlayShare.";
    }


    // Xóa PlayShare
    public void deletePlayShare(String id) {
        // Lấy thông tin PlayShare từ cơ sở dữ liệu
        Optional<PlayShare> playShareOptional = playShareRepository.findById(id);

        if (!playShareOptional.isPresent()) {
            throw new RuntimeException("PlayShare with ID " + id + " not found");
        }

        PlayShare playShare = playShareOptional.get();

        // Lấy danh sách người tham gia từ PlayShare
        List<UserModel> participants = playShare.getParticipants();

        // Xóa PlayShare khỏi danh sách playShares của từng người dùng tham gia
        for (UserModel user : participants) {
            // Xóa PlayShare khỏi danh sách playShares của người dùng
            user.getPlayShares().remove(playShare);

            // Lưu lại người dùng sau khi xóa PlayShare
            userRepository.save(user);
        }

        // Xóa PlayShare khỏi cơ sở dữ liệu
        playShareRepository.deleteById(id);
    }

}
