package yuown.pos.repository;

import yuown.pos.domain.MultiList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultiList entity.
 */
@SuppressWarnings("unused")
public interface MultiListRepository extends JpaRepository<MultiList,Long> {

}
