package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.CreateExamResponse;

public class ExamMapper {
    public ExamModel createRequestToModel(CreateExamRequest request, CreateExamResponse response) {
        if (request != null) {
            ExamModel result = new ExamModel();
            result.setSecureId(result.getSecureId());
            result.setQuestions(arrayToString(request.getExams(), "question"));
            result.setAnswers(arrayToString(request.getExams(), "answer"));
            result.setScore(response.getScore());
            result.setBlank(response.getBlank());
            result.setCorrect(response.getCorrect());
            result.setIncorrect(response.getIncorrect());
            result.setQuestionType(request.getQuestionType());
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
}
