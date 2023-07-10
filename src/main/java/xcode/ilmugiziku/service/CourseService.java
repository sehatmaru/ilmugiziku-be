package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.PurchaseRequest;
import xcode.ilmugiziku.domain.request.course.BenefitRequest;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.domain.response.course.CourseBenefitResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.BenefitMapper;
import xcode.ilmugiziku.mapper.CourseMapper;
import xcode.ilmugiziku.mapper.InvoiceMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.InvoiceTypeEnum.COURSE;
import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class CourseService {

   @Autowired private JavaMailSender javaMailSender;
   @Autowired private InvoiceService invoiceService;
   @Autowired private ProfileService profileService;
   @Autowired private CourseBenefitService courseBenefitService;
   @Autowired private CourseRepository courseRepository;
   @Autowired private BenefitRepository benefitRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private WebinarRepository webinarRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private CourseBenefitRepository courseBenefitRepository;
   @Autowired private CronJobRepository cronJobRepository;
   @Autowired private InvoiceRepository invoiceRepository;

   private final CourseMapper courseMapper = new CourseMapper();
   private final BenefitMapper benefitMapper = new BenefitMapper();
   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   public BaseResponse<List<CourseResponse>> getCourseList() {
      BaseResponse<List<CourseResponse>> response = new BaseResponse<>();

      try {
         List<CourseModel> models = courseRepository.findByDeletedAtIsNull();
         List<CourseResponse> responses = courseMapper.modelsToResponses(models);

         responses.forEach(e -> e.setBenefits(benefitMapper.benefitsToResponses(courseBenefitService.getCourseBenefits(e.getSecureId()))));

         response.setSuccess(responses);
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

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

   public BaseResponse<Boolean> sendWebinarReminder(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (webinarModel != null) {
         DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
         DateFormat timeFormat = new SimpleDateFormat("HH:mm");
         String date = dateFormat.format(webinarModel.getDate());
         String time = timeFormat.format(webinarModel.getDate());

         SimpleMailMessage msg = new SimpleMailMessage();
         msg.setTo(userModel.getEmail());
         msg.setSubject("Zoom Meeting Reminder");
         msg.setText("Halo " + profileService.getUserFullName(CurrentUser.get().getUserSecureId()) + ",\n\n" +
                 "Ini adalah reminder untuk kelas webinar anda\n\n" +
                 "Judul: " + webinarModel.getTitle() + "\n" +
                 "Tanggal: " + date + "\n" +
                 "Waktu: " + time + " WIB\n" +
                 "Link: " + webinarModel.getLink() + "\n" +
                 "Meeting ID: " + webinarModel.getMeetingId() + "\n" +
                 "Passcode: " + webinarModel.getPasscode() + "\n\n" +
                 "Pastikan hadir tepat waktu ya !\n\n" +
                 "Note: Ini adalah email otomatis, jangan reply ke email ini.");

         javaMailSender.send(msg);

         response.setSuccess(true);
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> cancelCourse(String courseSecureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      UserCourseRelModel userCourse = userCourseRepository.getUserCourseBySecureId(courseSecureId);

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

   public BaseResponse<PurchaseResponse> purchase(String courseSecureId, PurchaseRequest request) {
      BaseResponse<PurchaseResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      CourseModel courseModel = courseRepository.findBySecureIdAndDeletedAtIsNull(courseSecureId);
      UserCourseRelModel userCourse = userCourseRepository.getActiveUserCourse(CurrentUser.get().getUserSecureId(), courseSecureId);
      InvoiceModel unpaidInvoice = invoiceRepository.getPendingCourseInvoice(courseSecureId);

      if (courseModel == null) throw new AppException(COURSE_NOT_FOUND_MESSAGE);
      if (!courseModel.isOpen()) throw new AppException(INACTIVE_COURSE);
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
    * will execute at 1 am every dat
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void refreshActiveCourse() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.CHECKING_COURSE);

      try {
         List<UserCourseRelModel> userCourse = userCourseRepository.getAllActiveCourse();

         int totalEffectedData = 0;

         for (UserCourseRelModel course : userCourse) {
            if (course.getExpireAt().before(new Date())) {
               course.setActive(false);
               totalEffectedData += 1;
            }
         }

         userCourseRepository.saveAll(userCourse);

         cronJobModel.setSuccess(true);
         cronJobModel.setTotalEffectedData(totalEffectedData);
      } catch (Exception e) {
         cronJobModel.setDescription(e.toString());
      }

      cronJobRepository.save(cronJobModel);
   }

}
