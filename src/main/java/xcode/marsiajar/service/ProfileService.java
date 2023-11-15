package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.dto.CurrentUser;
import xcode.marsiajar.domain.model.ProfileModel;
import xcode.marsiajar.domain.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired private ProfileRepository profileRepository;

    public String getUserFullName() {
        return profileRepository.getProfileBySecureId(CurrentUser.get().getUserSecureId()).getFullName();
    }

    public String getUserFullName(String secureId) {
        return profileRepository.getProfileBySecureId(secureId).getFullName();
    }

    public ProfileModel getUserProfile(String secureId) {
        return profileRepository.getProfileBySecureId(secureId);
    }
}
