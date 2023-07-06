package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.course.BenefitRequest;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.course.CourseBenefitResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.BenefitMapper;
import xcode.ilmugiziku.mapper.CourseMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.COURSE_NOT_FOUND_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class CourseService {

   @Autowired private JavaMailSender javaMailSender;
   @Autowired private ProfileService profileService;
   @Autowired private CourseBenefitService courseBenefitService;
   @Autowired private CourseRepository courseRepository;
   @Autowired private BenefitRepository benefitRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private LessonRepository lessonRepository;
   @Autowired private WebinarRepository webinarRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private CourseBenefitRepository courseBenefitRepository;
   @Autowired private CronJobRepository cronJobRepository;

   private final CourseMapper courseMapper = new CourseMapper();
   private final BenefitMapper benefitMapper = new BenefitMapper();

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

   // TODO: 05/07/23
//   private CourseResponse setCourse(UserModel userModel, CourseTypeEnum courseType) {
//      CourseResponse result = new CourseResponse();
//
//      List<LessonModel> lessons = lessonRepository.findAllByCourseTypeAndDeletedAtIsNull(courseType);
//
//      for (LessonModel lesson: lessons) {
//         result.getLessons().add(lessonMapper.modelToResponse(lesson));
//      }
//
//      if (userModel.isAdmin() || (courseType == UKOM && userModel.isUKOMExpert()) || (courseType == SKB && userModel.isSKBExpert())) {
//         List<WebinarModel> webinars = webinarRepository.findAllByCourseTypeAndDeletedAtIsNull(courseType);
//
//         for (WebinarModel webinar: webinars) {
//            result.getWebinars().add(webinarMapper.modelToResponse(webinar));
//         }
//      }
//
//      return result;
//   }

   // TODO: 05/07/23
//   public BaseResponse<CourseInformationResponse> getCourseInformation() {
//      BaseResponse<CourseInformationResponse> response = new BaseResponse<>();
//
//      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
//
//      if (userModel.isPremium()) {
//         refreshPremiumPackage(userModel);
//      }
//
//      response.setSuccess(new CourseInformationResponse(userModel.isUKOMPackage(), userModel.isSKBPackage()));
//
//      return response;
//   }

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

   /**
    * will execute at 1 am every dat
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void refreshActiveCourse() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.CHECKING_COURSE);

      try {
         List<UserCourseRelModel> userCourse = userCourseRepository.getAllActiveCourse(CurrentUser.get().getUserSecureId());

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
