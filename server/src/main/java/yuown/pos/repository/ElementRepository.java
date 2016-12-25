package yuown.pos.repository;

import yuown.pos.domain.Element;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Element entity.
 */
@SuppressWarnings("unused")
public interface ElementRepository extends JpaRepository<Element,Long> {

}
