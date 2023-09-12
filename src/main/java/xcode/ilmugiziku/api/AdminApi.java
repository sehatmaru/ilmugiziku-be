package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.enums.RoleEnum;
import xcode.ilmugiziku.domain.request.BaseRequest;
import xcode.ilmugiziku.domain.request.benefit.CreateUpdateBenefitRequest;
import xcode.ilmugiziku.domain.request.category.CreateUpdateCategoryRequest;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.request.exam.CreateUpdateExamRequest;
import xcode.ilmugiziku.domain.request.question.CreateUpdateQuestionRequest;
import xcode.ilmugiziku.domain.request.template.CreateUpdateTemplateRequest;
import xcode.ilmugiziku.domain.request.user.AddUpdateAdminRequest;
import xcode.ilmugiziku.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TemplateResponse;
import xcode.ilmugiziku.domain.response.benefit.BenefitResponse;
import xcode.ilmugiziku.domain.response.category.CategoryResponse;
import xcode.ilmugiziku.domain.response.course.CourseListResponse;
import xcode.ilmugiziku.domain.response.exam.ExamListResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResponse;
import xcode.ilmugiziku.domain.response.invoice.InvoiceListResponse;
import xcode.ilmugiziku.domain.response.question.QuestionListResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;
import xcode.ilmugiziku.domain.response.user.UserResponse;
import xcode.ilmugiziku.domain.response.webinar.WebinarListResponse;
import xcode.ilmugiziku.domain.response.webinar.WebinarResponse;
import xcode.ilmugiziku.service.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "admin")
public class AdminApi {

    @Autowired private ExamService examService;
    @Autowired private QuestionService questionService;
    @Autowired private TemplateService templateService;
    @Autowired private BenefitService benefitService;
    @Autowired private CourseService courseService;
    @Autowired private WebinarService webinarService;
    @Autowired private UserService userService;
    @Autowired private InvoiceService invoiceService;
    @Autowired private AuthService authService;
    @Autowired private CategoryService categoryService;

    @PostMapping("/question/create")
    ResponseEntity<BaseResponse<Boolean>> createQuestion (
            @RequestBody @Validated List<CreateUpdateQuestionRequest> body
    ) {
        BaseResponse<Boolean> response = questionService.createQuestion(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/question/update")
    ResponseEntity<BaseResponse<Boolean>> updateQuestion(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateQuestionRequest body
    ) {
        BaseResponse<Boolean> response = questionService.updateQuestion(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/question/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteQuestion(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = questionService.deleteQuestion(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/question/detail")
    ResponseEntity<BaseResponse<QuestionResponse>> getQuestionDetail(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<QuestionResponse> response = questionService.getQuestionDetail(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/question/list")
    ResponseEntity<BaseResponse<List<QuestionListResponse>>> getQuestionList(
            @RequestParam @Validated String content,
            @RequestParam @Validated String categorySecureId
    ) {
        BaseResponse<List<QuestionListResponse>> response = questionService.getQuestionList(content, categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/template/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createTemplate(
            @RequestBody @Validated CreateUpdateTemplateRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = templateService.createTemplate(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/template/update")
    ResponseEntity<BaseResponse<Boolean>> updateTemplate(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateTemplateRequest body
    ) {
        BaseResponse<Boolean> response = templateService.updateTemplate(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/template/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteTemplate(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = templateService.deleteTemplate(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/template/list")
    ResponseEntity<BaseResponse<List<TemplateResponse>>> getTemplateList(
            @RequestParam String name
    ) {
        BaseResponse<List<TemplateResponse>> response = templateService.getTemplateList(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/template/question")
    ResponseEntity<BaseResponse<Boolean>> setTemplateQuestions(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated List<BaseRequest> request
    ) {
        BaseResponse<Boolean> response = templateService.setQuestions(secureId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/exam/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createExam(
            @RequestBody @Validated CreateUpdateExamRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = examService.createExam(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/exam/update")
    ResponseEntity<BaseResponse<Boolean>> updateExam(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateExamRequest body
    ) {
        BaseResponse<Boolean> response = examService.updateExam(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/exam/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteExam(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = examService.deleteExam(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/exam/list")
    ResponseEntity<BaseResponse<List<ExamListResponse>>> getExamList(
            @RequestParam String title,
            @RequestParam String status,
            @RequestParam String categorySecureId
    ) {
        BaseResponse<List<ExamListResponse>> response = examService.getExamList(title, status, categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/exam/detail")
    ResponseEntity<BaseResponse<ExamResponse>> getExamDetail(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<ExamResponse> response = examService.getExamDetail(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/exam/template")
    ResponseEntity<BaseResponse<Boolean>> setTemplate(
            @RequestParam @Validated String examSecureId,
            @RequestParam @Validated String templateSecureId
    ) {
        BaseResponse<Boolean> response = examService.setTemplate(examSecureId, templateSecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Deprecated
    @GetMapping("/exam/time")
    ResponseEntity<BaseResponse<Boolean>> setTime(
            @RequestParam @Validated String secureId,
            @RequestParam @Validated Date startTime,
            @RequestParam @Validated Date endTime
    ) {
        BaseResponse<Boolean> response = examService.setTime(secureId, startTime, endTime);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/benefit/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createBenefit(
            @RequestBody @Validated CreateUpdateBenefitRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = benefitService.createBenefit(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/benefit/update")
    ResponseEntity<BaseResponse<Boolean>> updateBenefit(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateBenefitRequest body
    ) {
        BaseResponse<Boolean> response = benefitService.updateBenefit(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/benefit/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteBenefit(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = benefitService.deleteBenefit(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/benefit/list")
    ResponseEntity<BaseResponse<List<BenefitResponse>>> getBenefitList(
            @RequestParam String name
    ) {
        BaseResponse<List<BenefitResponse>> response = benefitService.getBenefitList(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/course/update")
    ResponseEntity<BaseResponse<Boolean>> updateCourse(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateCourseRequest body
    ) {
        BaseResponse<Boolean> response = courseService.updateCourse(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/webinar/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createWebinar(
            @RequestBody @Validated CreateUpdateWebinarRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = webinarService.createWebinar(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/webinar/update")
    ResponseEntity<BaseResponse<Boolean>> updateWebinar(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateWebinarRequest body
    ) {
        BaseResponse<Boolean> response = webinarService.updateWebinar(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/webinar/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteWebinar(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = webinarService.deleteWebinar(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/detail")
    ResponseEntity<BaseResponse<WebinarResponse>> getWebinarDetail(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<WebinarResponse> response = webinarService.getWebinarDetail(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/list")
    ResponseEntity<BaseResponse<List<WebinarListResponse>>> getWebinarList(
            @RequestParam String title,
            @RequestParam String status,
            @RequestParam String categorySecureId
    ) {
        BaseResponse<List<WebinarListResponse>> response = webinarService.getWebinarList(title, status, categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/course/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createCourse(
            @RequestBody @Validated CreateUpdateCourseRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = courseService.createCourse(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/course/list")
    ResponseEntity<BaseResponse<List<CourseListResponse>>> getCourseList(
            @RequestParam @Validated String title,
            @RequestParam @Validated String categorySecureId
    ) {
        BaseResponse<List<CourseListResponse>> response = courseService.getCourseList(title, categorySecureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/webinar/availability")
    ResponseEntity<BaseResponse<Boolean>> webinarAvailability(
            @RequestParam @Validated String secureId,
            @RequestParam @Validated boolean isAvailable
    ) {
        BaseResponse<Boolean> response = webinarService.setAvailability(secureId, isAvailable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/course/availability")
    ResponseEntity<BaseResponse<Boolean>> courseAvailability(
            @RequestParam @Validated String secureId,
            @RequestParam @Validated boolean isAvailable
    ) {
        BaseResponse<Boolean> response = courseService.setAvailability(secureId, isAvailable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/user/list")
    ResponseEntity<BaseResponse<List<UserResponse>>> userList(
            @RequestParam @Validated RoleEnum role,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String registrationType,
            @RequestParam String status
    ) {
        BaseResponse<List<UserResponse>> response = userService.getUserList(role, name, email, registrationType, status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/user/status/toggle")
    ResponseEntity<BaseResponse<Boolean>> toggleStatus(
            @RequestParam @Validated String secureId,
            @RequestParam @Validated boolean status
    ) {
        BaseResponse<Boolean> response = userService.toggleStatus(secureId, status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/invoice/list")
    ResponseEntity<BaseResponse<List<InvoiceListResponse>>> invoiceList(
            @RequestParam String status,
            @RequestParam String customerName,
            @RequestParam String invoiceId,
            @RequestParam String category
    ) {
        BaseResponse<List<InvoiceListResponse>> response = invoiceService.list(status, customerName, invoiceId, category);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/user/add")
    ResponseEntity<BaseResponse<CreateBaseResponse>> addAdminUser(
            @RequestBody @Validated AddUpdateAdminRequest request
    ) {
        BaseResponse<CreateBaseResponse> response = authService.createAdmin(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/category/create")
    ResponseEntity<BaseResponse<CreateBaseResponse>> createCategory (
            @RequestBody @Validated CreateUpdateCategoryRequest body
    ) {
        BaseResponse<CreateBaseResponse> response = categoryService.createCategory(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/category/update")
    ResponseEntity<BaseResponse<Boolean>> updateCategory(
            @RequestParam @Validated String secureId,
            @RequestBody @Validated CreateUpdateCategoryRequest body
    ) {
        BaseResponse<Boolean> response = categoryService.updateCategory(secureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/category/delete")
    ResponseEntity<BaseResponse<Boolean>> deleteCategory(
            @RequestParam @Validated String secureId
    ) {
        BaseResponse<Boolean> response = categoryService.deleteCategory(secureId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/category/list")
    ResponseEntity<BaseResponse<List<CategoryResponse>>> getCategoryList(
            @RequestParam @Validated String title
    ) {
        BaseResponse<List<CategoryResponse>> response = categoryService.getCategoryList(title);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
