package com.likith.PhoenixHotel.service.interfac;

import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.dto.LoginRequest;
import com.likith.PhoenixHotel.entity.User;

public interface UserService {

    ResponseDTO register(User user);

    ResponseDTO login(LoginRequest loginRequest);

    ResponseDTO getAllUsers();

    ResponseDTO getUserBookingHistory(String userId);

    ResponseDTO deleteUser(String userId);

    ResponseDTO getUserById(String userId);

    ResponseDTO getMyInfo(String email);
}
