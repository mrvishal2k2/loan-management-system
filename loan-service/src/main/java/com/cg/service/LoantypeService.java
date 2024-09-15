package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Loantype;
import com.cg.repository.LoantypeRepository;

@Service
public class LoantypeService {
    @Autowired
    private LoantypeRepository ltype;
    // save loan type
	public Loantype savetype(Loantype type) {
		
		return ltype.save(type);
	}
	
	// display all available loan types
	public List<Loantype> getAllTypes() {
		return ltype.findAll();
	}

}
