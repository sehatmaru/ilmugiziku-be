package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.request.course.BenefitRequest;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.domain.response.course.CourseBenefitResponse;
import xcode.ilmugiziku.domain.response.course.CourseListResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.BenefitMapper;
import xcode.ilmugiziku.mapper.CourseMapper;
import xcode.ilmugiziku.mapper.InvoiceMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.LearningTypeEnum.COURSE;
import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class CourseService {

   @Autowired private InvoiceService invoiceService;
   @Autowired private RatingService ratingService;
   @Autowired private CourseBenefitService courseBenefitService;
   @Autowired private CourseRepository courseRepository;
   @Autowired private BenefitRepository benefitRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private CourseBenefitRepository courseBenefitRepository;
   @Autowired private InvoiceRepository invoiceRepository;
   @Autowired private RatingRepository ratingRepository;

   private final CourseMapper courseMapper = new CourseMapper();
   private final BenefitMapper benefitMapper = new BenefitMapper();
   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   /**
    * get courses list
    * benefits included
    * @return course list
    */
   public BaseResponse<List<CourseListResponse>> getCourseList() {
      BaseResponse<List<CourseListResponse>> response = new BaseResponse<>();

      try {
         List<CourseModel> models = courseRepository.findByDeletedAtIsNull();
         List<CourseListResponse> responses = courseMapper.modelsToListResponses(models);
//         responses.forEach(e -> e.setBenefits(benefitMapper.benefitsToResponses(courseBenefitService.getCourseBenefits(e.getSecureId()))));

         response.setSuccess(responses);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * get course detail
    * benefits included
    * @param secureId is course secure id
    * @return course detail
    */
   public BaseResponse<CourseResponse> getCourse(String secureId) {
      BaseResponse<CourseResponse> response = new BaseResponse<>();

      CourseModel model = courseRepository.findBySecureIdAndDeletedAtIsNull(secureId);
      CourseResponse result = courseMapper.modelToResponse(model);

      for (CourseBenefitResponse feature: result.getBenefits()) {
         BenefitModel featureModel = benefitRepository.findBySecureIdAndDeletedAtIsNull(feature.getSecureId());

         feature.setDesc(featureModel.getDescription());
      }

      if (model != null) {
         response.setSuccess(result);
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   /**
    * create course
    * include save the benefits
    * to t_course_benefit_rel
    * @param request body
    * @return response
    */
   public BaseResponse<CreateBaseResponse> createCourse(CreateUpdateCourseRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         CourseModel model = courseMapper.createRequestToModel(request);
         courseRepository.save(model);
         courseBenefitRepository.saveAll(benefitMapper.benefitRequestToCourseBenefitModels(request.getBenefits(), model.getSecureId()));

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * update the course,
    * set 'deleted' = true to the previous
    * course-benefit relation on t_course_benefit_rel,
    * then insert the new relation
    * @param secureId is course secure id
    * @param request body
    * @return boolean
    */
   public BaseResponse<Boolean> updateCourse(String secureId, CreateUpdateCourseRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      CourseModel model = courseRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);

      try {
         courseRepository.save(courseMapper.updateRequestToModel(model, request));

         List<CourseBenefitRelModel> previousCourseBenefitModel = courseBenefitRepository.getCourseBenefits(secureId);
         checkPreviousCourseBenefit(previousCourseBenefitModel, request.getBenefits());
         checkNewCourseBenefit(previousCourseBenefitModel, request.getBenefits(), secureId);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * to check the previous relation
    * used in {@link #updateCourse}
    * @param models = course-benefit rel list
    * @param benefits = new benefit list
    */
   private void checkPreviousCourseBenefit(List<CourseBenefitRelModel> models, List<BenefitRequest> benefits) {
      for (CourseBenefitRelModel prev : models) {
         boolean isAdded = false;

         for (BenefitRequest benefit : benefits) {
            if (prev.getBenefit().equals(benefit.getSecureId())) {
               isAdded = true;
               break;
            }
         }

         if (isAdded) {
            prev.setDeleted(true);
            courseBenefitRepository.save(prev);
         }
      }
   }

   /**
    * to register new course-benefit relation
    * used in {@link #updateCourse}
    * @param models = existing course-benefit relation list
    * @param benefits = new benefit list
    * @param secureId = course secure id
    */
   private void checkNewCourseBenefit(List<CourseBenefitRelModel> models, List<BenefitRequest> benefits, String secureId) {
      for (BenefitRequest current : benefits) {
         boolean isAdded = false;

         for (CourseBenefitRelModel model : models) {
            if (current.getSecureId().equals(model.getBenefit())) {
               isAdded = true;
               break;
            }
         }

         if (!isAdded) {
            courseBenefitRepository.save(benefitMapper.benefitRequestToCourseBenefitModel(current, secureId));
         }
      }
   }

   /**
    * get all course that user already purchased
    * @return course list
    */
   public BaseResponse<List<CourseResponse>> getUserCourses() {
      BaseResponse<List<CourseResponse>> response = new BaseResponse<>();
      List<CourseResponse> result;

      try {
         List<UserCourseRelModel> userCourseModel = userCourseRepository.getUserActiveCourse(CurrentUser.get().getUserSecureId());

         List<CourseModel> courseModels = new ArrayList<>();
         userCourseModel.forEach(e -> courseModels.add(courseRepository.findBySecureIdAndDeletedAtIsNull(e.getSecureId())));

         result = courseMapper.userCoursesToResponses(courseModels);

         result.forEach(e -> e.setBenefits(benefitMapper.benefitsToResponses(courseBenefitService.getCourseBenefits(e.getSecureId()))));

         response.setSuccess(result);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * cancel the course that already purchased by user
    * @param courseSecureId string
    * @return boolean
    */
   public BaseResponse<Boolean> cancelCourse(String courseSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      UserCourseRelModel userCourse = userCourseRepository.getActiveUserCourseBySecureId(courseSecureId);

      if (userCourse == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);

      try {
         userCourse.setDeleted(true);
         userCourseRepository.save(userCourse);

         response.setSuccess(true);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * purchase a course,
    * its have some validation like
    * if unpaidInvoice != null it means you cant purchase,
    * user must pay his last invoice
    * @param courseSecureId string
    * @param request body
    * @return response
    */
   public BaseResponse<PurchaseResponse> purchase(String courseSecureId, PurchaseRequest request) {
      BaseResponse<PurchaseResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);
      UserCourseRelModel userCourse = userCourseRepository.getActiveUserCourse(CurrentUser.get().getUserSecureId(), courseSecureId);
      InvoiceModel unpaidInvoice = invoiceRepository.getPendingUserCourseInvoice(CurrentUser.get().getUserSecureId(), courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      if (!courseModel.isAvailable()) throw new AppException(INACTIVE_COURSE);
      if (userCourse != null) throw new AppException(USER_COURSE_EXIST);

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
            String userCourseSecureId = generateSecureId();

            PurchaseResponse invoice = invoiceService.createInvoice(userModel, request, null, courseModel, COURSE, invoiceSecureId);

            UserCourseRelModel userCourseModel = new UserCourseRelModel();
            userCourseModel.setSecureId(userCourseSecureId);
            userCourseModel.setUser(CurrentUser.get().getUserSecureId());
            userCourseModel.setCourse(courseSecureId);

            InvoiceModel model = invoiceMapper.createRequestToModel(request ,invoice);
            model.setSecureId(invoiceSecureId);
            model.setUserCourse(userCourseSecureId);
            model.setTotalAmount(courseModel.getPrice());
            model.setInvoiceType(COURSE);

            invoiceRepository.save(model);
            userCourseRepository.save(userCourseModel);

            response.setSuccess(invoice);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      }

      return response;
   }

   /**
    * set the availability of a course
    * used in admin
    * @param courseSecureId string
    * @param isAvailable boolean
    * @return boolean
    */
   public BaseResponse<Boolean> setAvailability(String courseSecureId, boolean isAvailable) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);
      List<InvoiceModel> unpaidInvoice = invoiceRepository.getPendingCourseInvoice(courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);

      if (isAvailable) {
         if (courseModel.isAvailable()) throw new AppException(ACTIVE_COURSE_EXIST);
      } else {
         if (!courseModel.isAvailable()) throw new AppException(INACTIVE_COURSE_EXIST);
         if (!unpaidInvoice.isEmpty()) throw new AppException(UNPAID_INVOICE_EXIST);
      }

      try {
         courseModel.setAvailable(isAvailable);
         courseModel.setUpdatedAt(new Date());
         courseRepository.save(courseModel);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   /**
    * give rating to course,
    * user can only give rating if he
    * registered to the course
    * @param courseSecureId string
    * @param rating int
    * @return boolean
    */
   public BaseResponse<Boolean> giveRating(String courseSecureId, int rating) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);
      RatingModel prevRating = ratingRepository.getCourseRating(courseSecureId, CurrentUser.get().getUserSecureId());
      UserCourseRelModel userCourse = userCourseRepository.getPaidUserCourse(CurrentUser.get().getUserSecureId(), courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      if (prevRating != null) throw new AppException(RATING_EXIST);
      if (userCourse == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
      if (rating < 0 || rating > 5) throw new AppException(PARAMS_ERROR_MESSAGE);

      try {
         RatingModel ratingModel = new RatingModel();
         ratingModel.setRating(rating);
         ratingModel.setCourse(courseSecureId);
         ratingModel.setUser(CurrentUser.get().getUserSecureId());
         ratingModel.setRatedAt(new Date());

         ratingRepository.save(ratingModel);

         List<RatingModel> ratingList = ratingRepository.getAllCourseRating(courseSecureId);
         courseModel.setRating(ratingService.calculateRatings(ratingList));

         courseRepository.save(courseModel);

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

}
