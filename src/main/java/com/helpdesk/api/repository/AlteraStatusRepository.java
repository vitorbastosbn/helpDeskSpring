package com.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.helpdesk.api.entity.AlteraStatus;

public interface AlteraStatusRepository extends MongoRepository<AlteraStatus, String>{
	
	Iterable<AlteraStatus> findByTicketIdOrderByDataAlteracaoStatusDesc(String id);

}
