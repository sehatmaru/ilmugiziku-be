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
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.DiscussionVideoMapper;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class DiscussionVideoService {

   @Autowired private DiscussionVideoRepository discussionVideoRepository;
   @Autowired private TemplateRepository templateRepository;

   private final DiscussionVideoMapper discussionVideoMapper = new DiscussionVideoMapper();

   public BaseResponse<DiscussionVideoResponse> getDiscussionVideo(String templateSecureId) {
      BaseResponse<DiscussionVideoResponse> response = new BaseResponse<>();

      TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(templateSecureId);

      if (templateModel != null) {
         try {
            DiscussionVideoModel model = discussionVideoRepository.findByTemplateSecureIdAndDeletedAtIsNull(templateSecureId);

            response.setSuccess(discussionVideoMapper.modelToResponse(model));
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createDiscussionVideo(CreateDiscussionVideoRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      TemplateModel templateModel = templateRepository.findBySecureIdAndDeletedAtIsNull(request.getTemplateSecureId());

      if (templateModel != null) {
         try {
            DiscussionVideoModel model = discussionVideoMapper.createRequestToModel(request);
            discussionVideoRepository.save(model);

            createResponse.setSecureId(model.getSecureId());

            response.setSuccess(createResponse);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateDiscussionVideo(UpdateDiscussionVideoRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      DiscussionVideoModel model = discussionVideoRepository.findBySecureIdAndDeletedAtIsNull(request.getSecureId());

      try {
         discussionVideoRepository.save(discussionVideoMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteDiscussionVideo(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      DiscussionVideoModel model = discussionVideoRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            discussionVideoRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

}
