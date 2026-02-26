package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.UserPreferencesDTO;
import com.arcarshowcaseserver.dto.UserProfileDTO;
import com.arcarshowcaseserver.payload.response.MessageResponse;
import com.arcarshowcaseserver.security.services.UserDetailsImpl;

public interface UserService {
    MessageResponse updateProfile(UserDetailsImpl userDetails, UserPreferencesDTO profileDTO);
    UserProfileDTO getProfile(UserDetailsImpl userDetails);
}
