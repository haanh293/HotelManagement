package com.hotel.application.service;

import com.hotel.application.port.in.AuthUseCase;
import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.external.EmailService;
import com.hotel.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository; 
import java.util.Collections;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class AuthService implements AuthUseCase {

	private static final Map<String, String> tokenStore = new HashMap<>();

    private final SpringDataUserRepository userRepo;
    private final EmailService emailService; // Service gửi mail đã tạo
    private final SpringDataGuestRepository guestRepo;
    public AuthService(SpringDataUserRepository userRepo, 
            			EmailService emailService,
            			SpringDataGuestRepository guestRepo) { // <--- Thêm vào đây
    	this.userRepo = userRepo;
    	this.emailService = emailService;
    	this.guestRepo = guestRepo; // <--- Gán giá trị
}
    private static final String GOOGLE_CLIENT_ID = "735228477144-oqnmhglsfd7hpm0ulv9sqopejc94eck3.apps.googleusercontent.com";
    @Override
    public User login(String username, String password) {
        // 1. Tìm user trong DB
        UserJpaEntity entity = userRepo.findByUsername(username).orElse(null);

        // 2. Kiểm tra mật khẩu (So sánh chuỗi thường cho nhanh)
        if (entity != null && entity.getPassword().equals(password)) {
            return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
        }
        
        // 3. Đăng nhập thất bại
        return null; 
    }
    @Override
    public void register(User user) {
        // 1. Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // 2. Chuyển đổi từ Domain sang Entity
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole()); // Mặc định có thể để là "GUEST" hoặc lấy từ input

        // 3. Lưu vào DB
        userRepo.save(entity);
    }
    @Override
    public String forgotPassword(String email) {
        // B1: Kiểm tra email có tồn tại trong hệ thống không
        if (userRepo.findByUsername(email).isEmpty()) {
            throw new RuntimeException("Email này chưa đăng ký tài khoản!");
        }

        // B2: Tạo mã Token ngẫu nhiên (4 số)
        String token = String.valueOf((int) (Math.random() * 9000) + 1000);

        // B3: Lưu vào bộ nhớ tạm
        tokenStore.put(email, token);

        // B4: Gửi Email thật
        String subject = "Xác nhận Quên Mật Khẩu - Hotel Imperial";
        String body = "Xin chào,\n\n" +
                      "Mã xác nhận của bạn là: " + token + "\n" +
                      "Mã này có hiệu lực trong 5 phút. Vui lòng không chia sẻ cho ai.";
        
        emailService.sendEmail(email, subject, body);

        return token;
    }

    @Override
    public void resetPassword(String email, String token, String newPassword) {
        // B1: Lấy token đã lưu trong RAM ra
        String storedToken = tokenStore.get(email);

        // B2: Kiểm tra khớp mã
        if (storedToken == null || !storedToken.equals(token)) {
            throw new RuntimeException("Mã xác nhận không đúng hoặc đã hết hạn!");
        }

        // B3: Cập nhật mật khẩu mới vào DB
        UserJpaEntity user = userRepo.findByUsername(email).get();
        user.setPassword(newPassword); // (Lưu ý: Nếu có mã hóa BCrypt thì nhớ mã hóa ở đây)
        userRepo.save(user);

        // B4: Xóa token đi (để không dùng lại được nữa)
        tokenStore.remove(email);
    }
    @Override
    public User loginWithGoogle(String credential) {
        try {
            // 1. Cấu hình bộ xác thực của Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            // 2. Xác thực Token gửi từ Front-end
            GoogleIdToken idToken = verifier.verify(credential);
            
            if (idToken != null) {
                // 3. Nếu Token xịn -> Lấy thông tin người dùng
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name"); // Lấy tên nếu cần

                // 4. Kiểm tra xem Email này đã có trong DB chưa
                UserJpaEntity userEntity = userRepo.findByUsername(email).orElse(null);

                if (userEntity == null) {
                    userEntity = new UserJpaEntity();
                    userEntity.setUsername(email);
                    userEntity.setPassword("GOOGLE_LOGIN_AUTO");
                    userEntity.setRole("GUEST");
                    userEntity = userRepo.save(userEntity); // Lưu User mới
                }	
                GuestJpaEntity guestEntity = guestRepo.findByUserId(userEntity.getId()).orElse(null);
                if (guestEntity == null) {
                    // Nếu chưa có Guest -> TẠO NGAY
                    guestEntity = new GuestJpaEntity();
                    guestEntity.setFullName(name);
                    guestEntity.setEmail(email);
                    guestEntity.setUserId(userEntity.getId()); // Quan trọng: Link với User ID
                    guestEntity.setPhoneNumber("");
                    guestEntity.setAddress("");
                    
                    guestRepo.save(guestEntity);
                }

                // 6. Trả về User đã đăng nhập
                return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole());

            } else {
                throw new RuntimeException("Token Google không hợp lệ!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage());
        }
    }
}