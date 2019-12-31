package com.msa.study.meeching.repository;

import com.msa.study.meeching.domain.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

}
