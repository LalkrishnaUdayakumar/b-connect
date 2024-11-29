package com.lal.b_connect.entity.repository;


import com.lal.b_connect.entity.database.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo>,
        PagingAndSortingRepository<UserInfo, Long> {
    UserInfo findByPhoneNumber(String phoneNumber);

    List<UserInfo> findByBloodGroup(String bloodGroup);
}
