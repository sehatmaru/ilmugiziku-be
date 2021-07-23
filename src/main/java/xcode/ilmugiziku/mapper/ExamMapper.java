package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.CreateExamResponse;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ExamMapper {
    public ExamModel createRequestToModel(CreateExamRequest request, CreateExamResponse response) {
        if (request != null) {
            ExamModel result = new ExamModel();
            result.setSecureId(generateSecureId());
            result.setQuestions(arrayToString(request.getExams(), "question"));
            result.setAnswers(arrayToString(request.getExams(), "answer"));
            result.setBlank(response.getBlank());
            result.setQuestionType(request.getQuestionType());
            result.setQuestionSubType(request.getQuestionSubType());
            result.setQuestionSubType(request.getQuestionSubType());

            return result;
        } else {
            return null;
        }
    }

    private String arrayToString(ExamRequest[] requests, String type) {
        StringBuilder result = new StringBuilder();

        if (type.equals("question")) {
            for (ExamRequest exam: requests) {
                result.append(exam.getQuestionsSecureId());

                if (!exam.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        } else {
            for (ExamRequest exam: requests) {
                if (exam.getAnswersSecureId().isEmpty()) {
                    result.append("-");
                } else {
                    result.append(exam.getAnswersSecureId());
                }

                if (!exam.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        }

        return result.toString();
    }

    public CreateExamResponse generateResponse(ExamRequest[] exams) {
        CreateExamResponse result = new CreateExamResponse();
        int blank = 0;
        int answered = 0;

        for (ExamRequest exam: exams) {
            if (exam.getAnswersSecureId().isEmpty()) {
                blank += 1;
            } else {
                answered += 1;
            }
        }

        result.setSecureId(generateSecureId());
        result.setAnswered(answered);
        result.setBlank(blank);

        return result;
    }
}
