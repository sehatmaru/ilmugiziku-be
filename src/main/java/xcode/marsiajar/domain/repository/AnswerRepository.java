package xcode.marsiajar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xcode.marsiajar.domain.model.AnswerModel;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerModel, String> {

    @Query(value = "SELECT * FROM t_answer" +
            " WHERE question_secure_id = :question", nativeQuery = true)
    List<AnswerModel> getAnswersByQuestion(String question);

    @Query(value = "SELECT * FROM t_answer" +
            " WHERE question_secure_id = :question" +
            " AND correct_answer IS TRUE" +
            " LIMIT 1", nativeQuery = true)
    AnswerModel getCorrectAnswer(String question);
}
