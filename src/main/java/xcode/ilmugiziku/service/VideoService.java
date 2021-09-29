package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.VideoModel;
import xcode.ilmugiziku.domain.repository.VideoRepository;
import xcode.ilmugiziku.domain.request.video.CreateVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.mapper.VideoMapper;
import xcode.ilmugiziku.presenter.VideoPresenter;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class VideoService implements VideoPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final AnswerService answerService;
   private final ScheduleService scheduleService;

   private final VideoRepository videoRepository;

   private final VideoMapper videoMapper = new VideoMapper();

   public VideoService(AuthTokenService authTokenService, AnswerService answerService, ScheduleService scheduleService, VideoRepository videoRepository, AuthService authService) {
      this.authTokenService = authTokenService;
      this.videoRepository = videoRepository;
      this.answerService = answerService;
      this.scheduleService = scheduleService;
      this.authService = authService;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createDiscussionVideo(String token, CreateVideoRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            try {
               VideoModel model = videoMapper.createRequestToModel(request);
               videoRepository.save(model);

               createResponse.setSecureId(model.getSecureId());

               response.setSuccess(createResponse);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public VideoModel getDiscussionVideoByQuestionTypeAndQuestionSubTypeAndTemplateSecureId(int questionType, int questionSubType, String templateSecureId) {
      return videoRepository.findByQuestionTypeAndQuestionSubTypeAndTemplateSecureIdAndDeletedAtIsNull(questionType, questionSubType, templateSecureId);
   }

}
