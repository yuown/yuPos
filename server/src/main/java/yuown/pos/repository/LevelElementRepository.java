package yuown.pos.repository;

import yuown.pos.domain.LevelElement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LevelElement entity.
 */
@SuppressWarnings("unused")
public interface LevelElementRepository extends JpaRepository<LevelElement,Long> {

}
