package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.enums.RoleEnum;
import xcode.marsiajar.domain.model.ProfileModel;
import xcode.marsiajar.domain.model.UserModel;
import xcode.marsiajar.domain.repository.UserRepository;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.user.UserResponse;
import xcode.marsiajar.exception.AppException;
import xcode.marsiajar.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static xcode.marsiajar.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class UserService {

   @Autowired private ProfileService profileService;
   @Autowired private UserRepository userRepository;

   private final UserMapper userMapper = new UserMapper();

   public BaseResponse<List<UserResponse>> getUserList(RoleEnum role, String name, String email, String registrationType, String status) {
      BaseResponse<List<UserResponse>> response = new BaseResponse<>();

      try {
         List<UserModel> models = userRepository.findByRoleAndDeletedAtIsNullOrderByCreatedAtDesc(role);
         List<UserResponse> responses = userMapper.loginModelsToLoginResponses(models);

         for (UserResponse user : responses) {
            ProfileModel profile = profileService.getUserProfile(user.getSecureId());
            user.setName(profile.getFullName());
         }

         responses = responses.stream()
                 .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                 .filter(e -> e.getEmail().toLowerCase().contains(email.toLowerCase()))
                 .collect(Collectors.toList());

         if (!status.isEmpty()) {
            boolean active = Objects.equals(status, "active");

            responses = responses.stream()
                    .filter(e -> e.isActive() == active)
                    .collect(Collectors.toList());
         }

         if (!registrationType.isEmpty()) {
            responses = responses.stream()
                    .filter(e -> e.getType().name().equalsIgnoreCase(registrationType))
                    .collect(Collectors.toList());
         }

         response.setSuccess(responses);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> toggleStatus(String secureId, boolean status) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      UserModel model = userRepository.findBySecureId(secureId);

      if (model == null) {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      try {
         model.setActive(status);

         userRepository.save(model);

         response.setSuccess(true);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

}
