package com.msa.study.meetchating.repository;

import com.msa.study.meetchating.domain.MemberEntity;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

}
