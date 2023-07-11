package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.request.exam.ExamRequest;
import xcode.ilmugiziku.domain.response.exam.CreateExamResponse;
import xcode.ilmugiziku.domain.response.exam.ExamRankResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ExamMapper {
    public ExamModel createRequestToModel(CreateExamRequest request, CreateExamResponse response) {
        if (request != null) {
            ExamModel result = new ExamModel();
            result.setSecureId(generateSecureId());
            // TODO: 11/07/23
//            result.setQuestions(arrayToString(request.getExams(), "question"));
//            result.setAnswers(arrayToString(request.getExams(), "answer"));
//            result.setBlank(response.getBlank());
//            result.setQuestionType(request.getQuestionType());
//            result.setQuestionSubType(request.getQuestionSubType());
//            result.setQuestionSubType(request.getQuestionSubType());
            result.setCreatedAt(new Date());

            return result;
        } else {
            return null;
        }
    }

    public ExamResultResponse modelToResultResponse(ExamModel model) {
        if (model != null) {
            ExamResultResponse result = new ExamResultResponse();
            // TODO: 11/07/23  
//            result.setQuestionSubType(model.getQuestionSubType());
            result.setDate(model.getCreatedAt());
//            result.setCorrect(model.getCorrect());
//            result.setScore(model.getScore());
//            result.setTotal(model.getBlank() + model.getCorrect() + model.getIncorrect());

            return result;
        } else {
            return null;
        }
    }

    public List<ExamResultResponse> modelsToResultResponses(List<ExamModel> models) {
        if (models != null) {
            List<ExamResultResponse> response = new ArrayList<>();

            for (ExamModel model : models) {
                response.add(modelToResultResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public ExamRankResponse modelToRankResponse(ExamModel model) {
        if (model != null) {
            ExamRankResponse result = new ExamRankResponse();
            // TODO: 11/07/23
//            result.setCorrect(model.getCorrect());
//            result.setTotal(model.getBlank() + model.getCorrect() + model.getIncorrect());

            return result;
        } else {
            return null;
        }
    }

    public List<ExamRankResponse> modelsToRankResponses(List<ExamModel> models) {
        if (models != null) {
            List<ExamRankResponse> response = new ArrayList<>();

            for (ExamModel model : models) {
                response.add(modelToRankResponse(model));
            }

            return response;
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

    public String[] stringToArray(String requests) {
        return requests.split(",");
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
