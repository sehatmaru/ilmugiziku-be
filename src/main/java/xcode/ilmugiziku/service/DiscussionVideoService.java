package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.DiscussionVideoModel;
import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.repository.DiscussionVideoRepository;
import xcode.ilmugiziku.domain.repository.TemplateRepository;
import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.DiscussionVideoResponse;
import xcode.ilmugiziku.mapper.DiscussionVideoMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class DiscussionVideoService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private DiscussionVideoRepository discussionVideoRepository;
   @Autowired private TemplateRepository templateRepository;

   private final DiscussionVideoMapper discussionVideoMapper = new DiscussionVideoMapper();

   public BaseResponse<DiscussionVideoResponse> getDiscussionVideo(String token, String templateSecureId) {
      BaseResponse<DiscussionVideoResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId);

         if (templateModel != null) {
            try {
               DiscussionVideoModel model = discussionVideoRepository.findByTemplateSecureIdAndDeletedAtIsNull(templateSecureId);

               response.setSuccess(discussionVideoMapper.modelToResponse(model));
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createDiscussionVideo(String token, CreateDiscussionVideoRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(request.getTemplateSecureId());

         if (templateModel != null) {
            if (request.validate()) {
               try {
                  DiscussionVideoModel model = discussionVideoMapper.createRequestToModel(request);
                  discussionVideoRepository.save(model);

                  createResponse.setSecureId(model.getSecureId());

                  response.setSuccess(createResponse);
               } catch (Exception e){
                  response.setFailed(e.toString());
               }
            } else {
               response.setWrongParams();
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateDiscussionVideo(String token, UpdateDiscussionVideoRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         DiscussionVideoModel model = discussionVideoRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());

         try {
            discussionVideoRepository.save(discussionVideoMapper.updateRequestToModel(model, request));

            response.setSuccess(true);
         } catch (Exception e){
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> deleteDiscussionVideo(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         DiscussionVideoModel model = discussionVideoRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               discussionVideoRepository.save(model);

               response.setSuccess(true);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

}
