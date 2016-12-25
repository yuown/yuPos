package yuown.pos.repository;

import yuown.pos.domain.Configuration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Configuration entity.
 */
@SuppressWarnings("unused")
public interface ConfigurationRepository extends JpaRepository<Configuration,Long> {

}
