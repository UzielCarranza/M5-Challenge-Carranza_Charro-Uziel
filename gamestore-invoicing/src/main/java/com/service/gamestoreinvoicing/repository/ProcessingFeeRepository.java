package com.service.gamestoreinvoicing.repository;

import com.service.gamestoreinvoicing.model.ProcessingFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingFeeRepository extends JpaRepository<ProcessingFee, String> {
}
