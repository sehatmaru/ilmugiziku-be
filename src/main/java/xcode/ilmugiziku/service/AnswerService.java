package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;

import java.util.List;

@Service
public class AnswerService {

   private final AnswerRepository answerRepository;

   public AnswerService(AnswerRepository answerRepository) {
      this.answerRepository = answerRepository;
   }

   public AnswerModel getAnswerBySecureId(String secureId) {
      return answerRepository.findBySecureId(secureId);
   }

   public List<AnswerModel> getAnswerListByQuestionSecureId(String secureId) {
      return answerRepository.findAllByQuestionSecureId(secureId);
   }

   public void save(AnswerModel model) {
      if (model != null) {
         answerRepository.save(model);
      }
   }
}
