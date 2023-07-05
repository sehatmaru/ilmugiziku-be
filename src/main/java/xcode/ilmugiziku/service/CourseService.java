package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.course.CourseBenefitResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.CourseMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class CourseService {

   @Autowired private JavaMailSender javaMailSender;
   @Autowired private ProfileService profileService;
   @Autowired private CourseRepository courseRepository;
   @Autowired private BenefitRepository benefitRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private LessonRepository lessonRepository;
   @Autowired private WebinarRepository webinarRepository;

   private final CourseMapper courseMapper = new CourseMapper();

   public BaseResponse<List<CourseResponse>> getCourseList() {
      BaseResponse<List<CourseResponse>> response = new BaseResponse<>();

      // TODO: 05/07/23
//      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      List<CourseModel> models = courseRepository.findByDeletedAtIsNull();
      List<CourseResponse> responses = courseMapper.modelsToResponses(models);

      for (CourseResponse resp : responses) {
//         resp.setOpen(!userModel.isPaidCourse(resp.getCourseType()));

         for (CourseBenefitResponse feature: resp.getBenefits()) {
            BenefitModel model = benefitRepository.findBySecureIdAndDeletedAtIsNull(feature.getSecureId());

            feature.setDesc(model.getDescription());
         }
      }

      response.setSuccess(responses);

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

      try {
         courseRepository.save(courseMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   // TODO: 05/07/23
//   public BaseResponse<CourseResponse> getUserCourse(CourseTypeEnum courseType) {
//      BaseResponse<CourseResponse> response = new BaseResponse<>();
//
//      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
//      response.setSuccess(setCourse(userModel, courseType));
//
//      return response;
//   }

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

   // TODO: 05/07/23
//   public void refreshPremiumPackage(UserModel userModel) {
//      if (userModel.isSKBPackage()) {
//         PaymentModel paymentModel;
//
//         if (userModel.isSKBExpert()) {
//            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), SKB_EXPERT, PAID);
//         } else {
//            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), SKB_NEWBIE, PAID);
//         }
//
//         if (paymentModel.getExpiredDate().before(new Date())) {
//            paymentModel.setDeletedAt(new Date());
//            userModel.setPackages(userModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));
//
//            paymentRepository.save(paymentModel);
//            userRepository.save(userModel);
//         }
//      }
//
//      if (userModel.isUKOMPackage()) {
//         PaymentModel paymentModel;
//
//         if (userModel.isUKOMExpert()) {
//            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), UKOM_EXPERT, PAID);
//         } else {
//            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), UKOM_NEWBIE, PAID);
//         }
//
//         if (paymentModel.getExpiredDate().before(new Date())) {
//            paymentModel.setDeletedAt(new Date());
//            userModel.setPackages(userModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));
//
//            paymentRepository.save(paymentModel);
//            userRepository.save(userModel);
//         }
//      }
//   }

}
