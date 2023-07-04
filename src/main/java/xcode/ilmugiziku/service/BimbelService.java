package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.LessonRepository;
import xcode.ilmugiziku.domain.repository.PaymentRepository;
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

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.PaymentStatusRefs.PAID;

@Service
public class BimbelService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private JavaMailSender javaMailSender;
   @Autowired private AuthRepository authRepository;
   @Autowired private LessonRepository lessonRepository;
   @Autowired private PaymentRepository paymentRepository;
   @Autowired private WebinarRepository webinarRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final LessonMapper lessonMapper = new LessonMapper();

   public BaseResponse<BimbelResponse> getBimbel(String token, int bimbelType) {
      BaseResponse<BimbelResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authRepository.findBySecureId(authTokenModel.getAuthSecureId());

         if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
            response.setSuccess(setBimbel(authModel, bimbelType));
         } else {
            throw new AppException(PARAMS_ERROR_MESSAGE);
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   private BimbelResponse setBimbel(AuthModel authModel, int bimbelType) {
      BimbelResponse result = new BimbelResponse();

      List<LessonModel> lessons = lessonRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);

      for (LessonModel lesson: lessons) {
         result.getLessons().add(lessonMapper.modelToResponse(lesson));
      }

      if (authModel.isAdmin() || (bimbelType == UKOM && authModel.isUKOMExpert()) || (bimbelType == SKB_GIZI && authModel.isSKBExpert())) {
         List<WebinarModel> webinars = webinarRepository.findAllByBimbelTypeAndDeletedAtIsNull(bimbelType);

         for (WebinarModel webinar: webinars) {
            result.getWebinars().add(webinarMapper.modelToResponse(webinar));
         }
      }

      return result;
   }

   public BaseResponse<BimbelInformationResponse> getBimbelInformation(String token) {
      BaseResponse<BimbelInformationResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authRepository.findBySecureId(authTokenModel.getAuthSecureId());

         if (authModel.isPremium()) {
            refreshPremiumPackage(authModel);
         }

         response.setSuccess(new BimbelInformationResponse(authModel.isUKOMPackage(), authModel.isSKBPackage()));
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> sendWebinarReminder(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authRepository.findBySecureId(authTokenModel.getAuthSecureId());
         WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(secureId);

         if (webinarModel != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String date = dateFormat.format(webinarModel.getDate());
            String time = timeFormat.format(webinarModel.getDate());

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(authModel.getEmail());
            msg.setSubject("Zoom Meeting Reminder");
            msg.setText("Halo " + authModel.getFullName() + ",\n\n" +
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
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public void refreshPremiumPackage(AuthModel authModel) {
      if (authModel.isSKBPackage()) {
         PaymentModel paymentModel;

         if (authModel.isSKBExpert()) {
            paymentModel = paymentRepository.findByAuthSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(authModel.getSecureId(), SKB_EXPERT, PAID);
         } else {
            paymentModel = paymentRepository.findByAuthSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(authModel.getSecureId(), SKB_NEWBIE, PAID);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentRepository.save(paymentModel);
            authRepository.save(authModel);
         }
      }

      if (authModel.isUKOMPackage()) {
         PaymentModel paymentModel;

         if (authModel.isUKOMExpert()) {
            paymentModel = paymentRepository.findByAuthSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(authModel.getSecureId(), UKOM_EXPERT, PAID);
         } else {
            paymentModel = paymentRepository.findByAuthSecureIdAndPackageTypeAndPaymentStatusAndDeletedAtIsNull(authModel.getSecureId(), UKOM_NEWBIE, PAID);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentRepository.save(paymentModel);
            authRepository.save(authModel);
         }
      }
   }
}
