package com.xxzxxz.press.repository;

import java.util.List;

import com.xxzxxz.press.model.RouteGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;






public interface RouteGroupRepository extends JpaRepository<RouteGroup,Integer> {
	@Query("select rg from RouteGroup rg")
	Page<RouteGroup>findList(Pageable pageable);
	RouteGroup findById(int id);
	RouteGroup save(RouteGroup routeGroup);
	void deleteById(int id) ;

	@Query(value = "SELECT p.address FROM route_group as rg left join print as p on rg.area_id=p.area_id WHERE rg.id=?1",nativeQuery = true)
	String findFirstById(int id);

	@Query(value = "SELECT s.address FROM route_group as rg left join send_station as s on rg.area_id=s.area_id WHERE rg.id=?1",nativeQuery = true)
	List findRouteGroupById(int id);

}
