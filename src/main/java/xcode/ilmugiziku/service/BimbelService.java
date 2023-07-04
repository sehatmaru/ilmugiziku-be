package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.BimbelTypeEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.LessonRepository;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
import xcode.ilmugiziku.domain.repository.UserRepository;
import xcode.ilmugiziku.domain.repository.WebinarRepository;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelInformationResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.LessonMapper;
import xcode.ilmugiziku.mapper.WebinarMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.BimbelTypeEnum.SKB_GIZI;
import static xcode.ilmugiziku.domain.enums.BimbelTypeEnum.UKOM;
import static xcode.ilmugiziku.domain.enums.PackageTypeEnum.*;
import static xcode.ilmugiziku.domain.enums.PaymentStatusEnum.PAID;
import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class BimbelService {

   @Autowired private ProfileService profileService;
   @Autowired private JavaMailSender javaMailSender;
   @Autowired private UserRepository userRepository;
   @Autowired private LessonRepository lessonRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private WebinarRepository webinarRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final LessonMapper lessonMapper = new LessonMapper();

   public BaseResponse<BimbelResponse> getBimbel(BimbelTypeEnum bimbelType) {
      BaseResponse<BimbelResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      response.setSuccess(setBimbel(userModel, bimbelType));

      return response;
   }

   private BimbelResponse setBimbel(UserModel userModel, BimbelTypeEnum bimbelType) {
      BimbelResponse result = new BimbelResponse();

      List<LessonModel> lessons = lessonRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);

      for (LessonModel lesson: lessons) {
         result.getLessons().add(lessonMapper.modelToResponse(lesson));
      }

      if (userModel.isAdmin() || (bimbelType == UKOM && userModel.isUKOMExpert()) || (bimbelType == SKB_GIZI && userModel.isSKBExpert())) {
         List<WebinarModel> webinars = webinarRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);

         for (WebinarModel webinar: webinars) {
            result.getWebinars().add(webinarMapper.modelToResponse(webinar));
         }
      }

      return result;
   }

   public BaseResponse<BimbelInformationResponse> getBimbelInformation() {
      BaseResponse<BimbelInformationResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());

      if (userModel.isPremium()) {
         refreshPremiumPackage(userModel);
      }

      response.setSuccess(new BimbelInformationResponse(userModel.isUKOMPackage(), userModel.isSKBPackage()));

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

   public void refreshPremiumPackage(UserModel userModel) {
      if (userModel.isSKBPackage()) {
         PaymentModel paymentModel;

         if (userModel.isSKBExpert()) {
            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), SKB_EXPERT, PAID);
         } else {
            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), SKB_NEWBIE, PAID);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            userModel.setPackages(userModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentRepository.save(paymentModel);
            userRepository.save(userModel);
         }
      }

      if (userModel.isUKOMPackage()) {
         PaymentModel paymentModel;

         if (userModel.isUKOMExpert()) {
            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), UKOM_EXPERT, PAID);
         } else {
            paymentModel = paymentRepository.findByUserSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(userModel.getSecureId(), UKOM_NEWBIE, PAID);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            userModel.setPackages(userModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentRepository.save(paymentModel);
            userRepository.save(userModel);
         }
      }
   }
}
