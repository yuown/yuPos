package yuown.pos.repository;

import yuown.pos.domain.ListLevel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ListLevel entity.
 */
@SuppressWarnings("unused")
public interface ListLevelRepository extends JpaRepository<ListLevel,Long> {

}
