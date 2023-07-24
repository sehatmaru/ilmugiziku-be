package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.model.TemplateQuestionRelModel;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.repository.TemplateQuestionRepository;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.BaseRequest;
import xcode.ilmugiziku.domain.request.template.CreateUpdateTemplateRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TemplateResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.TemplateMapper;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class TemplateService {

   @Autowired private TemplateRepository templateRepository;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private TemplateQuestionRepository templateQuestionRepository;

   private final TemplateMapper templateMapper = new TemplateMapper();

   public BaseResponse<List<TemplateResponse>> getTemplateList() {
      BaseResponse<List<TemplateResponse>> response = new BaseResponse<>();

      try {
         List<TemplateModel> models = templateRepository.findAllByDeletedAtIsNull();

         response.setSuccess(templateMapper.modelsToResponses(models));
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createTemplate(CreateUpdateTemplateRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         TemplateModel model = templateMapper.createRequestToModel(request);
         templateRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateTemplate(String templateSecureId, CreateUpdateTemplateRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         templateRepository.save(templateMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteTemplate(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            templateRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   /**
    * add list of question to template
    * @param templateSecureId string
    * @param request = list of question
    * @return boolean
    */
   @Transactional()
   public BaseResponse<Boolean> setQuestions(String templateSecureId, List<BaseRequest> request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TemplateModel model = templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         templateQuestionRepository.deleteAllByTemplate(templateSecureId);

         request.forEach(e-> {
            QuestionModel question = questionRepository.findBySecureId(e.getSecureId());

            if (question == null) throw new AppException(NOT_FOUND_MESSAGE);

            TemplateQuestionRelModel templateQuestion = new TemplateQuestionRelModel();
            templateQuestion.setSecureId(generateSecureId());
            templateQuestion.setQuestion(question.getSecureId());
            templateQuestion.setTemplate(templateSecureId);

            templateQuestionRepository.save(templateQuestion);
         });

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }


}
