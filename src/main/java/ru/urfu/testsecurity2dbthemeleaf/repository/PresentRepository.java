package ru.urfu.testsecurity2dbthemeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.testsecurity2dbthemeleaf.entity.Present;

@Repository
public interface PresentRepository extends JpaRepository<Present, Long> {
}
