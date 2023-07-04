package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ProfileModel;
import xcode.ilmugiziku.domain.request.user.RegisterRequest;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ProfileMapper {
//    public ProfileModel editModel(ProfileModel model, EditProfileRequest request) {
//        if (model != null && request != null) {
//            model.setPhone(request.getPhone());
//            model.setEmail(request.getEmail());
//            model.setFullName(request.getFullName());
//            model.setUpdatedAt(new Date());
//
//            return model;
//        } else {
//            return null;
//        }
//    }

    public ProfileModel registerRequestToProfileModel(RegisterRequest request, String user) {
        if (request != null) {
            ProfileModel model = new ProfileModel();
            model.setSecureId(generateSecureId());
            model.setUser(user);
            model.setEmail(request.getEmail());
            model.setFirstName(request.getFirstName());
            model.setLastName(request.getLastName());
            model.setGender(request.getGender());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

}
