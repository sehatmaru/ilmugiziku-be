package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.request.question.CreateUpdateAnswerRequest;
import xcode.ilmugiziku.domain.response.answer.AnswerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class AnswerMapper {

    public List<AnswerModel> createRequestToModels(List<CreateUpdateAnswerRequest> models, String questionSecureId) {
        if (models != null) {
            List<AnswerModel> response = new ArrayList<>();

            models.forEach(e -> response.add(createRequestToModel(e, questionSecureId)));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public AnswerModel createRequestToModel(CreateUpdateAnswerRequest request, String questionSecureId) {
        if (request != null) {
            AnswerModel response = new AnswerModel();
            response.setSecureId(generateSecureId());
            response.setContent(request.getContent());
            response.setCorrectAnswer(request.isCorrectAnswer());
            response.setQuestion(questionSecureId);

            return response;
        } else {
            return null;
        }
    }

    public List<AnswerResponse> modelToResponses(List<AnswerModel> models) {
        if (models != null) {
            List<AnswerResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToResponse(e)));

            if (!CurrentUser.get().isAdmin()) Collections.shuffle(response, new Random(System.nanoTime()));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public AnswerResponse modelToResponse(AnswerModel model) {
        if (model != null) {
            AnswerResponse response = new AnswerResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            if (CurrentUser.get().isAdmin()) response.setCorrectAnswer(model.isCorrectAnswer());

            return response;
        } else {
            return null;
        }
    }

}
