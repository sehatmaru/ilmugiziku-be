package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.AnswerResponse;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.presenter.QuestionPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.QuestionSubTypeRefs.NONE;
import static xcode.ilmugiziku.shared.refs.QuestionTypeRefs.*;

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

   public void saveByModel(AnswerModel model) {
      answerRepository.save(model);
   }
}
