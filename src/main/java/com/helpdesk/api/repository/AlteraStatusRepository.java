package com.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.api.entity.AlteraStatus;

@Repository("alteraStatusRepository")
public interface AlteraStatusRepository extends MongoRepository<AlteraStatus, String>{
	
	Iterable<AlteraStatus> findByTicketIdOrderByDataAlteracaoStatusDesc(String id);

}
