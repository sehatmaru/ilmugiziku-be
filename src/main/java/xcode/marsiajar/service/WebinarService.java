package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.dto.CurrentUser;
import xcode.marsiajar.domain.model.*;
import xcode.marsiajar.domain.repository.*;
import xcode.marsiajar.domain.request.PurchaseRequest;
import xcode.marsiajar.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.CreateBaseResponse;
import xcode.marsiajar.domain.response.PurchaseResponse;
import xcode.marsiajar.domain.response.webinar.WebinarListResponse;
import xcode.marsiajar.domain.response.webinar.WebinarResponse;
import xcode.marsiajar.exception.AppException;
import xcode.marsiajar.mapper.InvoiceMapper;
import xcode.marsiajar.mapper.WebinarMapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static xcode.marsiajar.domain.enums.LearningTypeEnum.WEBINAR;
import static xcode.marsiajar.shared.ResponseCode.*;
import static xcode.marsiajar.shared.Utils.generateSecureId;

@Service
public class WebinarService {

   @Autowired private CategoryService categoryService;
   @Autowired private InvoiceService invoiceService;
   @Autowired private RatingService ratingService;
   @Autowired private WebinarRepository webinarRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private UserWebinarRepository userWebinarRepository;
   @Autowired private InvoiceRepository invoiceRepository;
   @Autowired private RatingRepository ratingRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   public BaseResponse<List<WebinarListResponse>> getWebinarList(String title, String status, String categorySecureId) {
      BaseResponse<List<WebinarListResponse>> response = new BaseResponse<>();

      try {
         List<WebinarModel> models = webinarRepository.findAllByDeletedAtIsNullOrderByUpdatedAtDesc();

         models = models.stream()
                 .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                 .collect(Collectors.toList());

         if (!status.isEmpty()) {
            boolean available = Objects.equals(status, "available");

            models = models.stream()
                    .filter(e -> e.isAvailable() == available)
                    .collect(Collectors.toList());
         }

         if (!categorySecureId.isEmpty()) {
            models = models.stream()
                    .filter(e -> e.getCategory().equals(categorySecureId))
                    .collect(Collectors.toList());
         }

         if (!status.isEmpty()) {
            boolean available = Objects.equals(status, "available");

            models = models.stream()
                    .filter(e -> e.isAvailable() == available)
                    .collect(Collectors.toList());
         }

         List<WebinarListResponse> responses = webinarMapper.modelsToListResponses(models);
         responses.forEach(e -> e.setCategory(categoryService.getCategoryName(e.getCategorySecureId())));

         response.setSuccess(responses);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<WebinarResponse> getWebinarDetail(String secureId) {
      BaseResponse<WebinarResponse> response = new BaseResponse<>();

      WebinarModel model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model == null) throw new AppException(NOT_FOUND_MESSAGE);

      try {
         WebinarResponse webinar = webinarMapper.modelToResponse(model);
         webinar.setCategory(categoryService.getCategoryName(webinar.getCategorySecureId()));

         response.setSuccess(webinar);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createWebinar(CreateUpdateWebinarRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         WebinarModel model = webinarMapper.createRequestToModel(request);
         webinarRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateWebinar(String secureId, CreateUpdateWebinarRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      try {
         WebinarModel model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         webinarRepository.save(webinarMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteWebinar(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      WebinarModel model = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            webinarRepository.save(model);

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
    * purchase a webinar,
    * its have some validation like
    * if unpaidInvoice != null it means you cant purchase,
    * user must pay his last invoice
    * @param webinarSecureId string
    * @param request body
    * @return response
    */
   public BaseResponse<PurchaseResponse> purchase(String webinarSecureId, PurchaseRequest request) {
      BaseResponse<PurchaseResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(webinarSecureId);
      UserWebinarRelModel userWebinar = userWebinarRepository.getActiveUserWebinar(CurrentUser.get().getUserSecureId(), webinarSecureId);
      InvoiceModel unpaidInvoice = invoiceRepository.getPendingWebinarInvoice(CurrentUser.get().getUserSecureId(), webinarSecureId);

      if (webinarModel == null) throw new AppException(WEBINAR_NOT_FOUND_MESSAGE);
      if (!webinarModel.isAvailable()) throw new AppException(INACTIVE_WEBINAR);
      if (userWebinar != null) throw new AppException(USER_WEBINAR_EXIST);

      if (unpaidInvoice != null) {
         PurchaseResponse resp = new PurchaseResponse();
         resp.setInvoiceDeadline(unpaidInvoice.getInvoiceDeadline());
         resp.setInvoiceId(unpaidInvoice.getInvoiceId());
         resp.setInvoiceUrl(unpaidInvoice.getInvoiceUrl());

         response.setSuccess(resp);
         response.setMessage(INVOICE_EXIST);
         response.setStatusCode(HttpStatus.CONFLICT.value());
      } else {
         try {
            String invoiceSecureId = generateSecureId();
            String userWebinarSecureId = generateSecureId();

            PurchaseResponse invoice = invoiceService.createInvoice(userModel, request, webinarModel, null, WEBINAR, invoiceSecureId);

            UserWebinarRelModel userWebinarModel = new UserWebinarRelModel();
            userWebinarModel.setSecureId(userWebinarSecureId);
            userWebinarModel.setUser(CurrentUser.get().getUserSecureId());
            userWebinarModel.setWebinar(webinarSecureId);

            InvoiceModel model = invoiceMapper.createRequestToModel(request ,invoice);
            model.setSecureId(invoiceSecureId);
            model.setUserWebinar(userWebinarSecureId);
            model.setTotalAmount(webinarModel.getPrice());
            model.setInvoiceType(WEBINAR);

            invoiceRepository.save(model);
            userWebinarRepository.save(userWebinarModel);

            response.setSuccess(invoice);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      }

      return response;
   }

   /**
    * set the availability of a webinar
    * used in admin
    * @param webinarSecureId string
    * @param isAvailable boolean
    * @return boolean
    */
   public BaseResponse<Boolean> setAvailability(String webinarSecureId, boolean isAvailable) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(webinarSecureId);
      List<InvoiceModel> unpaidInvoice = invoiceRepository.getPendingCourseInvoice(webinarSecureId);

      if (webinarModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);

      if (isAvailable) {
         if (webinarModel.isAvailable()) throw new AppException(ACTIVE_WEBINAR_EXIST);
      } else {
         if (!webinarModel.isAvailable()) throw new AppException(INACTIVE_WEBINAR_EXIST);
         if (!unpaidInvoice.isEmpty()) throw new AppException(UNPAID_INVOICE_EXIST);
      }

      try {
         webinarModel.setAvailable(isAvailable);
         webinarModel.setUpdatedAt(new Date());
         webinarRepository.save(webinarModel);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * give rating to webinar,
    * user can only give rating if he
    * registered to the webinar
    * @param webinarSecureId string
    * @param rating int
    * @return boolean
    */
   public BaseResponse<Boolean> giveRating(String webinarSecureId, int rating) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(webinarSecureId);
      RatingModel prevRating = ratingRepository.getWebinarRating(webinarSecureId, CurrentUser.get().getUserSecureId());
      UserWebinarRelModel userWebinar = userWebinarRepository.getPaidUserWebinar(CurrentUser.get().getUserSecureId(), webinarSecureId);

      if (webinarModel == null) throw new AppException(WEBINAR_NOT_FOUND_MESSAGE);
      if (prevRating != null) throw new AppException(RATING_EXIST);
      if (userWebinar == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
      if (rating < 0 || rating > 5) throw new AppException(PARAMS_ERROR_MESSAGE);

      try {
         RatingModel ratingModel = new RatingModel();
         ratingModel.setSecureId(generateSecureId());
         ratingModel.setRating(rating);
         ratingModel.setWebinar(webinarSecureId);
         ratingModel.setUser(CurrentUser.get().getUserSecureId());
         ratingModel.setRatedAt(new Date());

         ratingRepository.save(ratingModel);

         List<RatingModel> ratingList = ratingRepository.getAllWebinarRating(webinarSecureId);
         webinarModel.setRating(ratingService.calculateRatings(ratingList));

         webinarRepository.save(webinarModel);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

}
