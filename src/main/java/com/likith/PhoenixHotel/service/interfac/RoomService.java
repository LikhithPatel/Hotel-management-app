package com.likith.PhoenixHotel.service.interfac;

import com.amazonaws.Response;
import com.likith.PhoenixHotel.dto.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    ResponseDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    List<String> getAllRoomTypes();

    ResponseDTO getAllRooms();

    ResponseDTO deleteRoom(Long roomId);

    ResponseDTO updateRoom(Long roomId,String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    ResponseDTO getRoomById(Long roomId);

    ResponseDTO getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    ResponseDTO getAllAvailableRooms();
}
