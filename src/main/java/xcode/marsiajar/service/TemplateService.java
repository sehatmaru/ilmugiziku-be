package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.model.QuestionModel;
import xcode.marsiajar.domain.model.TemplateModel;
import xcode.marsiajar.domain.model.TemplateQuestionRelModel;
import xcode.marsiajar.domain.repository.QuestionRepository;
import xcode.marsiajar.domain.repository.TemplateQuestionRepository;
import xcode.marsiajar.domain.repository.TemplateRepository;
import xcode.marsiajar.domain.request.BaseRequest;
import xcode.marsiajar.domain.request.template.CreateUpdateTemplateRequest;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.CreateBaseResponse;
import xcode.marsiajar.domain.response.TemplateResponse;
import xcode.marsiajar.exception.AppException;
import xcode.marsiajar.mapper.TemplateMapper;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static xcode.marsiajar.shared.ResponseCode.NOT_FOUND_MESSAGE;
import static xcode.marsiajar.shared.Utils.generateSecureId;

@Service
public class TemplateService {

   @Autowired private TemplateRepository templateRepository;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private TemplateQuestionRepository templateQuestionRepository;

   private final TemplateMapper templateMapper = new TemplateMapper();

   public BaseResponse<List<TemplateResponse>> getTemplateList(String name) {
      BaseResponse<List<TemplateResponse>> response = new BaseResponse<>();

      try {
         List<TemplateModel> models = templateRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
         models = models.stream()
                 .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                 .collect(Collectors.toList());

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
