package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.BenefitModel;
import xcode.ilmugiziku.domain.model.CourseBenefitRelModel;
import xcode.ilmugiziku.domain.repository.BenefitRepository;
import xcode.ilmugiziku.domain.repository.CourseBenefitRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseBenefitService {

   @Autowired private BenefitRepository benefitRepository;
   @Autowired private CourseBenefitRepository courseBenefitRepository;

   /**
    * get all course benefit registered before
    * @param course is courseSecureId
    * @return course benefit list
    */
   public List<BenefitModel> getCourseBenefits(String course) {
      List<BenefitModel> result = new ArrayList<>();

      List<CourseBenefitRelModel> courseBenefitModel = courseBenefitRepository.getCourseBenefits(course);
      courseBenefitModel.forEach(f -> result.add(benefitRepository.findBySecureIdAndDeletedAtIsNull(f.getBenefit())));

      return result;
   }

}
