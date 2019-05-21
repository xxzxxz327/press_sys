package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Voucher;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher,Integer>{

	Page<Voucher> findById(int id,Pageable pageable);
	@Query(value = "select * from voucher where voucher.sum=?1 and voucher.flag=?2 and id!=-1",nativeQuery = true)
	Page<Voucher> findBySumAndFlag(int sum,int flag,Pageable pageable);
	Page<Voucher> findAll(Pageable pageable);

	@Query(value = "select * from voucher where flag=?1 and id!=-1",nativeQuery = true)
	Page<Voucher> findListByFlag(int flag,Pageable pageable);

	@Query(value = "select * from voucher where flag=0 and id!=-1",nativeQuery = true)
	List<Voucher> findListByFlagUnuse();
	
	Voucher findById(int id);
	
}
