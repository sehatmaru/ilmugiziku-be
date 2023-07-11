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
import xcode.ilmugiziku.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.PurchaseResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.InvoiceMapper;
import xcode.ilmugiziku.mapper.WebinarMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.domain.enums.InvoiceTypeEnum.WEBINAR;
import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

@Service
public class WebinarService {

   @Autowired private JavaMailSender javaMailSender;
   @Autowired private InvoiceService invoiceService;
   @Autowired private ProfileService profileService;
   @Autowired private WebinarRepository webinarRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private UserWebinarRepository userWebinarRepository;
   @Autowired private InvoiceRepository invoiceRepository;
   @Autowired private CronJobRepository cronJobRepository;

   private final WebinarMapper webinarMapper = new WebinarMapper();
   private final InvoiceMapper invoiceMapper = new InvoiceMapper();

   public BaseResponse<List<WebinarResponse>> getWebinarList() {
      BaseResponse<List<WebinarResponse>> response = new BaseResponse<>();

      try {
         List<WebinarModel> models = webinarRepository.findAllByDeletedAtIsNull();

         response.setSuccess(webinarMapper.modelsToResponses(models));
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

   public BaseResponse<PurchaseResponse> purchase(String webinarSecureId, PurchaseRequest request) {
      BaseResponse<PurchaseResponse> response = new BaseResponse<>();

      UserModel userModel = userRepository.findBySecureId(CurrentUser.get().getUserSecureId());
      WebinarModel webinarModel = webinarRepository.findBySecureIdAndDeletedAtIsNull(webinarSecureId);
      UserWebinarRelModel userWebinar = userWebinarRepository.getActiveUserWebinar(CurrentUser.get().getUserSecureId(), webinarSecureId);
      InvoiceModel unpaidInvoice = invoiceRepository.getPendingWebinarInvoice(webinarSecureId);

      if (webinarModel == null) throw new AppException(WEBINAR_NOT_FOUND_MESSAGE);
      if (!webinarModel.isOpen()) throw new AppException(INACTIVE_WEBINAR);
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
    * will send webinar reminders
    * execute every 30 minutes
    * start from 9am to 11pm
    */
   @Scheduled(cron = "0 0/30 9-23 * * ?")
   public void checkExpiredInvoice() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.WEBINAR_REMINDER);

      try {
         List<UserWebinarRelModel> userWebinarList = userWebinarRepository.getAllUpcomingWebinar();

         int totalEffectedData = 0;

         Calendar calendar = Calendar.getInstance();
         calendar.add(Calendar.MINUTE, -30);
         Date thirtyMinutesBeforeNow = calendar.getTime();

         for (UserWebinarRelModel userWebinar: userWebinarList) {
            WebinarModel webinar = webinarRepository.findBySecureIdAndDeletedAtIsNull(userWebinar.getWebinar());

            if (webinar.getDate().after(thirtyMinutesBeforeNow) && webinar.getDate().before(new Date())) {
               UserModel user = userRepository.getActiveUserBySecureId(userWebinar.getUser());

               DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
               DateFormat timeFormat = new SimpleDateFormat("HH:mm");
               String date = dateFormat.format(webinar.getDate());
               String time = timeFormat.format(webinar.getDate());

               SimpleMailMessage msg = new SimpleMailMessage();
               msg.setTo(user.getEmail());
               msg.setSubject("Zoom Meeting Reminder");
               msg.setText("Halo " + profileService.getUserFullName(CurrentUser.get().getUserSecureId()) + ",\n\n" +
                       "Ini adalah reminder untuk kelas webinar anda\n\n" +
                       "Judul: " + webinar.getTitle() + "\n" +
                       "Tanggal: " + date + "\n" +
                       "Waktu: " + time + " WIB\n" +
                       "Link: " + webinar.getLink() + "\n" +
                       "Meeting ID: " + webinar.getMeetingId() + "\n" +
                       "Passcode: " + webinar.getPasscode() + "\n\n" +
                       "Pastikan hadir tepat waktu ya !\n\n" +
                       "Note: Ini adalah email otomatis, jangan reply ke email ini.");

               javaMailSender.send(msg);

               totalEffectedData += 1;
            }
         }

         cronJobModel.setSuccess(true);
         cronJobModel.setTotalEffectedData(totalEffectedData);
      } catch (Exception e) {
         cronJobModel.setDescription(e.toString());
      }

      cronJobRepository.save(cronJobModel);
   }

}
