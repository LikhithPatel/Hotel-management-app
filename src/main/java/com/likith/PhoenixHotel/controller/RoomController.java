package com.likith.PhoenixHotel.controller;

import com.likith.PhoenixHotel.dto.ResponseDTO;
import com.likith.PhoenixHotel.dto.RoomDTO;
import com.likith.PhoenixHotel.service.interfac.BookingService;
import com.likith.PhoenixHotel.service.interfac.RoomService;
import com.likith.PhoenixHotel.service.interfac.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> addNewRoom(@RequestParam(value="photo",required=false) MultipartFile photo,
                                                  @RequestParam(value="roomType",required=false) String roomType,
                                                  @RequestParam(value="roomPrice",required=false) BigDecimal roomPrice,
                                                  @RequestParam(value="roomDescription",required=false) String roomDescription){
        if(photo==null||photo.isEmpty()|| roomType==null||roomType.isEmpty()||roomPrice==null){
            ResponseDTO response=new ResponseDTO();
            response.setStatusCode(400);
            response.setMessage("Please fill all the fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        ResponseDTO response=roomService.addNewRoom(photo,roomType,roomPrice,roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllRooms(){
        ResponseDTO response=roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getAllRoomsTypes(){
        List<String> roomTypes=roomService.getAllRoomTypes();
        return roomTypes;
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<ResponseDTO> getRoomById(@PathVariable("roomId") Long roomId){
        ResponseDTO response=roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<ResponseDTO> getAllAvailableRooms(){
        ResponseDTO response=roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<ResponseDTO> getAvailableRoomsByDateAndType(
            @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required=false) String roomType
    ){

        if(checkInDate==null|| roomType==null||roomType.isEmpty()||checkOutDate==null){
            ResponseDTO response=new ResponseDTO();
            response.setStatusCode(400);
            response.setMessage("Please fill all the fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        ResponseDTO response=roomService.getAvailableRoomsByDataAndType(checkInDate,checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> updateRoom(@PathVariable("roomId") Long roomId,
                                                  @RequestParam(value="photo",required=false) MultipartFile photo,
                                                  @RequestParam(value="roomType",required=false) String roomType,
                                                  @RequestParam(value="roomPrice",required=false) BigDecimal roomPrice,
                                                  @RequestParam(value="roomDescription",required=false) String roomDescription
    ){
        ResponseDTO response=roomService.updateRoom(roomId,roomDescription,roomType,roomPrice,photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteRoom(@PathVariable("roomId") Long roomId){
        ResponseDTO response=roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
