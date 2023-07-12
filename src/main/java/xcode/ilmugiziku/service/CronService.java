package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.CronJobTypeEnum;
import xcode.ilmugiziku.domain.enums.InvoiceStatusEnum;
import xcode.ilmugiziku.domain.model.*;
import xcode.ilmugiziku.domain.repository.*;
import xcode.ilmugiziku.exception.AppException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.USER_NOT_FOUND_MESSAGE;

/**
 * this is cron job service,
 * please comment all service
 * when running on dev
 */
@Service
public class CronService {

   @Autowired private JavaMailSender javaMailSender;
   @Autowired private ProfileService profileService;
   @Autowired private UserWebinarRepository userWebinarRepository;
   @Autowired private WebinarRepository webinarRepository;
   @Autowired private UserRepository userRepository;
   @Autowired private CronJobRepository cronJobRepository;
   @Autowired private UserCourseRepository userCourseRepository;
   @Autowired private InvoiceRepository invoiceRepository;

   /**
    * will send webinar reminders
    * execute every 30 minutes
    */
   @Scheduled(cron = "0 */30 * * * *")
   public void sendWebinarReminders() {
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

               if (user == null) throw new AppException(USER_NOT_FOUND_MESSAGE);

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

   /**
    * will check all expired user course
    * execute at 1 am every day
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


   /**
    * will check all expired invoice
    * execute at 1 am every day
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void checkExpiredInvoice() {
      CronJobModel cronJobModel = new CronJobModel(CronJobTypeEnum.CHECKING_EXPIRED_PAYMENT);

      try {
         List<InvoiceModel> pendingInvoices = invoiceRepository.getAllPendingInvoice();

         int totalEffectedData = 0;

         for (InvoiceModel invoice: pendingInvoices) {
            if (invoice.getInvoiceDeadline().before(new Date())) {
               invoice.setDeletedAt(new Date());
               invoice.setInvoiceStatus(InvoiceStatusEnum.EXPIRED);

               totalEffectedData += 1;
            }
         }

         invoiceRepository.saveAll(pendingInvoices);

         cronJobModel.setSuccess(true);
         cronJobModel.setTotalEffectedData(totalEffectedData);
      } catch (Exception e) {
         cronJobModel.setDescription(e.toString());
      }

      cronJobRepository.save(cronJobModel);
   }

}
