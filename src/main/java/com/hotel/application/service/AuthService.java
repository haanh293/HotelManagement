package com.hotel.application.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hotel.application.port.in.AuthUseCase;
import com.hotel.domain.model.User;
import com.hotel.infrastructure.adapter.out.external.EmailService;
import com.hotel.infrastructure.adapter.out.persistence.entity.EmployeeJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataEmployeeRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataGuestRepository;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements AuthUseCase {

    // Kho lưu token quên mật khẩu tạm thời
    private static final Map<String, String> tokenStore = new HashMap<>();
    
    // Client ID của Google (Đảm bảo khớp với Google Cloud Console)
    private static final String GOOGLE_CLIENT_ID = "735228477144-oqnmhglsfd7hpm0ulv9sqopejc94eck3.apps.googleusercontent.com";

    private final SpringDataUserRepository userRepo;
    private final EmailService emailService;
    private final SpringDataGuestRepository guestRepo;
    private final SpringDataEmployeeRepository employeeRepo;

    public AuthService(SpringDataUserRepository userRepo, 
                       EmailService emailService,
                       SpringDataGuestRepository guestRepo, 
                       SpringDataEmployeeRepository employeeRepo) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.guestRepo = guestRepo;
        this.employeeRepo = employeeRepo;
    }

    // --- 1. ĐĂNG NHẬP THƯỜNG ---
    @Override
    public User login(String username, String password) {
        UserJpaEntity entity = userRepo.findByUsername(username).orElse(null);
        if (entity != null && entity.getPassword().equals(password)) {
            return new User(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
        }
        return null; 
    }

    // --- 2. ĐĂNG KÝ THƯỜNG 
    @Override
    @Transactional 
    public void register(User user) {
        // B1. Kiểm tra trùng tên đăng nhập
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập (Email) đã tồn tại!");
        }

        // B2. Tạo User (Tài khoản)
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        
        //Lấy Role từ input, nếu không có thì mặc định là GUEST ---
        String role = (user.getRole() != null && !user.getRole().isEmpty()) ? user.getRole().toUpperCase() : "GUEST";
        entity.setRole(role); 

        // B3. Lưu User
        UserJpaEntity savedUser = userRepo.save(entity);

        // B4. PHÂN LOẠI ĐỂ TẠO HỒ SƠ (PROFILE)
        if ("STAFF".equals(role)) {
            // --- TẠO EMPLOYEE ---
            EmployeeJpaEntity newEmployee = new EmployeeJpaEntity();
            newEmployee.setUserId(savedUser.getId()); // Liên kết khóa ngoại
            
            // Set giá trị mặc định (Vì lúc đăng ký chưa có thông tin chi tiết)
            newEmployee.setFullName(user.getUsername()); 
            newEmployee.setPosition("Nhân viên mới"); // Mặc định
            newEmployee.setSalary(0.0);               // Mặc định
            newEmployee.setPhoneNumber("");
            
            employeeRepo.save(newEmployee);
            
        } else {
            // --- TẠO GUEST (Mặc định cho GUEST hoặc role khác) ---
            GuestJpaEntity newGuest = new GuestJpaEntity();
            newGuest.setUserId(savedUser.getId());    // Liên kết khóa ngoại
            newGuest.setFullName(user.getUsername()); 
            newGuest.setEmail(user.getUsername());
            newGuest.setPhoneNumber("");
            newGuest.setAddress("");
            
            guestRepo.save(newGuest);
        }
    }

    // --- 3. QUÊN MẬT KHẨU (Gửi Email + Trả về mã) ---
    @Override
    public String forgotPassword(String email) {
        if (userRepo.findByUsername(email).isEmpty()) {
            throw new RuntimeException("Email này chưa đăng ký tài khoản!");
        }

        String token = String.valueOf((int) (Math.random() * 9000) + 1000);
        tokenStore.put(email, token);

        // Gửi Email thật
        try {
            String subject = "Xác nhận Quên Mật Khẩu - Hotel Imperial";
            String body = "Mã xác nhận của bạn là: " + token + "\nMã có hiệu lực 5 phút.";
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            System.err.println("Lỗi gửi mail: " + e.getMessage());
        }

        // Trả về token để tiện test (Frontend có thể dùng hoặc bỏ qua)
        return token;
    }

    // --- 4. ĐẶT LẠI MẬT KHẨU ---
    @Override
    public void resetPassword(String email, String token, String newPassword) {
        String storedToken = tokenStore.get(email);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new RuntimeException("Mã xác nhận không đúng hoặc đã hết hạn!");
        }

        UserJpaEntity user = userRepo.findByUsername(email).get();
        user.setPassword(newPassword);
        userRepo.save(user);
        tokenStore.remove(email);
    }

    // --- 5. ĐĂNG NHẬP GOOGLE (Đã bật xác thực thật) ---
    @Override
    @Transactional
    public User loginWithGoogle(String credential) {
        try {
            // Cấu hình bộ xác thực Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            // Xác thực Token từ Frontend gửi lên
            GoogleIdToken idToken = verifier.verify(credential);
            
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // B1: Xử lý User (Tài khoản)
                UserJpaEntity userEntity = userRepo.findByUsername(email).orElse(null);
                if (userEntity == null) {
                    userEntity = new UserJpaEntity();
                    userEntity.setUsername(email);
                    userEntity.setPassword("GOOGLE_LOGIN_AUTO");
                    userEntity.setRole("GUEST");
                    userEntity = userRepo.save(userEntity);
                }   
                
                // B2: Xử lý Guest (Hồ sơ) - Tìm theo UserID
                GuestJpaEntity guestEntity = guestRepo.findByUserId(userEntity.getId()).orElse(null);
                
                if (guestEntity == null) {
                    guestEntity = new GuestJpaEntity();
                    guestEntity.setFullName(name);
                    guestEntity.setEmail(email);
                    guestEntity.setUserId(userEntity.getId()); // Liên kết ID
                    guestEntity.setPhoneNumber("");
                    guestEntity.setAddress("");
                    
                    guestRepo.saveAndFlush(guestEntity); // Lưu ngay lập tức
                }

                // B3: Trả về kết quả
                return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole());

            } else {
                throw new RuntimeException("Token Google không hợp lệ!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage());
        }
    }
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword) {
        // B1. Kiểm tra xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp!");
        }

        // B2. Tìm người dùng trong DB
        UserJpaEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // B3. Kiểm tra mật khẩu cũ (So sánh chuỗi thường)
        // Lưu ý: Nếu sau này bạn dùng BCrypt, chỗ này phải dùng passwordEncoder.matches()
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng!");
        }

        // B4. Cập nhật và lưu
        user.setPassword(newPassword);
        userRepo.save(user);
    }
}