package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelInformationResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelResponse;
import xcode.ilmugiziku.mapper.LessonMapper;
import xcode.ilmugiziku.mapper.WebinarMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;

@Service
public class BimbelService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private AuthService authService;
   @Autowired private WebinarService webinarService;
   @Autowired private LessonService lessonService;
   @Autowired private JavaMailSender javaMailSender;
   @Autowired private PaymentService paymentService;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final LessonMapper lessonMapper = new LessonMapper();

   public BaseResponse<BimbelResponse> getBimbel(String token, int bimbelType) {
      BaseResponse<BimbelResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getAuthBySecureId(authTokenModel.getAuthSecureId());

         if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
            BimbelResponse result = new BimbelResponse();

            List<LessonModel> lessons = lessonService.getLessonByBimbelType(bimbelType);

            for (LessonModel lesson: lessons) {
               result.getLessons().add(lessonMapper.modelToResponse(lesson));
            }

            if (authModel.isAdmin() || (bimbelType == UKOM && authModel.isUKOMExpert()) || (bimbelType == SKB_GIZI && authModel.isSKBExpert())) {
               List<WebinarModel> webinars = webinarService.getWebinarByBimbelType(bimbelType);

               for (WebinarModel webinar: webinars) {
                  result.getWebinars().add(webinarMapper.modelToResponse(webinar));
               }
            }

            response.setSuccess(result);
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<BimbelInformationResponse> getBimbelInformation(String token) {
      BaseResponse<BimbelInformationResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getAuthBySecureId(authTokenModel.getAuthSecureId());

         if (authModel.isPremium()) {
            refreshPremiumPackage(authModel);
         }

         response.setSuccess(new BimbelInformationResponse(authModel.isUKOMPackage(), authModel.isSKBPackage()));
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> sendWebinarReminder(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getAuthBySecureId(authTokenModel.getAuthSecureId());
         WebinarModel webinarModel = webinarService.getWebinarBySecureId(secureId);

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
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public void refreshPremiumPackage(AuthModel authModel) {
      if (authModel.isSKBPackage()) {
         PaymentModel paymentModel;

         if (authModel.isSKBExpert()) {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), SKB_EXPERT);
         } else {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), SKB_NEWBIE);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentService.savePaymentModel(paymentModel);
            authService.saveAuthModel(authModel);
         }
      }

      if (authModel.isUKOMPackage()) {
         PaymentModel paymentModel;

         if (authModel.isUKOMExpert()) {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), UKOM_EXPERT);
         } else {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), UKOM_NEWBIE);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentService.savePaymentModel(paymentModel);
            authService.saveAuthModel(authModel);
         }
      }
   }
}
