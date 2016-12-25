package yuown.pos.repository;

import yuown.pos.domain.Level;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Level entity.
 */
@SuppressWarnings("unused")
public interface LevelRepository extends JpaRepository<Level,Long> {

}
