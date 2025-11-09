package com.lal.b_connect.entity.repository;

import com.lal.b_connect.entity.database.BloodRequest;
import com.lal.b_connect.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodRequestRepo extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByRequesterId(Long requesterId);

    List<BloodRequest> findByDonorIdAndStatus(Long donorId, RequestStatus status);
    List<BloodRequest> findByRequesterIdAndStatus(Long requesterId, RequestStatus status);
}