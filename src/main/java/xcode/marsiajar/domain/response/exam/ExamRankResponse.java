package xcode.marsiajar.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExamRankResponse {
    private String title;
    private int participant;
    private List<RankResponse> ranks = new ArrayList<>();
}
