package com.andree.panjaitan.parkeebe.vehicle;

import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;


    public List<VehicleType> getAllVehicleType() {
        return vehicleTypeRepository.findAllByDeletedAtIsNull().stream().toList();
    }

    public List<VehicleType> getAllVehicleTypeInLocation(UUID locationUuid) {
        return vehicleTypeRepository.findAllByLocationID(locationUuid).stream().toList();
    }

    public void createNewVehicleType(VehicleTypeRequest request) {
        var vehicleType = VehicleType
                .builder()
                .name(request.getVehicleName())
                .build();
        vehicleTypeRepository.save(vehicleType);
    }

    public void deleteVehicleType(UUID uuid, User user) {
        var v = vehicleTypeRepository.findById(uuid)
                .orElseThrow(() -> new ErrorAppException(CodeError.NOT_FOUND.getCodeError(), "vehicle not found"));
        v.setDeletedAt(LocalDateTime.now());
        v.setDeletedBy(user.getUsername());
    }
}
