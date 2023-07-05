package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.ProfileModel;
import xcode.ilmugiziku.domain.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired private ProfileRepository profileRepository;

    public String getUserFullName() {
        return profileRepository.getProfileBySecureId(CurrentUser.get().getUserSecureId()).map(ProfileModel::getFullName).orElse("");
    }

    public String getUserFullName(String secureId) {
        return profileRepository.getProfileBySecureId(secureId).map(ProfileModel::getFullName).orElse("");
    }
}
