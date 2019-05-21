package com.xxzxxz.press.repository;

import java.util.List;

import com.xxzxxz.press.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;




public interface RouteRepository extends JpaRepository<Route,Integer> {

	@Query("select r from Route r")
	Page<Route>findList(Pageable pageable);
	Route findById(int id);

	@Query(value = "select p.address from route as r left join print as p on r.print_id = p.id WHERE r.id=?1",nativeQuery = true)
	 String findRouteById(int id);

	@Query(value = "select s.address from route as r left join send_station as s on r.send_id = s.id WHERE r.id=?1",nativeQuery = true)
	String findFirstById(int id);

	Route save(Route route);
	void deleteById(int id) ;


}
