package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.repository.AnswerRepository;
import xcode.ilmugiziku.domain.repository.ExamRepository;
import xcode.ilmugiziku.domain.repository.QuestionRepository;
import xcode.ilmugiziku.domain.repository.UserRepository;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.exam.ExamKeyResponse;
import xcode.ilmugiziku.mapper.ExamMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

   @Autowired private ProfileService profileService;
   @Autowired private QuestionService questionService;
   @Autowired private TemplateService templateService;
   @Autowired private ExamRepository examRepository;
   @Autowired private AnswerRepository answerRepository;
   @Autowired private QuestionRepository questionRepository;
   @Autowired private UserRepository userRepository;

   private final ExamMapper examMapper = new ExamMapper();

   // TODO: 11/07/23
//   public BaseResponse<CreateExamResponse> submitExam(CreateExamRequest request) {
//      BaseResponse<CreateExamResponse> response = new BaseResponse<>();
//
//      ScheduleModel schedule = scheduleService.getScheduleByDate(new Date());
//
//      if (userRepository.findBySecureIdAndDeletedAtIsNull(CurrentUser.get().getUserSecureId()) != null) {
//         if (request.getQuestionType() == QUIZ) {
//            saveExam(request, response, schedule);
//         } else {
//            if (!isExamExist(schedule.getSecureId(), CurrentUser.get().getUserSecureId(), request.getQuestionType(), request.getQuestionSubType())) {
//               saveExam(request, response, schedule);
//            } else {
//               throw new AppException(EXIST_MESSAGE);
//            }
//         }
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }

   // TODO: 11/07/23
//   private void saveExam(CreateExamRequest request, BaseResponse<CreateExamResponse> response, ScheduleModel schedule) {
//      CreateExamResponse createResponse = examMapper.generateResponse(request.getExams());
//
//      ExamModel model = examMapper.createRequestToModel(request, createResponse);
//      model.setUserSecureId(CurrentUser.get().getUserSecureId());
//      model.setScheduleSecureId(schedule.getSecureId());
//
//      try {
//         examRepository.save(calculateScore(request.getExams(), model));
//         response.setSuccess(createResponse);
//      } catch (Exception e) {
//         throw new AppException(e.toString());
//      }
//   }

   private ExamModel calculateScore(ExamRequest[] exams, ExamModel model) {
      int score = 0;
      int correct = 0;
      int incorrect = 0;

      for (ExamRequest exam: exams) {
         boolean res = false;

         // TODO: 11/07/23
//         for (AnswerModel answer: answerRepository.findAllByQuestionSecureId(exam.getQuestionsSecureId())) {
//            if (answer.isValue() && (answer.getSecureId().equals(exam.getAnswersSecureId()))) {
//               res = true;
//               break;
//            }
//         }

         if (res) correct += 1;
         else incorrect += 1;
      }

      if (correct > 0) {
         double tmp = (float) correct / exams.length;
         score = (int) Math.round(tmp * 100);
      }

      // TODO: 11/07/23  
//      model.setScore(score);
//      model.setCorrect(correct);
//      model.setIncorrect(incorrect);

      return model;
   }

   // TODO: 11/07/23
//   public BaseResponse<List<ExamResultResponse>> getExamResult(QuestionTypeEnum questionType) {
//      BaseResponse<List<ExamResultResponse>> response = new BaseResponse<>();
//
//      ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
//      List<ExamModel> exams = examRepository.findByScheduleSecureIdAndUserSecureIdAndQuestionType(scheduleModel.getSecureId(), CurrentUser.get().getUserSecureId(), questionType);
//
//      if (scheduleModel.getSecureId() != null) {
//         response.setSuccess(examMapper.modelsToResultResponses(exams));
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }

   // TODO: 11/07/23
//   public BaseResponse<List<ExamRankResponse>> getExamRank(QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType) {
//      BaseResponse<List<ExamRankResponse>> response = new BaseResponse<>();
//
//      ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
//      List<ExamModel> exams = examRepository.findByScheduleSecureIdAndQuestionTypeAndQuestionSubTypeOrderByScoreDesc(scheduleModel.getSecureId(), questionType, questionSubType);
//
//      if (scheduleModel.getSecureId() != null) {
//         List<ExamRankResponse> results = examMapper.modelsToRankResponses(exams);
//
//         for (int i=0; i < exams.size(); i++) {
//            results.get(i).setFullName(profileService.getUserFullName(exams.get(i).getUserSecureId()));
//         }
//
//         response.setSuccess(results);
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }

   // TODO: 11/07/23
//   public BaseResponse<List<ExamKeyResponse>> getExamKey(QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType) {
//      BaseResponse<List<ExamKeyResponse>> response = new BaseResponse<>();
//
//      ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
//      ExamModel exam = examRepository.findByScheduleSecureIdAndUserSecureIdAndQuestionTypeAndQuestionSubType(scheduleModel.getSecureId(), CurrentUser.get().getUserSecureId(), questionType, questionSubType);
//
//      if (scheduleModel.getSecureId() != null && exam != null) {
//         response.setSuccess(getExamKeys(exam));
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }
   
   private List<ExamKeyResponse> getExamKeys(ExamModel exam) {
      List<ExamKeyResponse> results = new ArrayList<>();

      // TODO: 11/07/23  
//      for (String questionSecureId: examMapper.stringToArray(exam.getQuestions())) {
//         ExamKeyResponse examKeyResponse = new ExamKeyResponse();
//         QuestionModel questionModel = questionRepository.findBySecureId(questionSecureId);
//         List<AnswerModel> answerModels = answerRepository.findAllByQuestionSecureId(questionSecureId);
//
//         for (AnswerModel model: answerModels) {
//            if (model.isValue()) {
//               examKeyResponse.setAnswer(model.getContent());
//               examKeyResponse.setQuestion(questionModel.getContent());
//               examKeyResponse.setDiscussion(questionModel.getDiscussion());
//            }
//         }
//
//         results.add(examKeyResponse);
//      }
      
      return results;
   }

   // TODO: 11/07/23
//   public BaseResponse<List<ExamInformationResponse>> getExamInformation(QuestionTypeEnum questionType) {
//      BaseResponse<List<ExamInformationResponse>> response = new BaseResponse<>();
//
//      ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
//
//      if (scheduleModel.getSecureId() != null) {
//         if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB) {
//            response.setSuccess(setExamInformation(questionType, scheduleModel));
//         } else {
//            throw new AppException(PARAMS_ERROR_MESSAGE);
//         }
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }
//
//   private List<ExamInformationResponse> setExamInformation(QuestionTypeEnum questionType, ScheduleModel scheduleModel) {
//      List<ExamInformationResponse> result = new ArrayList<>();
//
//      for (int i = 1; i< QuestionSubTypeEnum.values().length+1; i++) {
//         BaseResponse<QuestionResponse> questions = questionService.getTryOutQuestion(questionType, QuestionSubTypeEnum.values()[i], "");
//
//         result.add(new ExamInformationResponse(
//                 QuestionSubTypeEnum.values()[i],
//                 questions.getResult().getExam().size(),
//                 questionType == TRY_OUT_UKOM ? TIME_LIMIT_UKOM : TIME_LIMIT_SKB,
//                 !isExamExist(scheduleModel.getSecureId(), CurrentUser.get().getUserSecureId(), questionType, QuestionSubTypeEnum.values()[i])
//         ));
//      }
//
//      return result;
//   }
//
//   public BaseResponse<List<ExamVideoResponse>> getExamVideo(QuestionTypeEnum questionType) {
//      BaseResponse<List<ExamVideoResponse>> response = new BaseResponse<>();
//
//      ScheduleModel scheduleModel = scheduleService.getScheduleByDate(new Date());
//
//      if (scheduleModel.getSecureId() != null) {
//         if (questionType == TRY_OUT_UKOM || questionType == TRY_OUT_SKB) {
//            response.setSuccess(setExamVideo(scheduleModel, questionType));
//         } else {
//            throw new AppException(PARAMS_ERROR_MESSAGE);
//         }
//      } else {
//         throw new AppException(NOT_FOUND_MESSAGE);
//      }
//
//      return response;
//   }
//
//   private List<ExamVideoResponse> setExamVideo(ScheduleModel scheduleModel, QuestionTypeEnum questionType) {
//      List<ExamVideoResponse> results = new ArrayList<>();
//
//      for (int i=1; i<QuestionSubTypeEnum.values().length+1; i++) {
//         ExamModel exam = examRepository.findByScheduleSecureIdAndUserSecureIdAndQuestionTypeAndQuestionSubType(scheduleModel.getSecureId(), CurrentUser.get().getUserSecureId(), questionType, QuestionSubTypeEnum.values()[i]);
//         TemplateModel template = templateService.getActiveTemplate(questionType, QuestionSubTypeEnum.values()[i]);
//
//         if (exam != null && template != null) {
//            DiscussionVideoModel video = discussionVideoRepository.findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(questionType, QuestionSubTypeEnum.values()[i], template.getSecureId());
//
//            if (video != null) {
//               results.add(new ExamVideoResponse(video.getUri(), QuestionSubTypeEnum.values()[i]));
//            }
//         }
//      }
//
//      return results;
//   }
//
//   private boolean isExamExist(String schedule, String user, QuestionTypeEnum questionType, QuestionSubTypeEnum questionSubType) {
//      return examRepository.findByScheduleSecureIdAndUserSecureIdAndQuestionTypeAndQuestionSubType(schedule, user, questionType, questionSubType) != null;
//   }
}
