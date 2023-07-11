package xcode.ilmugiziku.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.ilmugiziku.domain.model.AnswerModel;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerModel, String> {


}
