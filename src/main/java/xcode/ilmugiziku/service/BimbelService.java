package xcode.ilmugiziku.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.LessonModel;
import xcode.ilmugiziku.domain.model.WebinarModel;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelInformationResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelResponse;
import xcode.ilmugiziku.mapper.LessonMapper;
import xcode.ilmugiziku.mapper.WebinarMapper;
import xcode.ilmugiziku.presenter.BimbelPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;

@Service
public class BimbelService implements BimbelPresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;
   private final WebinarService webinarService;
   private final LessonService lessonService;
   private final JavaMailSender javaMailSender;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final LessonMapper lessonMapper = new LessonMapper();

   public BimbelService(AuthTokenService authTokenService,
                        AuthService authService,
                        WebinarService webinarService,
                        LessonService lessonService,
                        JavaMailSender javaMailSender) {
      this.authTokenService = authTokenService;
      this.authService = authService;
      this.webinarService = webinarService;
      this.lessonService = lessonService;
      this.javaMailSender = javaMailSender;
   }

   @Override
   public BaseResponse<BimbelResponse> getBimbel(String token, int bimbelType) {
      BaseResponse<BimbelResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (bimbelType == UKOM || bimbelType == SKB_GIZI) {
            BimbelResponse result = new BimbelResponse();

            List<WebinarModel> webinars = webinarService.getWebinarByBimbelType(bimbelType);
            List<LessonModel> lessons = lessonService.getLessonByBimbelType(bimbelType);

            for (WebinarModel webinar: webinars) {
               result.getWebinars().add(webinarMapper.modelToResponse(webinar));
            }

            for (LessonModel lesson: lessons) {
               result.getLessons().add(lessonMapper.modelToResponse(lesson));
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

   @Override
   public BaseResponse<BimbelInformationResponse> getBimbelInformation(String token) {
      BaseResponse<BimbelInformationResponse> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthTokenModel authTokenModel = authTokenService.getAuthTokenByToken(token);
         AuthModel authModel = authService.getAuthBySecureId(authTokenModel.getAuthSecureId());

         response.setSuccess(new BimbelInformationResponse(authModel.isUKOMPackage(), authModel.isSKBPackage()));
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
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
}
